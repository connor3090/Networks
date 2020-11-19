import java.io.*; // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class RequestDecoderBin implements RequestDecoder, RequestBinConst {

  private String encoding; // Character encoding

  public RequestDecoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public RequestDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Request decode(InputStream wire) throws IOException {
    // boolean single, rich, female;
    // DataInputStream src = new DataInputStream(wire);
    // long ID = src.readLong();
    // short streetnumber = src.readShort();
    // int zipcode = src.readInt();
    // byte flags = src.readByte();

    // // Deal with the lastname
    // int stringLength = src.read(); // Returns an unsigned byte as an int
    // if (stringLength == -1)
    // throw new EOFException();
    // byte[] stringBuf = new byte[stringLength];
    // src.readFully(stringBuf);
    // String lastname = new String(stringBuf, encoding);

    // return new Request(ID, lastname, streetnumber, zipcode, ((flags &
    // SINGLE_FLAG) == SINGLE_FLAG),
    // ((flags & RICH_FLAG) == RICH_FLAG), ((flags & FEMALE_FLAG) == FEMALE_FLAG));

    DataInputStream src = new DataInputStream(wire);
    byte tmlByte = src.readByte();
    int TML = tmlByte;
    System.out.println("TML: " + String.format("0x%02X", TML));

    short RID = src.readShort();
    System.out.println("RID: " + String.format("0x%04X", RID));

    int x = src.readByte();
    System.out.println("x: " + String.format("0x%02X", x));

    int a3 = src.readByte();
    System.out.println("a3: " + String.format("0x%02X", a3));

    int a2 = src.readByte();
    System.out.println("a2: " + String.format("0x%02X", a2));

    int a1 = src.readByte();
    System.out.println("a1: " + String.format("0x%02X", a1));

    int a0 = src.readByte();
    System.out.println("a0: " + String.format("0x%02X", a0));

    byte checksum = src.readByte();
    System.out.println("Checksum: " + String.format("0x%02X", checksum));

    return new Request(RID, x, a3, a2, a1, a0, checksum);
  }

  public Request decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload);
  }
}
