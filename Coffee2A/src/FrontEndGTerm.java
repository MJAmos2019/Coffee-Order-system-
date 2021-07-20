
public class FrontEndGTerm {


	private BackEnd backEnd;
	private GTerm gt;
	// Used to store table, entryBox index.
	private int[] EntryBox;
	private GTerm gt1;
	private int []indicies;

	public FrontEndGTerm(BackEnd B1) {
		this.backEnd = B1;
		this.EntryBox = new int[8];
		this.GTW();
		this.gt1 = null;
		this.indicies = new int [2];
	}

	public void GTW() {
		// GTerm window Style
		// Int variable for positioning components in the GTerm Window
		int Button = 31;
		int z = 0;
		int textLine = 25;
		this.gt = new GTerm(1280, 800);
		this.gt.setBackgroundColor(213, 242, 238);
		this.gt.setFontSize(20);
		this.gt.setXY(15, 0);
		// Title
		this.gt.println("     Office Coffee Order Log:");
		this.gt.println("           Management system ");
		z += 92;
		this.gt.setFontSize(12);
		this.gt.setXY(0, 93);
		// Table
		EntryBox[0] = this.gt.addTable(625, 650, " Name\t Office Number \t Dietary \t Temperature\t Own Cup?");
		z += 361;
		// Buttons and TextFields
		this.gt.setXY(722, 100);
		this.gt.addButton("Instructions", this, "Instructions");
		z += Button;
		this.gt.setXY(722, 380);
		this.gt.addButton("Add Record ", this, "addRecordEntry");
		z += Button;
		this.gt.setXY(640, 160);
		this.gt.print("Name: ");
		this.EntryBox[1] = this.gt.addTextField("", 100);
		this.gt.setXY(640, 200);
		this.gt.print(" Office Number: ");
		this.EntryBox[2] = this.gt.addTextField("", 100);
		this.gt.setXY(640, 240);
		this.gt.print(" V|G|L|N: ");
		this.EntryBox[3] = this.gt.addTextField("", 100);
		z += textLine;
		this.gt.setXY(640, 280);
		this.gt.print("Temperature: ");
		this.EntryBox[4] = this.gt.addTextField("", 100);
		this.gt.setXY(640, 320);
		this.gt.print("Will you be using your own cup? True/False ");
		this.EntryBox[5] = this.gt.addTextField("", 80);
		z += textLine;
		this.gt.setXY(700, z);
		this.EntryBox[6] = this.gt.addTextField("", 100);
		this.gt.setXY(800, z);
		this.gt.addButton("Edit Record", this, "editEntry");
		z += Button;
		this.gt.setXY(700, z);
		this.EntryBox[7] = this.gt.addTextField("", 100);
		this.gt.setXY(800, z);
		this.gt.addButton("Remove Record", this, "removeRecord");
		this.gt.addButton("Load/Save", this, "gTermWindow");
	}

	// Instructions GTerm window that give details to users on how to place an order
	// Could have also been displayed in the Main GTerm but did not suit the
	// esthetic.
	public void Instructions() {
		// GTerm Style

		this.gt = new GTerm(600, 500);
		this.gt.setBackgroundColor(204, 229, 255);

		this.gt.setFontSize(14);

		this.gt.println("     Office Coffee Order Log Instructions:");
		this.gt.println("    ");
		this.gt.println("Please enter the details of your order in thier corresponding entry boxes\n "
				+ "Name, Capitalised - E.G Maryjane \n " + "Your office building number - Must be an integer\n"
				+ "Any dietary requirements (as a Capitalised letter), \n" + "							 V-vegan \n"
				+ "							 G- Gluten Free \n" + "							 L- Lactose intolerant \n"
				+ "							 N- None \n" + "Preffered coffee temperature - Must be a numerical value \n"
				+ "True or false: Will you be using your own cup?  - Capitalised Eg. True \n");

	}

	// Method to add record via the Gterm button.
	public void addRecordEntry() {
		String userInputs[] = new String[5];
		int i = 0;
		while (i < userInputs.length) {
			userInputs[i] = this.gt.getTextFromEntry(this.EntryBox[i + 1]);
			this.gt.setTextInEntry(this.EntryBox[i + 1], "");
			i += 1;
		}
		this.gt.showMessageDialog(backEnd.addEntry(userInputs));
		// Calls refresh method to Make the table show the new entries.
		refreshTable(this.gt, this.EntryBox[0]);
	}

	// Removes order based on entry, or shows error message as returned by
	// BackEnd.removeEntry()
	public void removeRecord() {
		String entry = "";
		// prompts user to selected an entry
		if (this.gt.getTextFromEntry(this.EntryBox[7]).equals("")) {
			entry = this.gt.getInputString("Please enter a name or number for entry to remove.");
		} else
			entry = this.gt.getTextFromEntry(this.EntryBox[7]);
		this.gt.showMessageDialog(backEnd.removeEntry(entry));
		refreshTable(this.gt, this.EntryBox[0]);
		this.gt.setTextInEntry(this.EntryBox[7], "");
	}

	// Method to validate input
	// Delete previous input and re-enters new data
	public void editEntry() {
		// Existing inputs stored before deletion.
		String order[] = new String[5];
		// The order being searched.
		String lookup;
		// Negative value used to offset
		int ind = -1;
		// returns true if data in entered, false if no data is entered.
		boolean mode;
		// Two options for text field entry or Gterm Window entry
		if (this.gt.getTextFromEntry(this.EntryBox[6]).equals("")) {
			mode = false;
			lookup = this.gt.getInputString("Please enter a name or number for entry to edit.");
		} else {
			mode = true;
			lookup = this.gt.getTextFromEntry(this.EntryBox[6]);
		}
		try {
			ind += Integer.valueOf(lookup);
		} catch (NumberFormatException e) {
			ind = backEnd.recLookup(lookup);
		}
		// stops if invalid
		if (ind == -1) {
			this.gt.showErrorDialog("Order not found.");
			return;
		} else {
			// Records the entry so that after deletion it can be re-added.
			order[0] = backEnd.getName(ind);
			order[1] = String.valueOf(backEnd.getOfficeNum(ind));
			order[2] = String.valueOf(backEnd.getDiet(ind));
			order[3] = String.valueOf(backEnd.getTemp(ind));
			order[4] = String.valueOf(backEnd.getCup(ind));
			// removes initial order.
			this.gt.showMessageDialog(backEnd.removeEntry(String.valueOf(ind + 1)));
			if (!mode)
				this.gt.showMessageDialog(backEnd.addEntry(this.gt.getInputString(
						"Please enter the details of your order seperated by a comma(,):\nName, your office building number,\n"
								+ "Any dietary requirements (as a letter), \n" + " V-vegan \n" + " G- Gluten Free \n"
								+ " L- Lactose intolerant \n " + "N- None \n" + " Preffered coffee temperature,  \n"
								+ " True or false: Will you be using your own cup?" + order[0] + " | " + order[1]
								+ " | " + order[2] + " | " + order[3] + " | " + order[4])
						.split("\\s*+[,|;:\\\\\\/]\\s*+", 5)));
			else {
				// fills the text entry boxes so that they can edit
				// data can be re-entered  with the Add button.
				this.gt.setTextInEntry(this.EntryBox[1], order[0]);
				this.gt.setTextInEntry(this.EntryBox[2], order[1]);
				this.gt.setTextInEntry(this.EntryBox[3], order[2]);
				this.gt.setTextInEntry(this.EntryBox[4], order[3]);
				this.gt.setTextInEntry(this.EntryBox[5], order[4]);
			}
			this.refreshTable(this.gt, this.EntryBox[0]);
			this.gt.setTextInEntry(this.EntryBox[6], "");
		}
	}

	// Method to Reset the table with all values from the existing inputs from
	// BackEnd.
	public void refreshTable(GTerm gt, int ind) {
		if (backEnd.getNum() == 0)
			this.gt.showErrorDialog("No records.");
		gt.clearRowsOfTable(this.EntryBox[0]);
		int i = 0;
		while (i < backEnd.getNum()) {
			gt.addRowToTable(ind, backEnd.getName(i) + "\t" + backEnd.getOfficeNum(i) + "\t" + backEnd.getDiet(i) + "\t"
					+ backEnd.getTemp(i) + "\t" + backEnd.getCup(i));
			i += 1;
		}
		// obtains the numbers needed to calculate the average showed on table
		// the total highest and lowest values of temperature.
		// Values needed to calculate average below
		// lowest, highest and total.
		float tempTot = 0;
		int OfNumTot = 0;
		int maxOfNum = 0;
		float maxTemp = 0;
		int minOfNum = backEnd.getOfficeNum(0);
		float minTemp = backEnd.getTemp(0);
		i = 0;
		while (i < backEnd.getNum()) {
			OfNumTot += backEnd.getOfficeNum(i);
			tempTot += backEnd.getTemp(i);
			if (maxOfNum < backEnd.getOfficeNum(i))
				maxOfNum = backEnd.getOfficeNum(i);
			else if (minOfNum > backEnd.getOfficeNum(i))
				minOfNum = backEnd.getOfficeNum(i);
			if (maxTemp < backEnd.getTemp(i))
				maxTemp = backEnd.getTemp(i);
			else if (minTemp > backEnd.getTemp(i))
				minTemp = backEnd.getTemp(i);
			i += 1;
		}
		// Prints average values for number of orders and temperature.
		gt.addRowToTable(ind, "Averages:\t" + OfNumTot / backEnd.getNum() + "\t\t" + tempTot / backEnd.getNum());

	}
	public void gTermWindow() {			
		this.gt1 = new GTerm(1380, 800);
		this.gt1.setBackgroundColor(213, 242, 238);
		this.gt1.setTitle("Acute Inflammtion");
		this.gt1.println(" ");
		this.gt1.println(" ");
		int tableW = 700;
		int tableH = 650;
		this.gt1.setXY(800, 20);
		this.gt1.addButton("Load", backEnd, "loadFile");
		this.gt1.setXY(800, 60);
		this.gt1.addButton("save", backEnd, "save");
		this.gt1.setXY(800, 100);
		this.gt1.addButton("open", backEnd, "openFile");
		this.gt1.setXY(0, 23);
		this.indicies[0] = this.gt1.addTable(tableW, tableH, "nausea\t LumbarPain \t Urine \t MicturitionPains \t swelling \t inflammation \t Nephritis ");
		this.gt1.setFontSize(16);
		String [] info = this.backEnd.openFile();
		int i = 0;
		int ind = 0;
		
		int oneCount = 0;
		while(i < this.backEnd.getNum()) {
			
			if (this.backEnd.getDivide(i)) {
			ind = 0;
			oneCount += 1;
		} else {
			ind = 1;
		}
		this.gt1.addRowToTable(this.indicies[ind], this.backEnd.getTemp(i)+"\t"+ this.backEnd.getLumbarPain(i)+"\t"+ this.backEnd.getUrine(i)
		+"\t"+ this.backEnd.getMicturitionPains(i)+"\t"+ this.backEnd.getswelling(i)+"\t"+ this.backEnd.getinflammation(i)
		+"\t"+ this.backEnd.getNephritis(i));
		
		i+= 1;
		}
		this.gt1.setXY(0, 0);
		this.gt1.print("Acute Inflammtion");
		this.gt1.setXY(tableW, 0);
		this.gt1.setFontSize(12);
		this.gt1.setXY((tableW * 2)+10, 0);
		i=0;
		while (i< info.length) {
			this.gt1.println("Review results click here: ");
			this.gt1.setXY(0, tableH +25);
			this.gt1.addButton("save", backEnd, "save");
		}
}
	
}

//gt.showHelp();
// https://stackoverflow.com/questions/30113062/user-input-string-into-string-array
// https://www.w3schools.com/java/java_arrays.asp
// https://jupiter.csit.rmit.edu.au/~e58140/GTerm/doc/GTerm.html#mousePressed(java.awt.event.MouseEvent)
