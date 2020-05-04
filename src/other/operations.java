package other;

public class operations {

    //gets the 2's complement of a binary string
    public static int Complement(String Bin){
        StringBuilder res = new StringBuilder();

        if (Bin.charAt(0) == '1'){
            boolean flip = false;

            for (int i = Bin.length() - 1; i >= 0; i--) {
                if (flip){
                    if (Bin.charAt(i) == '0') res.append(1);
                    else res.append(0);
                }
                else {
                    res.append(Bin.charAt(i));
                    if (Bin.charAt(i) == '1') flip = true;
                }
            }

        }
        else return Integer.parseInt(Bin,2);

        return Integer.parseInt(res.reverse().toString(),2) * -1;
    }

}
