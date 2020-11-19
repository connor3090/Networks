import java.io.*; // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class ResponseDecoderBin implements ResponseDecoder, ResponseBinConst {

    private String encoding; // Character encoding

    public ResponseDecoderBin() {
        encoding = DEFAULT_ENCODING;
    }

    public ResponseDecoderBin(String encoding) {
        this.encoding = encoding;
    }

    public Response decode(InputStream wire) throws IOException {
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

        // return new Response(ID, lastname, streetnumber, zipcode, ((flags &
        // SINGLE_FLAG) == SINGLE_FLAG),
        // ((flags & RICH_FLAG) == RICH_FLAG), ((flags & FEMALE_FLAG) == FEMALE_FLAG));

        DataInputStream src = new DataInputStream(wire);
        byte TML = src.readByte();
        short RID = src.readShort();
        byte error = src.readByte();
        int result = src.readInt();
        byte checksum = src.readByte();
        return new Response(RID, error, result, checksum);
    }

    public Response decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }
}