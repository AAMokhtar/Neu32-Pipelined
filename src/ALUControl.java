
public class ALUControl {
	
	/*
	 * returns a String of 4 bits dictating the ALU operation.
	 * 
	 * inputs: funct (!3 bits), and ALUop (3 bits)
	 * 
	 */
	
	public static String ALUSignals(String funct, String ALUop) {
		
		if(ALUop.length()!=3 || funct.length()!=13) {
			System.out.println("Length of inputs is incorrect. Return value: NULL");
			return null;
		}
		
		String ret = "";
		
		
		switch(ALUop) {
			case "000": ret = "0000";break;
			case "010": ret = "0100";break;
			case "011": ret = "0101";break;
			case "100": ret = "0110";break;
			case "101": ret = "0001";break;
			case "001":
				switch(funct) {
					case "0000000000000":ret = "0000";break;
					case "0000000000001":ret = "0001";break;
					case "0000000000010":ret = "0010";break;
					case "0000000000011":ret = "0011";break;
					case "0000000000100":ret = "0111";break;
					default: System.out.println("Invalid funct field! Return value: NULL"); return null;
				}
				break;
			default: System.out.println("invalid ALUOp! Return value: NULL"); return null;
		}

		return ret;
	}
	
	public static void main(String blabla[]) {
		
	}

}
