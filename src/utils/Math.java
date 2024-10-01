package utils;


public class Math {

    /**
     * @param a value to raise to the power
     * @param b value of the power
     * @return Returns the value of the first argument raised to the power of the second argument.
     * Warning : NO SPECIAL CASE
     */
    public static int powInt(int a, int b) {
        int res = 1;
        for (int i=0; i<b; i++) {
            res *=a;
        }
        return res;
    }

    
    /**
     * Return next int
     * @param a
     * @param dir direction to the next integer to return
     * @return
     */
    public static double nextInt(double a, double dir) {
        if (dir>0) {
            return java.lang.Math.ceil(a);
        } else {
            return java.lang.Math.floor(a);
        }
    }
}
