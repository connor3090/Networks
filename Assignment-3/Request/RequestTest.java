import java.net.*; // for DatagramSocket and DatagramPacket
public class RequestTest {
    public static void main(String[] args) throws Exception {
        ShouldTestDecode();
    }

    private static void ShouldTestToString() {
        short ID = 200;
        Request r = new Request(ID, 1, 2, 3, 4, 5, (byte)1);
        System.out.println(r.toString());
    }

    private static void ShouldTestEncode() throws Exception {
        short ID = 128;
        Request r = new Request(ID, 1, 2, 3, 4, 5, (byte)1);
        RequestEncoder encoder = new RequestEncoderBin();
        byte[] codedRequest = encoder.encode(r);
        int TML = codedRequest[0];
        short RID = codedRequest[2];
        System.out.println(TML);
    }

    private static void ShouldTestDecode() throws Exception {
        short ID = 128;
        Request r = new Request(ID, 1, 2, 3, 4, 5, (byte)1);
        RequestEncoder encoder = new RequestEncoderBin();
        byte[] codedRequest = encoder.encode(r);
        DatagramPacket packet = new DatagramPacket(codedRequest, codedRequest.length);
        RequestDecoder decoder = new RequestDecoderBin();
        Request decodedRequest = decoder.decode(packet);
        System.out.println(decodedRequest.toString());
    }
}
