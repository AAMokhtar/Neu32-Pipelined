package stages;

import components.*;
import components.pipelineRegs.ID_EX;
import components.pipelineRegs.IF_ID;
import other.DatapathException;
import other.formatter;
import other.operations;

import java.util.HashMap;
import java.util.Hashtable;

public class Decode {

    public static void run() throws DatapathException {
        //get all inputs from IF_ID
        HashMap<String, String> input = IF_ID.read();
        String instruction = input.get("Instruction");
        int incrementedPC = Integer.parseInt(input.get("PC"), 2);

        //divide the instruction
        String opCode = instruction.substring(0,4);

        String rs = instruction.substring(4,9);
        String rt = instruction.substring(9,14);
        String rd = instruction.substring(14,19);

        String immediate = instruction.substring(14);
        String target = instruction.substring(4);

        String funct = instruction.substring(19);

        //control signals are NOP initially.
        Hashtable<String, String> control = new Hashtable<>();
        control.put("Branch","0");
        control.put("Jump","0");
        control.put("DstReg","0");
        control.put("ALUSrc","0");
        control.put("ALUop","000");
        control.put("MemRead","0");
        control.put("MemWrite","0");
        control.put("RegWrite","0");
        control.put("MemToReg","0");

        //we set the controls according to the hazard detection unit's NOP signal.
            control = (Hashtable<String, String>) MUX.mux2in(MainControl.controlSignals(opCode),
                       control,0/*TODO: HazardDetectionUnit.NOP */);

        //get the correct destination register
        if (control.get("DstReg").equals("0"))
            rd = rt;

        //read the source registers
        String[] values = {RegisterFile.readdata(rs),
                         RegisterFile.readdata(rt)};

        //extend the immediate
        int opInt = Integer.parseInt(opCode,2);
        immediate = Signextend.signeextend(immediate);

        if (operations.Complement(immediate) > 32) immediate = "00000000000000000000000000100000";

        //calculate branch address
        int branchAddress = incrementedPC + (operations.Complement(immediate) << 2);

        /*
         * calculate jump address:
         * shift the 28 bit target left by 2 bits and get the
         * remaining 2 bits from the incremented PC's most left bits
         */
        int jumpAddress = Integer.parseInt(input.get("PC").substring(0,3) + target,2);

        //zero flag
        //TODO: Data hazard. get the most recent values of rs,rt.
        String ZFlag = opInt == 7?Comparator.compare("=",values[0],values[1]):"1";
        //greater than flag
        //TODO: Data hazard. get the most recent values of rs,rt.
        String GFlag = opInt == 8?Comparator.compare(">",values[0],values[1]):"0";

        //get branch signals
        String branchSignals = BranchControl.branchSignals(control.get("Jump"),control.get("Branch"),GFlag,ZFlag);

        /*
         * Because the values in a branch comparison are needed during ID but may be produced later in time,
         * it is possible that a data hazard can occur and a stall will be needed. For example, if an ALU
         * instruction immediately preceding a branch produces one of the operands for the comparison in
         * the branch, a stall will be required, since the EX stage for the ALU instruction will occur after
         * the ID cycle of the branch. By extension, if a load is immediately followed by a conditional
         * branch that is on the load result, two stall cycles will be needed, as the result from the load
         * appears at the end of the MEM cycle but is needed at the beginning of ID for the branch.
         */

        //decide branch
        int newPC = (int) MUX.mux4in(incrementedPC,branchAddress,branchAddress,jumpAddress,
                branchSignals.charAt(0)+"",branchSignals.charAt(1)+"");

        if (newPC != incrementedPC){
            PC.setPC(newPC);
            Fetch.Flush = 1;
        }
        else Fetch.Flush = 0;


        /*
         * Any instruction may operate on a single source register, two source
         * registers, one source register and a sign-extended 32-bit immediate value, a sign-extended 32-bit
         * immediate value, or no operands. Additionally, we may need the destination register's number if
         * the instruction requires writing back to a register. We can either prepare only the required
         * operands for the EX stage based on the opcode or prepare all the operands that might be required
         * for any opcode. The latter design is simpler, so let's prepare all of them and store them at fixed
         * locations in the ID/EX register. It simplifies the design.
         */


        ID_EX.write(values[0], values[1], immediate,String.format("%32s", Integer.toBinaryString(branchAddress))
                .replace(' ', '0'), rs, rt, rd, funct, control);

        printStage(values[0],values[1],immediate,input.get("PC"),rt,rd,control);
    }

    public static void printStage(String read1,String read2, String SE, String pc,String rt
    , String rd, Hashtable<String,String> control){
        if (!formatter.checknop(formatter.AssemblyStages[1])) {
            StringBuilder out = new StringBuilder();
            out.append("\t").append(formatter.AssemblyStages[1])
                    .append(" in Decode stage: \n\n\t\t")
                    .append("read data 1: ").append(formatter.formatOut(read1))
                    .append("\n\t\t").append("read data 2: ").append(formatter.formatOut(read2))
                    .append("\n\t\t").append("sign-extend: ").append(formatter.formatOut(SE))
                    .append("\n\t\t").append("Next PC: ").append(formatter.formatOut(pc))
                    .append("\n\t\t").append("rt: ").append(rt)
                    .append("\n\t\t").append("rd: ").append(rd)
                    .append("\n\t\t").append("WB controls: ").append("MemToReg: ").append(control.get("MemToReg"))
                    .append(", ").append("RegWrite: ").append(control.get("RegWrite"))
                    .append("\n\t\t").append("MEM controls: ").append("MemRead: ").append(control.get("MemRead"))
                    .append(", ").append("MemWrite: ").append(control.get("MemWrite"))
                    .append(", ").append("Branch: ").append(control.get("Branch"))
                    .append("\n\t\t").append("EX controls: ").append("RegDest: ").append(control.get("DstReg"))
                    .append(", ").append("ALUOp: ").append(control.get("ALUop")).append(", ")
                    .append("ALUSrc: ").append(control.get("ALUSrc")).append("\n\n");
            System.out.print(out.toString());
        }
    }

}
