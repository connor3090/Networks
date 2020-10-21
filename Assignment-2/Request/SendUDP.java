import java.net.*; // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*; // for IOException
import java.util.Scanner;
import java.util.Random;

public class SendUDP {

  public static void main(String args[]) throws Exception {

    if (args.length != 2 && args.length != 3) // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Destination>" + " <Port> [<encoding]");

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
      
      byte[] preChecksum = {(byte)RID, x, a3, a2, a1, a0);
      byte checksum = checksum(preChecksum);
      
      Request request = new Request(RID, x, a3, a2, a1, a0);

      DatagramSocket sock = new DatagramSocket(); // UDP socket for sending

      // Use the encoding scheme given on the command line (args[2])
      RequestEncoder encoder = (args.length == 3 ? new RequestEncoderBin(args[2]) : new RequestEncoderBin());

      byte[] codedRequest = encoder.encode(request); // Encode friend

      DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length, destAddr, destPort);
      sock.send(message);

      sock.close();

      System.out.print("Would you like to send another request? (yes/no): ");
      yn = reader.next();
      System.out.println(yn);
      if (!yn.equals("yes")) {
        accept = false;
      }
      RID++;
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
}
