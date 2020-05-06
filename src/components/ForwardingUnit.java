package components;

import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.MEM_WB;

public class ForwardingUnit {
    public static String ForwardA = "00";
    public static String ForwardB = "00";

    public static void setFlags(String rs, String rt,String regWrite){
        if (regWrite.equals("0")){
            ForwardA = "00";
        }
        else if (rs.equals(EX_MEM.rd())){
            ForwardA = "01";
        }
        else if (rs.equals(MEM_WB.rd())){
            ForwardA = "10";
        }
        //==========================
        if (regWrite.equals("0")){
            ForwardB = "00";
        }
        else if (rt.equals(EX_MEM.rd())){
            ForwardB = "01";
        }
        else if (rt.equals(MEM_WB.rd())){
            ForwardB = "10";
        }

    }

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

}
