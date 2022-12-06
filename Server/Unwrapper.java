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
        this.firstByte = ByteBuffer.allocate(1).putInt(buffer.read()).get(0); //gets byte at 0 of returned int (4 bytes)
        this.secondByte = ByteBuffer.allocate(1).putInt(buffer.read()).get(0);
        this.length = getLength(buffer);
        this.payload = getPayload(buffer, length);
    }

    private int getLength(BufferedInputStream buffer) throws IOException
    {
        byte[] array = new byte[4];
        for(int i=0; i<4; i++)
        {
            array[i] = ByteBuffer.allocate(1).putInt(buffer.read()).get(0);
        }
        return ByteBuffer.wrap(array).getInt();
    }

    private byte[] getPayload(BufferedInputStream buffer, int length) throws IOException
    {
        byte[] array = new byte[length - 1];
        for(int i=0; i<length; i++)
        {
            array[i] = ByteBuffer.allocate(1).putInt(buffer.read()).get(0);
        }
        return array;
    }



}