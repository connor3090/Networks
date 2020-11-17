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

      System.out.println("Received Binary-Encoded Friend");
      System.out.println(receivedRequest);

      clntSock.getInputStream().close();
      clntSock.close();
    }

  }
}
