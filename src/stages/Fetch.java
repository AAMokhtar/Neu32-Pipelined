package stages;

import components.Cache;
import components.PC;
import components.pipelineRegs.IF_ID;
import other.formatter;

public class Fetch {
    /**
     * loads the instruction from the program counter.
     * instruction is set to 0 if we are flushing the pipeline.
     */
    public static char Flush = '0';

    public static void run(){
         String.format("%032d", 0); //NOP add, $0, $0, $0

        /**
         *  we assume that the branch is not taken. if that's incorrect, flush
         *  the current instruction (the one in IF).
         */
        String instruction = instruction = Cache.load(PC.get32bitPC());

        //PC + 4
        incPC();

        IF_ID.write(instruction,PC.getPC());

        printStage(instruction,PC.get32bitPC()); //printing
    }

    public static void incPC(){
        PC.setPC(PC.getPC() + 4);
    }

    private static void printStage(String instruction,String pc){
        if (!formatter.checknop(formatter.AssemblyStages[0])) {
            StringBuilder out = new StringBuilder();
            out.append("\t").append(formatter.AssemblyStages[0])
                    .append(" in Fetch stage: \n\n\t\t")
                    .append("Next PC: ").append(formatter.formatOut(pc))
                    .append("\n\t\t").append("Instruction: ")
                    .append(formatter.formatOut(instruction)).append("\n\n");

            System.out.print(out.toString());
        }
    }
}
