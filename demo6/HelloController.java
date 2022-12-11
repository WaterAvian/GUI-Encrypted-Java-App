package com.example.demo6;

import com.example.demo6.Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.Socket;

public class HelloController {

    Long destinationID = Long.valueOf(2);


    @FXML private TextField chooseID;

    @FXML private TextField enterDestination;

    @FXML
    private Pane convoPane;

    @FXML
    private Label convobar;

    @FXML
    private TextField messageInput;

    @FXML
    private TextArea textArea;


    public void receiveMessage(Long ID, String message){
        textArea.appendText(ID + ": " + message + "\n");
    }

    public void addInformation(String information){
        textArea.appendText(information + "\n");
    }


    @FXML
    void broadcast(ActionEvent event) {
        HelloApplication.client.send(destinationID, "please work i am miserable");
        textArea.appendText(HelloApplication.client.getMyGivenID() + ": " + messageInput.getText() + "\n");

    }

    @FXML
    void choose(ActionEvent event){
        HelloApplication.client.setMyGivenID(Long.valueOf(chooseID.getText()));
        System.out.println("Set client ID to " + HelloApplication.client.getMyGivenID());
    }

    @FXML
    void enterDest(ActionEvent event){
        destinationID = Long.valueOf(enterDestination.getText());
        System.out.println("Set destination ID to " + destinationID);
    }
}
