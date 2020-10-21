public class Response {
    public byte length;
    public short RID;
    public int errorCode;
    public double result;
    public int checksum;


    public Response(short RID, int error, double result, int checksum) {
        this.length = 9;
        this.RID = RID;
        this.errorCode = error;
        this.result = result;
        this.checksum = checksum;
    }

    public String toString() {
        final String EOLN = java.lang.System.getProperty("line.separator");
        String value = "";


        return value;
    }

}
