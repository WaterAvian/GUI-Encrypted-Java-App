import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedInputStream bufferedInput;
    private BufferedOutputStream bufferedOutput; 
    private Long clientID;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedOutput = new BufferedOutputStream(socket.getOutputStream()); //new OutputStreamWriter(X); 
            this.bufferedInput = new BufferedInputStream(socket.getInputStream());
            this.clientID = getUsername(); //updated
            clientHandlers.add(this);
            //REMOVED: sendRecievedMessage("SERVER: " + clientID + "has entered the chat!")
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("UHHHHHHHHHH");
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public Long getUsername() throws IOException //updated
    {
        byte[] payload = new Unwrapper(bufferedInput).payload;
        NetworkMessage message = new NetworkMessage(payload);
        System.out.println(message.senderID);
        System.out.println(message.receiverID);
        System.out.println(message.message.toString());
        Long retClientID = message.senderID;
        System.out.println(payload.length);
        for(int i = 0; i<payload.length;i++){
            System.out.printf("%x ",payload[i]);
        }
//        Long clientID = new NetworkMessage(new Unwrapper(bufferedInput).payload).senderID; //at ClientHandler.getUsername(ClientHandler.java:28)
        System.out.println("\nretClientID: "+retClientID);
        return retClientID;
    }

    @Override
    public void run(){ 

        while (socket.isConnected())
        {
            try {
                NetworkMessage messageFromClient = new NetworkMessage(new Unwrapper(bufferedInput).payload); 
                if(!messageFromClient.failed){
                    sendRecievedMessage(messageFromClient);
                }
            } catch(IOException e) {

                //THIS NO LONGER CLOSES EVERYTHING, stopped crashing

                closeEverything(socket, bufferedInput, bufferedOutput);
                e.printStackTrace();
            }

            //added this to reset buffers if we have an IO issue
            if(this.bufferedOutput == null){
            try{
            this.bufferedOutput = new BufferedOutputStream(socket.getOutputStream()); //new OutputStreamWriter(X); 
            this.bufferedInput = new BufferedInputStream(socket.getInputStream());
            }
            catch(Exception e){}
            }
        }
    }

    public void sendRecievedMessage(NetworkMessage messageToSend){


        for(ClientHandler clientHandler : clientHandlers)
        {
            try {
                if (clientHandler.clientID == messageToSend.receiverID)
                {

                    clientHandler.bufferedOutput.write(messageToSend.sendOut()); //send message method
                    clientHandler.bufferedOutput.flush();
                    return;
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedInput, bufferedOutput);
                e.printStackTrace();
            }   
        } 
    }

    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        //sendRecievedMessage("User Disconnected.");
    }

    public void closeEverything(Socket socket, BufferedInputStream bufferedInput, BufferedOutputStream bufferedOutput)
    {
        removeClientHandler();
        try {
            if(bufferedInput != null)
            {
                bufferedInput.close();
            }
            if(bufferedOutput != null)
            {
                bufferedInput.close();
            }
           // if(socket != null)
            //{
            //    bufferedInput.close();
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}