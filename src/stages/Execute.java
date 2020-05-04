package stages;

import components.ALU;
import components.ALUControl;
import components.MUX;
import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.ID_EX;
import other.DatapathException;
import other.formatter;
import other.operations;

import java.util.HashMap;

public class Execute {
    public static void run() throws DatapathException {

        //===============get relevant inputs from ID_EX=======================
            HashMap<String, String> input = ID_EX.read();
            String ReadData1 = input.get("ReadData1");
            String ReadData2 = input.get("ReadData2");

            String Immediate = input.get("Immediate");

        //==========================EX control================================
            String ALUop = input.get("ALUop");
            String ALUSrc = input.get("ALUSrc");
            String funct = input.get("funct");

        //======================forwarding signals============================
            /*
             * Cases:
             *  - ForwardA = 00: First ALU operand comes from register file = Value of (rs)
             *  - ForwardA = 01: Forward result of previous instruction to A (from ALU stage)
             *  - ForwardA = 10: Forward result of 2nd previous instruction to A (from MEM stage)
             *  - ForwardA = 11: Don't care
             *
             *  - ForwardB = 00: First ALU operand comes from register file = Value of (rt)
             *  - ForwardB = 01: Forward result of previous instruction to A (from ALU stage)
             *  - ForwardB = 10: Forward result of 2nd previous instruction to A (from MEM stage)
             *  - ForwardB = 11: Don't care
             */
            String ForwardA = "00" /*TODO: get signal from the forwarding unit*/;
                String ForwardB = "00" /*TODO: get signal from the forwarding unit*/;

        //=======================set the operands=============================

            //the forwarding unit decides the source of the operands
            String mux1 = (String) MUX.mux4in(ReadData1, EX_MEM.ALUResult(), 0/*TODO: MEM/WB value*/,
                    ReadData1,ForwardA.charAt(0)+"",ForwardA.charAt(1)+"");

            int operand1 = Integer.parseInt(mux1,2);


            String mux2 = (String) MUX.mux4in(ReadData2, EX_MEM.ALUResult(),0/*TODO: MEM/WB value*/,
                    ReadData2,ForwardB.charAt(0)+"",ForwardB.charAt(1)+"");
            int operand2 = Integer.parseInt(mux2,2);

            int operand2ALT = operations.Complement(Immediate);

            //choose between operand2 and operand2ALT according to the ALUSrc signal
            operand2 = (int) MUX.mux2in(operand2,operand2ALT,ALUSrc.charAt(0) - '0');

        //====================get the operation code==========================
            String ALUCode = ALUControl.ALUSignals(funct,ALUop);

        //=======================perform the operation========================
            int ALUresult = ALU.ALUEvaluator(ALUCode,operand1,operand2);

            //result = 0 ?
            String ZFlag = ALUresult == 0? "1":"0";

        //================pass the outputs to the next stage==================
        EX_MEM.write(Integer.toBinaryString(ALUresult), ReadData2,ZFlag,input.get("rd"),input);

        //=====================print the required output======================
        printStage(ZFlag,input.get("BranchAddress"),String.format("%32s", Integer.toBinaryString(ALUresult))
                        .replace(' ', '0'), ReadData2
                ,input.get("rt"),ID_EX.MEM_Control(),ID_EX.WB_Control());
    }

    public static void printStage(String zflag,String branchAddress,String ALUResult,String data2,String rt,
                                  HashMap<String,String> memcontrol, HashMap<String,String> wbcontrol){
        if (!formatter.checknop(formatter.AssemblyStages[2])) {
            StringBuilder out = new StringBuilder();
            out.append("\t").append(formatter.AssemblyStages[2])
                    .append(" in Excecute stage: \n\n\t\t")
                    .append("zero flag: ").append(zflag)
                    .append("\n\t\t").append("branch address: ").append(formatter.formatOut(branchAddress))
                    .append("\n\t\t").append("ALU result/address: ").append(formatter.formatOut(ALUResult))
                    .append("\n\t\t").append("register value to write to memory: ").append(formatter.formatOut(data2))
                    .append("\n\t\t").append("rt/rd register: ").append(rt)
                    .append("\n\t\t").append("WB controls: ").append("MemToReg: ").append(wbcontrol.get("MemToReg"))
                    .append(", ").append("RegWrite: ").append(wbcontrol.get("RegWrite"))
                    .append("\n\t\t").append("MEM controls: ").append("MemRead: ").append(memcontrol.get("MemRead"))
                    .append(", ").append("MemWrite: ").append(memcontrol.get("MemWrite"))
                    .append(", ").append("Branch: ").append(memcontrol.get("Branch")).append("\n\n");

            System.out.print(out.toString());
        }
    }
}
