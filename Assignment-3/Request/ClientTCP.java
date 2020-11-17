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

      Socket sock = new Socket(destAddr, destPort);
      System.out.println("Display request");
      System.out.println(request); // Display friend just to check what we send

      RequestEncoder encoder = (args.length == 3 ? new RequestEncoderBin(args[2]) : new RequestEncoderBin());

      byte[] codedRequest = encoder.encode(request);
      OutputStream out = sock.getOutputStream();
      out.write(codedRequest);
      sock.close();

      System.out.print("Would you like to send another request? (y/n): ");
      yn = reader.next();

      if (!yn.equals("y")) {
        accept = false;
      }
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
