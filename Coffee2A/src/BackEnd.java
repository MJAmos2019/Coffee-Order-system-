
import java.io.*;

public class BackEnd {

	// Private access modifier.
	private Coffee[] cfe;
	private AcuteInflammations[] acuteInflammations;
	private int currentNum;

	// Constructor.
	// Initialises arrays.
	public BackEnd() {
		this.cfe = new Coffee[0];
	}

	// Increases or decreases array sizes by an increment or decrement.
	// Returns the new array size
	public int expandArrays(int inc) {
		Coffee[] newCfe = new Coffee[this.cfe.length + inc];
		int i = 0;
		while (i < Integer.min(this.cfe.length, newCfe.length)) {
			newCfe[i] = this.cfe[i];
			i += 1;
		}
		this.cfe = newCfe;
		return this.cfe.length;
	}

	// Method that looks up entries using name
	// Return array index.
	public int recLookup(String name) {
		int i = 0;
		while (i < this.cfe.length) {
			if (this.cfe[i].getName().matches("(?i).*" + name + ".*"))
				return i;
			i += 1;
		}
		return -1;
	}

	// Returns name, as a String.
	public String getName(int i) {
		return cfe[i].getName();
	}

	// Returns office number as an int.
	public int getOfficeNum(int i) {
		return cfe[i].getOfficeNum();
	}

	// Returns dietary requirements as a char.
	public char getDiet(int i) {
		return cfe[i].getDiet();
	}

	// Returns temperature as a float.
	public float getTemp(int i) {
		return cfe[i].getTemp();
	}

	// Returns true or false if using their own cup or not, as a boolean.
	public boolean getCup(int i) {
		return cfe[i].getCup();
	}

	// Returns the current number of order.
	public int getNum() {
		return cfe.length;
	}

	/*
	 * Method that adds entries to the record array. Returns message, error or
	 * success messages.
	 */
	public String addEntry(String[] userInputs) {
		if (userInputs.length != 5)
			return "Entry length incorrect. " + userInputs[0];
		int entry = this.expandArrays(1) - 1;

		// abort the program.
		// Do not close program, user renters order.

		try {
			// When Name is left blank it return an error message
			// likewise when Name begins with a lowercase.
			if (userInputs[0].matches("[a-z].*|")) {
				this.expandArrays(-1);
				return "Name invalid, Please re-enter. Names must start with a Capitilised letter.\n"
						+ "Invalid Input: " + userInputs[0];
			}
			// Temperature must be a positive integer. Zero for iced option
			if (Integer.valueOf(userInputs[1]) < 0) {
				this.expandArrays(-1);
				return "Please enter a postive integer for preferred coffee temperature \n "
						+ "Use 0 for iced coffee.\n" + "\nInvalid Input: " + userInputs[1];
			}
			// Dietary Requirements must match VGLN representing diet needs. Must be
			// Capitalised.
			if (!userInputs[2].matches("[VGLN]")) {
				this.expandArrays(-1);
				return "Dietary requirements must correspond with V, G,L or N exactly. Capitalisation matters.\nInvalid Input: "
						+ userInputs[2];
			}
			// Temperature must be entered, returns an error message if not entered.
			if ((Float.valueOf(userInputs[3]) < -100) || (Float.valueOf(userInputs[3]) > 100)) {
				this.expandArrays(-1);
				return "Temperature must be between 0 and 100.\nInvalid Input: " + userInputs[3];
			}

			this.cfe[entry] = new Coffee(userInputs[0], Integer.valueOf(userInputs[1]), userInputs[2].charAt(0),
					Float.valueOf(userInputs[3]), Boolean.valueOf(userInputs[4]));
		} catch (NumberFormatException e) {
			return String.valueOf(e);
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Too much data entered. Please reread instructions.";
		}
		// Displays successful data entry.
		entry += 1;
		return "Order number " + entry + " added successfully.";
	}

	// Removes an entry from arrays.
	public String removeEntry(String reference) {
		int ind = -1;
		try {
			ind += Integer.valueOf(reference);
		} catch (NumberFormatException e) {
			ind = this.recLookup(reference);
		}
		String name;
		try {
			name = this.cfe[ind].getName();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "No Order found.";
		}
		int i = 0;
		while (i < this.cfe.length - 1) {
			if (i >= ind) {
				this.cfe[i] = this.cfe[i + 1];
			}
			i += 1;
		}
		// Reduces array size to adjust for the deleted entry.
		this.expandArrays(-1);
		return "Order \"" + name + "\" deleted successfully.";
	}

	public String[] openFile() {
		BufferedReader reader;
		boolean divide = false;
		try {
			reader = new BufferedReader(new FileReader("data.rtf"));

			int lineNum = 1;
			String text = reader.readLine();

			while (text != null) {
				if (text.matches("\\d+,\\d+\\d+,\\d+,\\d+,\\d+\\.\\d+,[12]"))
					lineNum += 1;
				text = reader.readLine();
			}
			this.acuteInflammations = new AcuteInflammations[lineNum - 1];
			reader = new BufferedReader(new FileReader("data.rtf"));
			lineNum = 0;

			text = reader.readLine();
			String[] inputs = new String[9];
			while ((text != null) && (text.matches("\\d+,\\d+\\d+,\\d+,\\d+,\\d+\\.\\d+,[12]"))) {
				inputs = text.split(",");
				if (inputs[8].matches("1"))
					divide = true;

				else if (inputs[8].matches("2"))
					divide = false;
				this.acuteInflammations[lineNum] = new AcuteInflammations(Integer.valueOf(inputs[0]),
						Boolean.valueOf(inputs[1]), Boolean.valueOf(inputs[2]), Boolean.valueOf(inputs[3]),
						Boolean.valueOf(inputs[4]), Boolean.valueOf(inputs[5]), Boolean.valueOf(inputs[6]),
						Boolean.valueOf(inputs[7]), divide);
				lineNum += 1;
				text = reader.readLine();
			}

			//
			//
			reader = new BufferedReader(new FileReader("diagnosis.names"));
			lineNum = 0;
			text = "";
			while (text != null) {
				text = reader.readLine();
				lineNum += 1;
			}
			String[] name = new String[lineNum - 1];

			reader = new BufferedReader(new FileReader("diagnosis.names"));
			text = reader.readLine();
			lineNum = 0;
			while (text != null) {
				name[lineNum] = text;
				lineNum += 1;
				text = reader.readLine();
			}
			reader.close();
			return name;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (NumberFormatException e) {
			return null;
		}

	}

	public void save() {
		BufferedWriter writer;
		int i;
		byte divide;
		try {
			writer = new BufferedWriter(new FileWriter("data.rtf"));
			i = 0;
			while (i < this.acuteInflammations.length) {
				if (this.getDivide(i))
					divide = 1;
				else
					divide = 2;
				writer.write(String.valueOf(this.getTemp(i)) + "," + String.valueOf(this.getLumbarPain(i)) + ","
						+ String.valueOf(this.getUrine(i)) + "," + String.valueOf(this.getMicturitionPains(i)) + ","
						+ String.valueOf(this.getswelling(i)) + "," + String.valueOf(this.getinflammation(i)) + ","
						+ String.valueOf(this.getNephritis(i)) + "," + String.valueOf(divide) + "\n");

				i += 1;
			}
			writer.close();
		} catch (IOException e) {
			return;
		}
	}

	// Load file method:
	public String loadFile(String filename) {
		String outcome = null;
		try {
			BufferedReader fileObj = new BufferedReader(new FileReader(filename));
			String line = fileObj.readLine();
			while (line != null) {
				String[] fieldsOfCurrLine = line.split(",");
				int Temp = fieldsOfCurrLine[0].charAt(0);
				boolean nausea = Boolean.valueOf(fieldsOfCurrLine[1]);
				boolean MicturitionPains = Boolean.valueOf(fieldsOfCurrLine[2]);
				boolean LumbarPain = Boolean.valueOf(fieldsOfCurrLine[3]);
				boolean swelling = Boolean.valueOf(fieldsOfCurrLine[4]);
				boolean Nephritis = Boolean.valueOf(fieldsOfCurrLine[5]);
				boolean inflammation = Boolean.valueOf(fieldsOfCurrLine[6]);
				boolean Urine = Boolean.valueOf(fieldsOfCurrLine[7]);
				boolean Divide = Boolean.valueOf(fieldsOfCurrLine[8]);

				AcuteInflammations(Temp, nausea, MicturitionPains, LumbarPain, swelling, Nephritis, inflammation, Urine,
						Divide);
				line = fileObj.readLine();
			}

			fileObj.close();
		} catch (Exception e) {
			outcome = e.getMessage();
		}

		return outcome;
	}

	public void AcuteInflammations(int Temp, boolean nausea, boolean MicturitionPains, boolean LumbarPain,
			boolean swelling, boolean Nephritis, boolean inflammation, boolean Urine, boolean Divide) {
		if (this.currentNum < this.acuteInflammations.length) {
			this.acuteInflammations[this.currentNum] = new AcuteInflammations(Temp, nausea, MicturitionPains,
					LumbarPain, swelling, Nephritis, inflammation, Urine, Divide);
			this.currentNum++;
		}
	}

	public int getTempA(int i) {
		return this.acuteInflammations[i].getTemp();
	}

	public boolean getnausea(int i) {
		return this.acuteInflammations[i].getnausea();
	}

	public boolean getMicturitionPains(int i) {
		return this.acuteInflammations[i].getMicturitionPains();
	}

	public boolean getLumbarPain(int i) {
		return this.acuteInflammations[i].getLumbarPain();
	}

	public boolean getswelling(int i) {
		return this.acuteInflammations[i].getswelling();
	}

	public boolean getNephritis(int i) {
		return this.acuteInflammations[i].getNephritis();
	}

	public boolean getinflammation(int i) {
		return this.acuteInflammations[i].getinflammation();
	}

	public boolean getUrine(int i) {
		return this.acuteInflammations[i].getUrine();
	}

	public boolean getDivide(int i) {
		return this.acuteInflammations[i].getDivide();
	}
	//

	public int getNumA() {
		return this.acuteInflammations.length;
	}
}
//gt.showHelp();
// https://stackoverflow.com/questions/30113062/user-input-string-into-string-array
// https://www.w3schools.com/java/java_arrays.asp
// https://jupiter.csit.rmit.edu.au/~e58140/GTerm/doc/GTerm.html#mousePressed(java.awt.event.MouseEvent)
	