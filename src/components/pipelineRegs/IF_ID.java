package components.pipelineRegs;

import other.formatter;

import java.util.HashMap;

public class IF_ID {
    /**
     * When IF/DWrite is deasserted (=0), the IF/ID register is not written to, making it deliver the
     * same instruction to the IF stage over and over again. This effectively freezes the instruction
     * that's currently in the ID stage.
     */
    private static char IF_IDWrite;

    /**
     * because we are not executing all the stages concurrently, stages will have to be executed
     * sequentially starting with IF. Completed stages will need a place to store their outputs
     * in without affecting the upcoming stages in the same cycle. A solution to this problem
     * is to store upcoming/outgoing values for the register. Each time we store something in
     * the register, the current incoming values will become the outgoing. Then we are going
     * to store the new values as incoming values.
     */
    private static HashMap<String,String> incoming;
    private static HashMap<String,String> outgoing;

    /**
     * to avoid copying values twice each time we write to the register (incoming -> outgoing, new values -> incoming),
     * we can make them alternate positions on every read. that way we only copy the values once (new values -> incoming).
     *
     * false = write to incoming, read from outgoing
     * true = write to outgoing, read from incoming
     */
    private static boolean reverse; //bit

    static {
        IF_IDWrite = '1';
        incoming = new HashMap<>();
        outgoing = new HashMap<>();

        incoming.put("Instruction", String.format("%032d", 0));
        outgoing.put("Instruction", String.format("%032d", 0));

        incoming.put("PC", String.format("%032d", 0));
        outgoing.put("PC", String.format("%032d", 0));
    }

    //ANY READ OTHER THAN ID
    public static String Instruction(){
        if (reverse) return incoming.get("Instruction");
        return outgoing.get("Instruction");
    }
    //ANY READ OTHER THAN ID
    public static String PC(){
        if (reverse) return incoming.get("PC");
        return outgoing.get("PC");
    }

    //read = get the previous cycle's values
    public static HashMap<String, String> read(){
        char prev_IF_IDWrite = IF_IDWrite;

        //reset the signal for the next instruction
        IF_IDWrite = '1';

        if (reverse){
           if (prev_IF_IDWrite == '1') reverse = false; //if we don't write, we don't reverse
            return incoming;
        }

        if (prev_IF_IDWrite == '1') reverse = true;
        return outgoing;
    }

    //write = store the output of IF in incoming (outgoing if the order is reversed)
    public static void write(String inst,int PC){
        //stall  if IF_IDWrite == '0'
        if (IF_IDWrite == '1') {
            if (reverse) {
                outgoing.put("Instruction", inst);
                outgoing.put("PC", String.format("%32s",
                        Integer.toBinaryString(PC)).replace(' ', '0'));
            }
            else{
                incoming.put("Instruction", inst);
                incoming.put("PC", String.format("%32s",
                        Integer.toBinaryString(PC)).replace(' ', '0'));
            }
        }
        formatter.advance(inst); //for printing
    }

    public static void flushOutgoing(){
        if (reverse) incoming.put("Instruction",String.format("%032d", 0)); //NOP add, $0, $0, $0
        else outgoing.put("Instruction",String.format("%032d", 0));

        formatter.AssemblyStages[1] = "addi $0, $0, $0 (NOP)";
    }


}
