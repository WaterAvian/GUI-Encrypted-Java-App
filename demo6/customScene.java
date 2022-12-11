package com.example.demo6;

import com.example.demo6.Client.Client;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class customScene extends Stage {

    Socket socket;
    Client client;


    public void create() throws IOException {
        this.socket = new Socket("localhost", 1121);
        this.client = new Client(socket, 1L);
        client.listenForMessage();
    }
}
