package components;

/**
 * equality/sign test unit.
 * used inside the ID stage to decide
 * branches.
 */

public class Comparator {

    /**
     *
     * @param operation =, > (no other operations are supported)
     * @param r1 register value (most likely rs)
     * @param r2 register value (most likely rt)
     * @return comparison operation == true
     */

    public static String compare(String operation, String r1, String r2){
        int op1 = Integer.parseInt(r1,2);
        int op2 = Integer.parseInt(r2,2);

        switch (operation){
            case "=": return (op1 - op2 == 0?"1":"0"); //equality test
            case ">": return (op1 - op2 > 0?"1":"0"); //sign test
            default: return "0";
        }
    }

}
