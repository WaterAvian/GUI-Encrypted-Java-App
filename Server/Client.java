import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.io.*;
import java.nio.charset.Charset;

public class Client {

    private Socket socket;
    private BufferedInputStream bufferedInput;
    private BufferedOutputStream bufferedOutput;

    private Long myGivenID;
    private Long recieverID; 


    public Client(Socket socket, long ID)
    {
        try {
            this.socket = socket;
            this.bufferedOutput = new BufferedOutputStream(socket.getOutputStream()); //new OutputStreamWriter(X); new OutputStream()
            this.bufferedInput = new BufferedInputStream(socket.getInputStream()); //new InputStream()
            this.myGivenID = ID;

        } catch (IOException e) {
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public void sendMessage(){
        try {
            byte[] bufferForHash = "BufferHashCoffeeCoffeeBeans[null][\\0]".getBytes(); 
            Long serverID = (long)0;
            NetworkMessage message =  new NetworkMessage(serverID, myGivenID, bufferForHash); 
            bufferedOutput.write(message.sendOut()); 
            bufferedOutput.flush();

            Scanner scan = new Scanner(System.in);
            System.out.println("Recipient ID?: ");
            recieverID = scan.nextLong();
            while (socket.isConnected())
            {
                String messageToSend = scan.nextLine();
                byte[] messageToBytes = messageToSend.getBytes(Charset.forName("UTF-8"));
                NetworkMessage newMsg =  new NetworkMessage(recieverID, myGivenID, messageToBytes);
                bufferedOutput.write(newMsg.sendOut());
                bufferedOutput.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                byte[] recievedDecoded; //recievedMessage
                

                while (socket.isConnected()){
                    try {
                        byte[] input = bufferedInput.read(); //RETURNS INT -> NEEDS TO BE CONVERTED TO BYTE
                        NetworkMessage recievedMessage = new NetworkMessage(input);
                        recievedDecoded = recievedMessage.message;
                        String resultAfterDecode = new String(recievedDecoded, "UTF-8");
                        System.out.println(resultAfterDecode);

                    } catch (IOException e) {
                        closeEverything(socket, bufferedInput, bufferedOutput);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedInputStream bufferedInput, BufferedOutputStream bufferedOutput) {
        try {
            if(bufferedInput != null)
            {
                bufferedInput.close();
            }
            if(bufferedOutput != null)
            {
                bufferedInput.close();
            }
            if(socket != null)
            {
                bufferedInput.close();
            }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void recordChat()
    {
        MessageHistory history = new MessageHistory();
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your ID: ");
        Long clientID = scan.nextLong();
        System.out.println("Initializing please wait");

        try{
            Socket socket = new Socket("localhost", 1112); 
            Client client = new Client(socket, clientID);
        
            client.listenForMessage();
            client.sendMessage();
        } catch(IOException e){
            e.printStackTrace();
        }
    
        System.out.println("Done. Chat room secured.");
        //System.exit(0); ERROR: JDWP Unable to get JNI 1.2 environment, jvm->GetEnv() return code = -2
    }
}


