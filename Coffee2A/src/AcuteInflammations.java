public class AcuteInflammations {
	private int Temp;
	private boolean nausea;
	private boolean LumbarPain;
	private boolean Urine;
	private boolean MicturitionPains;
	private boolean swelling;
	private boolean inflammation;
	private boolean Nephritis;
	private boolean divide;
	
	public AcuteInflammations(int Temp, boolean nausea , boolean LumbarPain,boolean Urine, boolean MicturitionPains , boolean swelling,boolean inflammation, boolean Nephritis,boolean divide) {
		this.Temp = Temp;
		this.nausea = nausea;
		this.LumbarPain = LumbarPain;
		this.Urine = Urine;
		this.MicturitionPains = MicturitionPains;
		this.swelling = swelling;
		this.inflammation = inflammation;
		this.Nephritis =  Nephritis;	
		this.divide = divide;
	}
	public int getTemp() {
		return this.Temp;
	}
	
	public boolean getnausea() {
		return this.nausea;
	}
	
	public boolean getMicturitionPains() {
		return this.MicturitionPains;
	}
	
	public boolean getLumbarPain() {
		return this.LumbarPain;
	}
	
	public boolean getswelling() {
		return this.swelling;
	}
	
	public boolean getNephritis() {
		return this.Nephritis;
	}
	
	public boolean getinflammation() {
		return this.inflammation;
	}
	
	public boolean getUrine() {
		return this.Urine;
	}
	
	public boolean getDivide() {
		return this.divide;
	}
}
