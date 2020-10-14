public class Request {

    public int x;
    public int a3;
    public int a2;
    public int a1;
    public int a0;

    public Request(int x, int a3, int a2, int a1, int a0) {
        this.x = x;
        this.a3 = a3;
        this.a2 = a2;
        this.a1 = a1;
        this.a0 = a0;
    }

    public String toString() {
        final String EOLN = java.lang.System.getProperty("line.separator");
        String value = "";
        if (a3 > 0) {
            value += a3 + "x^3";
        }
        if (a2 > 0) {
            value += " + " + a2 + "x^2";
        }
        if (a1 > 0) {
            value += " + " + a1 + "x";
        }
        if (a0 > 0) {
            value += " + " + a0 + EOLN;
        }
        value += "Solving for x = " + x;
        return value;
    }
}
