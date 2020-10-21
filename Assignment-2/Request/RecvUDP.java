import java.net.*; // for DatagramSocket and DatagramPacket
import java.io.*; // for IOException

public class RecvUDP {

  public static void main(String[] args) throws Exception {

    if (args.length != 1 && args.length != 2) // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");

    int port = Integer.parseInt(args[0]); // Receiving Port

    DatagramSocket sock = new DatagramSocket(port); // UDP socket for receiving
    DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

    for (;;) {
      sock.receive(packet);

      // Receive binary-encoded friend
      RequestDecoder decoder = (args.length == 2 ? // Which encoding
          new RequestDecoderBin(args[1]) : new RequestDecoderBin());

      // RequestDecoder decoder = new RequestDecoderBin();
      Request receivedRequest = decoder.decode(packet);

      System.out.println("Received Binary-Encoded Request");
      System.out.println(receivedRequest);
      
      //checksum checker
      byte l = receivedRequest.length;
      short RID = receivedRequest.RID;
      int x = receivedRequest.x;
      int a3 = receivedRequest.a3;
      int a2 = receivedRequest.a2;
      int a1 = receivedRequest.a1;
      int a0 = receivedRequest.a0;
      byte[] prechecksum = {l, (byte)RID, (byte)a3, (byte)a2, (byte)a1, (byte)a0};

      byte checksum = checksum(prechecksum);



      float answer = calculator(receivedRequest);

      int error = 0;
      if (packet.getLength() != 9) {
          error = 127;
      }
      if (checksum != receivedRequest.checksum) {
          error = 63;
      }
      if (packet.getLength() != 9) {
          error = 127;
      }

      byte[] prechecksum2 = {9, (byte)RID, (byte)error, (byte)answer};
      byte newChecksum = checksum(prechecksum2);
      ResponseEncoder encoder = (args.length == 2 ? new ResponseEncoderBin(args[2]) : new ResponseEncoderBin());
      Response response = new Response(RID, error, answer, newChecksum);
      byte[] codedResponse = encoder.encode(response);

      packet.setData(codedResponse);
      System.out.println(codedResponse);
      sock.send(packet);      
    }

  }
}
