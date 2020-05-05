import java.util.HashMap;

import components.Cache;
import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.MEM_WB;
import other.DatapathException;
import other.formatter;

public class Memory {

	//TODO: what should the mem access stage do?
	//Read + Write and set the pc
	//MR, MW, ALUres, readData2
	public static void mem() throws DatapathException
	{
		//getting the data from the pipeline register
		HashMap<String, String> input = EX_MEM.read();
		
		//MEM controls
		int memRead = Integer.parseInt(input.get("MemRead"));
		int memWrite = Integer.parseInt(input.get("MemWrite"));
		
		//directly goes to the MEM/WB register
		String rt = input.get("rd");
		
		//goes to address
		//note: ahmad uses Integer.toBinaryString() to write to the register
		//which doesn't guarantee 32 bits
		//TODO: ask him about this
		String ALUres = String.format("%32s", input.get("ALUResult")).replace(' ', '0');
		//goes to write data
		String readData2 = input.get("ReadData2");
		
		
		
		//we're only focusing on lw and sw
		//which can only set either MemRead and MemWrite for lw and sw respectively
		//but never both
		if(memRead==1 && memWrite==1)
			throw new DatapathException("MemRead and MemWrite signals are both set.");
		//reads data in the calculated address in ALUres
		else if(memRead==1 && memWrite==0)
		{
			//TODO: ask here too
			String readData = String.format("%32s", Cache.load(ALUres)).replace(' ', '0');
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		//saves read data 2 in the address calculated in the ALUres
		else if(memWrite==1 && memRead==0)
		{
			Cache.store(ALUres, readData2);
			//the strings we deal with are binary strings
			//so it can never be coincidentally that the data read is "don care"
			String readData = "don't care";
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		//do nothing
		else if(memRead==0 && memWrite==0)
		{
			String readData = "don't care";
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		else
			throw new DatapathException("Invalid signal value for MemRead and/or MemWrite." + '\n' 
		+"MemRead: " + memRead + '\n' + "MemWrite: " + memWrite);
		
		
	}
	
	public static void print(String ALUres, String readData, String rt, String MemToReg, String RegWrite)
	{
		if(!formatter.checknop(formatter.AssemblyStages[3]))
			System.out.println(formatter.AssemblyStages[3] + " in Memory stage:" + '\n' +
					           "ALU result: " + formatter.formatOut(ALUres) + '\n' +
					           "memory word read: " + (readData=="don't care" ? readData : formatter.formatOut(readData)) + '\n' +
					           "rt/rd field: " + rt + '\n' +
					           "WB controls: MemToReg: " + MemToReg + ", RegWrite: " + RegWrite);
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(Integer.toBinaryString(-2147483647));
		System.out.println((int)Long.parseLong(Integer.toBinaryString(-2147483647),2));
		System.out.println(Integer.toBinaryString(-10));
		System.out.println(String.format("%032d", 0));
		
		System.out.println("Invalid signal value for MemRead and/or MemWrite." + '\n' 
				+"MemRead: " + 1 + '\n' + "MemWrite: " + 2);
		String s = "0101";
		System.out.println(String.format("%32s", s).replace(' ', '0'));
		System.out.println(s=="0101" ? s : 1);
	}
}
