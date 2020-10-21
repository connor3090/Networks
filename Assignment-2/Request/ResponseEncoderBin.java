
import java.io.*; // for ByteArrayOutputStream and DataOutputStream

public class ResponseEncoderBin implements ResponseBinConst, ResponseEncoder{

    private String encoding;

    public ResponseEncoderBin(){
        this.encoding = DEFAULT_ENCODING;
    }
    public ResponseEncoderBin(String encoding){
        this.encoding = encoding;
    }


    @Override
    public byte[] encode(Response response) throws Exception {

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);

        out.writeByte(9);
        out.writeShort(response.RID);
        out.writeByte(response.errorCode);
        out.writeFloat((float)response.result);
        out.writeByte(response.checksum);
        out.flush();


        return buf.toByteArray();
    }
}
