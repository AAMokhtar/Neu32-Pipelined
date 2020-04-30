
public class BranchControl {
	
	/*
	 * inputs: jump 1 bit, branch 1 bit, gflag 1 bit, and zflag 1 bit.
	 * output: the 2 bit string that will go into the 4x1 mux
	 */
	
	public String branchSignals(String jump, String branch, String Gflag, String Zflag) {
		
		String ret = "";
		
		if(jump.equals("0")&&branch.equals("0")) {
			ret = "00";
		}
		
		else if (jump.equals("0")&&branch.equals("1")&&Gflag.equals("1")&&Zflag.equals("0")) {
			ret = "01";
		}
		
		else if (jump.equals("0")&&branch.equals("1")&&Gflag.equals("0")&&Zflag.equals("1")) {
			ret = "10";
		}
		
		else if (jump.equals("1")&&branch.equals("0")) {
			ret = "11";
		}
		
		else {
			System.out.println("Illegal state: branching problematic! Return value: NULL"); ret = null;
		}
		
		return ret;
		
	}
}
