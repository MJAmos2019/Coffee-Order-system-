
public class Coffee {
	// private data types

	private String name;
	private int officeNum;
	private char diet;
	private float temp;
	private boolean cup;

	// Constructor Method
	// Initialisations
	public Coffee(String name, int officeNum, char diet, float temp, boolean cup) {
		this.name = name;
		this.officeNum = officeNum;
		this.diet = diet;
		this.temp = temp;
		this.cup = cup;
	}

	// Returns name, as a String.
	public String getName() {
		return this.name;
	}

	// Returns office number as an int.
	public int getOfficeNum() {
		return this.officeNum;
	}

	// Returns dietary requirements as a char.
	public char getDiet() {
		return this.diet;
	}

	// Returns temperature as a float.
	public float getTemp() {
		return this.temp;
	}

	// Returns true or false if using their own cup or not, as a boolean.
	public boolean getCup() {
		return this.cup;
	}
}
//References 
//gt.showHelp();
// https://stackoverflow.com/questions/30113062/user-input-string-into-string-array
// https://www.w3schools.com/java/java_arrays.asp
// https://jupiter.csit.rmit.edu.au/~e58140/GTerm/doc/GTerm.html#mousePressed(java.awt.event.MouseEvent)
