package stages;

import java.util.HashMap;

import components.MUX;
import components.RegisterFile;
import components.pipelineRegs.MEM_WB;
import other.DatapathException;
import other.formatter;

public class WriteBack {
	
	public static void wb() throws DatapathException
	{
		//getting the data from the pipeline register
		HashMap<String, String> input = MEM_WB.read();
		
		//WB controls
		int memToReg = Integer.parseInt(input.get("MemToReg"));
		int regWrite = Integer.parseInt(input.get("RegWrite"));
		
		
		//to data input line 1 of 2x1 MUX
		String readData = input.get("ReadData");
		//to data input line 0 of 2x1 MUX
		String ALUres = input.get("ALUResult");
		//to write register
		String rt = input.get("rt");
		
		//you can never be too sure
		if(regWrite!=1 || regWrite!=0)
			throw new DatapathException("RegWrite value of: " + regWrite + " is invalid");
		if(memToReg!=1 || memToReg!=0)
			throw new DatapathException("MemToReg value of: " + memToReg + " is invalid");
		
		String writeData = (String) MUX.mux2in(ALUres, readData, memToReg);
		
		//write the data chosen by the MUX to the register
		if(regWrite==1)
			RegisterFile.writedata(writeData, rt);
		
		print();
	}
	
	public static void print()
	{
		if(!formatter.checknop(formatter.AssemblyStages[4]))
			System.out.println(formatter.AssemblyStages[4] + " in WB stage");
	}
	
}
