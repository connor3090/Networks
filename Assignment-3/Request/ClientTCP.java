import java.io.*; // for Input/OutputStream
import java.net.*; // for Socket
import java.util.Scanner;
import java.util.Random;

public class ClientTCP {

  public static void main(String args[]) throws Exception {

    if (args.length != 2) // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Destination> <Port>");

    InetAddress destAddr = InetAddress.getByName(args[0]); // Destination address
    int destPort = Integer.parseInt(args[1]); // Destination port
    boolean accept = true;
    Scanner reader = new Scanner(System.in);
    Random rand = new Random();
    short RID = (short) rand.nextInt(Short.MAX_VALUE + 1);

    int x;
    int a0;
    int a1;
    int a2;
    int a3;
    String yn;


    while (accept) {
      RID++;
      System.out.print("Enter a value x to solve for: ");
      x = reader.nextInt();

      System.out.print("\nEnter a value for a0: ");
      a0 = reader.nextInt();

      System.out.print("\nEnter a value for a1: ");
      a1 = reader.nextInt();

      System.out.print("\nEnter a value for a2: ");
      a2 = reader.nextInt();

      System.out.print("\nEnter a value for a3: ");
      a3 = reader.nextInt();

      byte[] preChecksum = { 9, (byte) RID, (byte) x, (byte) a3, (byte) a2, (byte) a1, (byte) a0 };
      byte checksum = checksum(preChecksum);
      Request request = new Request(RID, x, a3, a2, a1, a0, checksum);

      RequestEncoder encoder = (args.length == 3 ? new RequestEncoderBin(args[2]) : new RequestEncoderBin());

      byte[] codedRequest = encoder.encode(request);

      Socket sock = new Socket(destAddr, destPort);

      OutputStream out = sock.getOutputStream();
      InputStream in = sock.getInputStream();
      long startTime = System.currentTimeMillis();
      out.write(codedRequest);
      
      ResponseDecoder decoder = (args.length == 3 ? new ResponseDecoderBin(args[2]) : new ResponseDecoderBin());
      Response decodedResponse = decoder.decode(in);
      long endTime = System.currentTimeMillis();
      long elapsedTime = endTime - startTime;
      
      StringBuilder sb = new StringBuilder();
      for (byte b : codedRequest) {
        sb.append(String.format("%02X ", b));
      }

      System.out.println("\n\nEncoded sent message in hexadecimal: \n" + sb.toString() + "\n\n");
      System.out.println("Sent Message: ");
      System.out.println(request.toString());

      System.out.println("\nResult: P(x) = " + decodedResponse.result);

      System.out.println("Round trip time: " + elapsedTime + "ms\n");

      System.out.print("Would you like to send another request? (y/n): ");
      yn = reader.next();

      if (!yn.equals("y")) {
        accept = false;
      }
      sock.close();
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

    return (byte) ~checksum;
  }
}
