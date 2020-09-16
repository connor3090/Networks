import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class myFirstUDPServer {

  private static final int ECHOMAX = 255;  // Maximum size of echo datagram

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct argument list
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    DatagramSocket socket = new DatagramSocket(servPort);
    DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

    for (;;) {  // Run forever, receiving and echoing datagrams
      socket.receive(packet);     // Receive packet from client
      System.out.println("Handling client at " +
        packet.getAddress().getHostAddress() + " on port " + packet.getPort());

      String message = new String(packet.getData());
      String devowelizedMessage = devowelize(message);
      byte[] outBytes = devowelizedMessage.getBytes();
      packet.setData(outBytes); //put devowelized message in packet

      System.out.println("Devowelized Message: " + devowelizedMessage);

      socket.send(packet);       // Send the same packet back to client
      //packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }

  public static boolean isVowel(char letter) {

    if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') {
      return true;
    } else {
      return false;
    }
  }

  public static String devowelize(String message) {
    String out = "";
    for (int i = 0; i < message.length(); i++) {

      if (!isVowel(message.charAt(i))) {
        out += message.charAt(i);
      }

    }
    return out;
  }
}
