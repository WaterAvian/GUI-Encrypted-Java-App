import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Unwrapper {
    
    public byte firstByte;
    public byte secondByte;
    public int length;
    public byte[] payload;
    
    Unwrapper(BufferedInputStream buffer) throws IOException
    {
        this.firstByte = (byte)buffer.read();//ByteBuffer.allocate(4).put((byte)buffer.read()).get(0);  System.out.println("first"+firstByte); //gets byte at 0 of returned int (4 bytes)
        this.secondByte = (byte)buffer.read(); //I am going to suggest we perahaps simplify to this? no need to create a buffer here I don't think, we can read bytes directly from the stream by casting the ints of read(){which reads only one byte} back to byte
        this.length = getLength(buffer); System.out.println("lenth"+length);
        payload = new byte[length];
        buffer.readNBytes(payload, 0, length);
    }

    private int getLength(BufferedInputStream buffer) throws IOException
    {
        byte[] array = new byte[4];
        for(int i=0; i<4; i++)
        {
            array[i] = (byte)buffer.read(); //I am pretty sure this is the business
        } 

        int test = ByteBuffer.wrap(array).getInt();
        System.out.println(test);
        return test;
    }

    private byte[] getPayload(BufferedInputStream buffer, int length) throws IOException
    {
        byte[] array = new byte[length];
        for(int i=0; i<length; i++)
        {
            array[i] = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);
        }
        return array;
    }



}