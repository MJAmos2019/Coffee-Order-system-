import java.util.InputMismatchException;
import java.util.Scanner;

public class FrontEndConsole {

	private BackEnd backEnd;
	private Scanner console;

	// Constructor Method
	public FrontEndConsole(BackEnd B1) {
		this.backEnd = B1;
		this.console = new Scanner(System.in);
		System.out.println("Both Console and GTerm Window could be used to place an order.");

		// Calls cmdListen method to check user commands
		this.cmdListen();
	}

	// Method that checks user commands
	// Method loops and checks commands.
	public void cmdListen() {
		System.out.println(
				"\n" + "Hello,place your coffee order below!\n" + "Follow the instructions to place an order.\n");
		String cmd = "info";
		while (!cmd.matches("(?i)quit|exit|close|esc(?:ape)?|0")) {
			// While loops check that commands match.
			if (cmd.matches("(?i)(?:a(?:dd)?(?: rec(?:ord)?| entry)?|1)\\s++[^,|;:\\\\\\/]+"
					+ "(?:\\s*+[,|;:\\\\\\/]\\s*+[^,|;:\\\\\\/]+){4}")) {
				// Outputs an error or success message, while adding entry as typed in the
				// command line
				System.out
						.println(backEnd.addEntry(cmd.replaceFirst("(?i)(?:a(?:dd)?(?: rec(?:ord)?| entry)?|1)\\s+", "")
								.split("\\s*+[,|;:\\\\\\/]\\s*+", 5)));
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)a(?:dd)?(?: rec(?:ord)?| entry)?|1")) {
				// Prompt for inputs and outputs a message, error or success.
				// True indicates for the command "add".
				this.askInputs(true);
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)(?:d(?:el(?:ete)?)?|2)\\s++.+")) {
				// Outputs success or error message, while removing input entered in the command
				// line
				System.out.println(backEnd.removeEntry(cmd.replaceFirst("(?i)(?:d(?:el(?:ete)?)?|2)\\s+", "")));
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)(?:d(?:el(?:ete)?)?|remove|2)")) {
				// Prompt for inputs and outputs a message, error or success.
				// False indicates for the command "delete".
				this.askInputs(false);
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)(?:e(?:dit)?|3)\\s++.*")) {
				System.out.println(this.editRec(cmd.replaceFirst("(?i)(?:e(?:dit)?|3)\\s+", "")));
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)e(?:dit)?|3")) {
				System.out.println("which order would you like to edit?");
				System.out.println(this.editRec(this.console.nextLine()));
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)(?:display|show|table|out(?:put)?)(?: table)?|4")) {
				// calls DispalyTable method
				this.displayTable();
				cmd = this.console.nextLine();
			} else if (cmd.matches("(?i)i(?:nfo)?|cmd|list|(?:inate)?|5")) {
				System.out.println("Enter add to add an order(s),\n " + "       delete - to delete an order(s),\n"
						+ "	edit - to edit an" + " order,\n " + "	show - to display a table of current orders,\n"
						+ "	info - to show this list of commands,\n" + "	quit - to exit.\n");
				cmd = this.console.nextLine();
			} else {
				System.out.println("Error: invalid command. Follow the below instructions.");
				cmd = "info";
			}
		}
		System.out.print("Thank you for placing your coffee order");
		System.exit(0);
	}

	// Asks user for data input to delete or add new orders.
	// Repeats
	public void askInputs(boolean mode) {
		if (mode)
			System.out.println("How many orders would you like to place?");
		else
			System.out.println("How many orders would you like to remove?");

		// NextInt validates and catches string inputs.
		try {
			int mtNum = this.console.nextInt();
			this.console.nextLine();
			int i = 0;
			String input = "";

			if (mode) {
				while (i < mtNum) {
					System.out.println(
							"Please enter the details of your order seperated by a comma(,):\nName, your office building number,\n"
									+ "Any dietary requirements (as a letter), \n" + " V-vegan \n"
									+ " G- Gluten Free \n" + " L- Lactose intolerant \n " + "N- None \n"
									+ " Preffered coffee temperature,  \n"
									+ " True or false: Will you be using your own cup?");
					input = this.console.nextLine();
					System.out.println(backEnd.addEntry(input.split("\\s*+[,|;:\\\\\\/]\\s*+", 5)));
					i += 1;
				}
			} else {
				while (i < mtNum) {
					System.out.println("Please enter the name or entry number of orders to delete.\n"
							+ "As order numbers are deleted the order entry number changes.");
					System.out.println(backEnd.removeEntry(this.console.nextLine()));
					i += 1;
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("Order needs to be numerical(integers) only.");
		}
	}

	// Opens a record, outputs it, deletes it, replaces it.
	public String editRec(String name) {
		// In searching for a way to prefill text for editing, I discovered
		// useDelimiter.
		// Considering reworking the whole thing to include it.
		// Could not find a way to prefill entries.
		int ref = -1;
		// If they give a number, use a number. Otherwise, look it up using recLookup().
		try {
			ref += Integer.valueOf(name);
		} catch (NumberFormatException e) {
			ref = backEnd.recLookup(name);
		}
		// If that's still not a record, say so.
		if ((ref < 0) || (ref > backEnd.getNum()))
			return "No order found.";
		// Display current entry, for copy and pasting purposes. Then ask for new entry.
		System.out.println("Current entry selected:\n" + backEnd.getName(ref) + " | " + backEnd.getOfficeNum(ref)
				+ " | " + backEnd.getDiet(ref) + " | " + backEnd.getTemp(ref) + " | " + backEnd.getCup(ref) + "\n"
				+ "Please enter edited order details:");
		// Delete entry is poised to take "human" entry values, so add one to get the
		// index value.
		backEnd.removeEntry(String.valueOf(ref + 1));
		// Listen for edited entry.
		return backEnd.addEntry(this.console.nextLine().split("\\s*+[,|;:\\\\\\/]\\s*+", 5));
	}

	// Display Table method to output orders
	public void displayTable() {
		if (backEnd.getNum() == 0) {
			System.out.println("No Orders.");
			return;
		}
		// Finds the longest name and Office.
		// shortest name width for output rows.
		int nameWidth = 9;
		int OFWidth = 0;
		int i = 0;
		while (i < backEnd.getNum()) {
			if (nameWidth < backEnd.getName(i).length())
				nameWidth = backEnd.getName(i).length();
			if (OFWidth < String.valueOf(backEnd.getOfficeNum(i)).length())
				OFWidth = String.valueOf(backEnd.getOfficeNum(i)).length();
			i += 1;
		}
		// Office nuumber Width is increased by two to adjust for averages row.
		OFWidth += 2;
		// Set line divider using the above values.
		String divider = "+-" + "-".repeat(nameWidth) + "-+-" + "-".repeat(OFWidth) + "-+---+--------+-------+";
		// Displays table
		System.out.println(divider);
		i = 0;
		while (i < backEnd.getNum()) {
			System.out.printf("| %1$-" + nameWidth + "s | %2$" + OFWidth + "d | %3$s | %4$02.3f | %5$5s |%n",
					backEnd.getName(i), backEnd.getOfficeNum(i), backEnd.getDiet(i), backEnd.getTemp(i),
					backEnd.getCup(i));
			System.out.println(divider);
			i += 1;
		}
		// obtains the numbers needed to calculate the average showed on table
		// the total highest and lowest values of temperature.
		// Values needed to calculate average below
		// lowest, highest and total.

		int OFTot = 0;
		float tempTot = 0;
		int minOF = backEnd.getOfficeNum(0);
		float minTemp = backEnd.getTemp(0);
		int maxOF = 0;
		float maxTemp = 0;
		i = 0;
		while (i < backEnd.getNum()) {
			maxOF += backEnd.getOfficeNum(i);
			tempTot += backEnd.getTemp(i);
			if (maxOF < backEnd.getOfficeNum(i))
				maxOF = backEnd.getOfficeNum(i);
			else if (minOF > backEnd.getOfficeNum(i))
				minOF = backEnd.getOfficeNum(i);
			if (maxTemp < backEnd.getTemp(i))
				maxTemp = backEnd.getTemp(i);
			else if (minTemp > backEnd.getTemp(i))
				minTemp = backEnd.getTemp(i);
			i += 1;
		}
		// Prints average values as additional rows.
		System.out.printf("| %1$" + nameWidth + "s | %2$" + OFWidth + ".1f |   | %3$02.3f |       |%n", "Averages:",
				(float) OFTot / backEnd.getNum(), tempTot / backEnd.getNum());
		System.out.println(divider);

	}
}
//gt.showHelp();
// https://stackoverflow.com/questions/30113062/user-input-string-into-string-array
// https://www.w3schools.com/java/java_arrays.asp
// https://jupiter.csit.rmit.edu.au/~e58140/GTerm/doc/GTerm.html#mousePressed(java.awt.event.MouseEvent)
