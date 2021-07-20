
public class App {
	private BackEnd backEnd;
	private FrontEndGTerm uiGT;
	private FrontEndConsole uiConsole;

	public App() {
		this.backEnd = new BackEnd();
		this.uiGT = new FrontEndGTerm(backEnd);
		this.uiConsole = new FrontEndConsole(backEnd);
	}

	public static void main(String[] args) {
		App app = new App();
	}
}
