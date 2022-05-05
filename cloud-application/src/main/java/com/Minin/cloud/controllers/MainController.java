package com.Minin.cloud.controllers;

import com.Minin.cloud.model.AbstractMessage;
import com.Minin.cloud.model.FileMessage;
import com.Minin.cloud.model.ListMessage;
import com.Minin.cloud.network.Net;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.File;
import java.nio.file.Path;


import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Path serverDir = Path.of("ServerFiles");

    private Path clientDir;

    public ListView<String> clientView;

    public ListView<String> serverView;

    private Net net;

    private void read() {
        try {
            while (true) {
                AbstractMessage message = net.read();
                if (message instanceof ListMessage listMessage) {
                    serverView.getItems().clear();
                    serverView.getItems().addAll(listMessage.getFiles());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getClientFiles() throws IOException {
        return Files.list(clientDir)
                .map(Path::getFileName)
                .map(Path::toString)
                .toList();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientDir = Path.of("UserFiles");
            clientView.getItems().clear();
            clientView.getItems().addAll(getClientFiles());
            net = new Net("localhost", 8189);
            Thread.sleep(300);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem();
        net.write(new FileMessage(clientDir.resolve(fileName)));
    }

    public void download(ActionEvent actionEvent) throws IOException {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        Path path = clientDir.resolve(fileName);
        Files.write(path, net.getBytes(new FileMessage(serverDir.resolve(fileName))));
        clientView.getItems().clear();
        clientView.getItems().addAll(getClientFiles());
    }
}
