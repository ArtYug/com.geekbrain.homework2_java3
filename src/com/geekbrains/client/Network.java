package com.geekbrains.client;

import com.geekbrains.CommonConstants;
import com.geekbrains.server.ServerCommandConstants;

import java.io.*;
import java.net.Socket;
                    /*  Homework 3 java 3  class changes in Network and ClientHandler
       1. Добавить в сетевой чат запись локальной истории в текстовый файл на клиенте. Для каждой
        учетной записи файл с историей должен называться history_[login].txt. (Например,
        history_login1.txt, history_user111.txt)
        2. ** После загрузки клиента показывать ему последние 100 строк истории чата.*/

public class Network {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final ChatController controller;
    private static final File file = new File("history.txt");

    public Network(ChatController chatController) {
        this.controller = chatController;
    }

    public void startReadServerMessages() throws IOException {
        new Thread(() -> {
            try {
                while (true) {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(messageFromServer);
                    if (messageFromServer.startsWith(ServerCommandConstants.ENTER)) {
                        String[] client = messageFromServer.split(" ");
                        controller.displayMessage(client[1] + " " + "зашел в чат");
                        controller.displayClient(client[1]);
                    } else if (messageFromServer.startsWith(ServerCommandConstants.EXIT)) {
                        String[] client = messageFromServer.split(" ");
                        controller.removeClient(client[1]);
                        controller.displayMessage(client[1] + " покинул чат");
                    } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENTS)) {
                        String[] client = messageFromServer.split(" ");
                        for (int i = 1; i < client.length; i++) {
                            controller.displayClient(client[i]);
                        }
                    } else {
                        controller.displayMessage(messageFromServer);
                        writeFile(messageFromServer);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS, CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void writeFile(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("history_[login].txt" + this.controller.toString(), true))) {
            if (message != null) {
                bufferedWriter.write(message + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendAuth(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                initializeNetwork();
            }
            outputStream.writeUTF(ServerCommandConstants.AUTHENTICATION + " " + login + " " + password);
            boolean authenticated = inputStream.readBoolean();
            if (authenticated) {
                startReadServerMessages();
                if (file.exists()) {
                    controller.displayMessage(readLastHundredMessagesFromFile(file, 100));
                }
            }
            return authenticated;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String readLastHundredMessagesFromFile(File file, int lines) {
        int readLines = 0;
        StringBuilder builder = new StringBuilder();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long fileLength = file.length() - 1;
            randomAccessFile.seek(fileLength);
            for (long pointer = fileLength; pointer >= 0; pointer--) {
                randomAccessFile.seek(pointer);
                char c;
                c = (char) randomAccessFile.read();
                if (c == '\n') {
                    readLines++;
                    if (readLines == lines)
                        break;
                }
                builder.append(c);
            }
            builder.reverse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void closeConnection() {
        try {
            outputStream.writeUTF(ServerCommandConstants.EXIT);
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.exit(1);
    }
}
