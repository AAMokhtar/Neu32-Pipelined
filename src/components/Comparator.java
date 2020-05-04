package components;

import other.DatapathException;

public class Comparator {

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
