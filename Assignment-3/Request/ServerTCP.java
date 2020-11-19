import java.io.*; // for Input/OutputStream
import java.net.*; // for Socket and ServerSocket

public class ServerTCP {

  public static void main(String args[]) throws Exception {
    
    if (args.length != 1) // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int port = Integer.parseInt(args[0]); // Receiving Port

    ServerSocket servSock = new ServerSocket(port);
    for (;;) {

      Socket clntSock = servSock.accept();
      // Receive binary-encoded friend
      RequestDecoder decoder = new RequestDecoderBin();
      Request receivedRequest = decoder.decode(clntSock.getInputStream());

      System.out.println("Received Binary-Encoded Request with RID " + receivedRequest.RID + "\n");
      System.out.println("Decoded Request: \n");
      System.out.println(receivedRequest);

      short RID = receivedRequest.RID;
      byte error = 0;

      if (receivedRequest.length != 9) {
        error = 127;
      }
        
      if (!verifyChecksum(receivedRequest)) {
         error = 63;
      }

      int answer = calculator(receivedRequest);

      byte[] prechecksum2 = {9, (byte)RID, (byte)error, (byte)answer};
      
      byte newChecksum = checksum(prechecksum2);
      Response response = new Response(RID, error, answer, newChecksum);
      
      System.out.println("\nP(x) = " + answer);

      ResponseEncoder encoder = new ResponseEncoderBin();
      
      System.out.println("\nSending Response\n");
      OutputStream out = clntSock.getOutputStream();
      
      out.write(encoder.encode(response));
    }

  }

  public static byte checksum(byte[] message) {
    short checksum = 0x0;
    for (byte b : message) {
        checksum += b;
        if (checksum > 0xFF) {
            checksum -= 0x100;
            checksum += 0x1;
        }
    }

    return (byte)~checksum;
}

  public static boolean verifyChecksum(Request receivedRequest) {
    byte l = receivedRequest.length;
    short RID = receivedRequest.RID;
    int x = receivedRequest.x;
    int a3 = receivedRequest.a3;
    int a2 = receivedRequest.a2;
    int a1 = receivedRequest.a1;
    int a0 = receivedRequest.a0;
    byte[] prechecksum = {l, (byte)RID, (byte)x, (byte)a3, (byte)a2, (byte)a1, (byte)a0};

    byte checksum = checksum(prechecksum);
    if (receivedRequest.checksum != checksum) {
      return false;
    }
    else {
      return true;
    }
  }

  public static int calculator(Request request) {
    int x = request.x;
    int a3 = request.a3;
    int a2 = request.a2;
    int a1 = request.a1;
    int a0 = request.a0;

    int result = 0;
    result += a3 * Math.pow(x, 3);
    result += a2 * Math.pow(x, 2);
    result += a1 * x;
    result += a0;


    return result;

}
}