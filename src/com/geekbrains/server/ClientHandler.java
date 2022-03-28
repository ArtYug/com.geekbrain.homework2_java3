package com.geekbrains.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        authentication();
                        readMessages();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException exception) {
            throw new RuntimeException("Проблемы при создании обработчика");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String message = inputStream.readUTF();
            if (message.startsWith(ServerCommandConstants.AUTHENTICATION)) {
                String[] authInfo = message.split(" ");
                String nickName = server.getAuthService().getNickNameByLoginAndPassword(authInfo[1], authInfo[2]);
                if (nickName != null) {
                    if (!server.isNickNameBusy(nickName)) {
                        sendAuthenticationMessage(true);
                        this.nickName = nickName;
//                        server.broadcastMessage(nickName + " зашел в чат");
                        server.broadcastMessage(ServerCommandConstants.ENTER + " " + nickName);
                        sendMessage(server.getClients());
                        server.addConnectedUser(this);
                        return;
                    } else {
                        sendAuthenticationMessage(false);
                    }
                } else {
                    sendAuthenticationMessage(false);
                }
            }
        }
    }

    private void sendAuthenticationMessage(boolean authenticated) throws IOException {
        outputStream.writeBoolean(authenticated);
    }

    private void readMessages() throws IOException {
        while (true) {
            String messageInChat = inputStream.readUTF();
            System.out.println("от " + nickName + ": " + messageInChat);
            if (messageInChat.equals(ServerCommandConstants.EXIT)) {
                closeConnection();
                return;
            }
            server.broadcastMessage(nickName + ": " + messageInChat);
            writeFileHistory(nickName + " :" + messageInChat);
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void writeFileHistory(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("history.txt", true))) {
            bufferedWriter.write(message + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        server.disconnectUser(this);
        server.broadcastMessage(ServerCommandConstants.EXIT + " " + nickName);
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
