public class Response {
    public byte length;
    public short RID;
    public byte errorCode;
    public int result;
    public byte checksum;


    public Response(short RID, byte error, int result, byte checksum) {
        this.length = 9;
        this.RID = RID;
        this.errorCode = error;
        this.result = result;
        this.checksum = checksum;
    }

    public String toString() {
        final String EOLN = java.lang.System.getProperty("line.separator");
        String value = "P(x) = " + result;

        return value;
    }

}
