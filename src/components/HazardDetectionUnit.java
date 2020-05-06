package components;

import components.pipelineRegs.ID_EX;
import components.pipelineRegs.IF_ID;

public class HazardDetectionUnit {
    public static char PCWrite = '1';
    public static char NOP = '0';
    public static void  setFlags(String rs,String rt){
        if (ID_EX.MEM_Control().get("MemRead").equals("1") && (rs.equals(ID_EX.rd()) || rt.equals(ID_EX.rd()))){
            PCWrite = '0';
            PC.setPC(PC.getPC()-4);

            IF_ID.IF_IDWrite = '0';
            NOP = '1';
        }
        else {
            PCWrite = '1';
            IF_ID.IF_IDWrite = '1';
            NOP = '0';
        }
    }

    /**
     * changes needed in:
     *- PC
     *- IF_ID
     *- Fetch
     *- Decode
     */
}
