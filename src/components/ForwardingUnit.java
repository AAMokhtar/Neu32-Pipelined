package components;

public class ForwardingUnit {
    //TODO: MOSSAD
    // ForwardA and ForwardB are needed

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
