package other;

import components.VonNeumannMemory;
import stages.Decode;
import stages.Execute;
import stages.Fetch;

/**
 * used for testing the datapath you may delete this when we are done
 */
public class tester {
    public static void main(String[] args) throws DatapathException {

        String[] program = {     "00000000100010000110000000000000", //add 0000-00001-00010-00011-0000000000000
                                 "00000001100100001010000000000001", //sub 0000-00011-00100-00101-0000000000001
                                 "00000010100110001110000000000010", //and 0000-00101-00110-00111-0000000000010
                                 "00000011101000010010000000000011", //mul 0000-00111-01000-01001-0000000000011
                                 "00000100101010010110000000000100", //slt 0000-01001-01010-01011-0000000000100
                                 "00010101101100111111111111111111", //addi 0001-01011-01100-111111111111111111
                                 "00100110001101000000000000000000", //ori 0010-01100-01101-000000000000000000
                                 "00110110101110000000000000000001", //sll 0011-01101-01110-000000000000000001
                                 "01000111001111000000000000000001", //srl 0100-01110-01111-000000000000000001
                                 "01010111110000000000000000000000", //lw 0101-01111-10000-000000000000000000
                                 "01100111001111000000000000000000", //sw 0110-01110-01111-000000000000000000
                                 "01110000010000000000000000000001", //bne 0111-00000-10000-000000000000000001
                                 "10000111100000000000000000000000", //bgt 1000-01111-00000-000000000000000000
                                 "10010000000000000000000000010100"}; //jump 1001-0000000000000000000000010100


        //load the program into memory
            VonNeumannMemory.addinstructions(program);

        //we need 18 cycles to fully execute all of the 14 instructions
        for (int i = 0; i < 18; i++) {
            System.out.print("After clock-cycle: "+(i+1)+":\n\n");
            cycle();
        }
    }

    /**
     * simulates a clock cycle.
     * executes the stages sequentially
     * @throws DatapathException
     */
    public static void cycle() throws DatapathException {
        Fetch.run();
        Decode.run();
        Execute.run();
    }
}
