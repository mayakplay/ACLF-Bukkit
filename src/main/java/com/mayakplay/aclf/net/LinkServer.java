package com.mayakplay.aclf.net;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author winterprison
 * @version 0.0.1
 * @since 06.07.2019.
 */
public class LinkServer {

    private final Map<String,LinkConnection> serversMap = new HashMap<>();

    @Setter
    @Getter
    private String serverName;

    @Setter
    @Getter
    private int receivePort;
    private ServerSocket serverSocket;

    private Thread serverThread;
    private Thread clientThread;
    private boolean serverListening = false;

    public LinkServer(int receivePort) {
        this.receivePort = receivePort;
    }

    public void startListening(){
        if(serverThread != null){
            serverThread.interrupt();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (serverThread.getState() != Thread.State.TERMINATED){

            }
        }
        serverListening = true;
        serverThread = new Thread(() -> {
            DataInputStream In = null;
            DataOutputStream Out = null;
            try {
                serverSocket = new ServerSocket(receivePort); // создаем сокет сервера и привязываем его к вышеуказанному порту
                while (serverListening){
                    try {
                        Socket socket = serverSocket.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
                        if (socket.isConnected()) {
                            In = new DataInputStream(socket.getInputStream());
                            Out = new DataOutputStream(socket.getOutputStream());

                            String data = null;

                            try {
                                while (socket.isConnected() && serverListening) {

                                    data = In.readUTF(); // ожидаем пока клиент пришлет строку текста.
                                    JsonObject json = (JsonObject)new JsonParser().parse(data);

                                    String sender = null;
                                    if(json.has("sender") && !json.get("sender").isJsonNull()){
                                        sender = json.get("sender").getAsString();
                                    }

                                    if(!serversMap.containsKey(sender)
                                            || !serversMap.get(sender).getAddress().equals(socket.getInetAddress().getHostName())){
                                        Out.writeUTF(onUnknownSenderGetData(socket.getInetAddress(), json).toString());
                                        Out.flush();
                                    }else{
                                        Out.writeUTF(onGetData(sender,json).toString());
                                        Out.flush();
                                    }
                                }
                            } catch (IOException e) {
                            }finally {
                                socket.close();
                            }
                        }
                    } catch (SocketException se){
                        serverSocket.close();
                        serverSocket = new ServerSocket(receivePort);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ee) {
                            ee.printStackTrace();
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            } catch(Exception x) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                    if (In != null) {
                        In.close();
                    }
                    if (Out != null) {
                        Out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    public void stopListening() {
        serverListening = false;
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverThread.interrupt();
    }

    public void addServer(String name, String address, int port){
        serversMap.put(name,new LinkConnection(address, port));
    }

    public void addServer(String name, LinkConnection connection){
        serversMap.put(name,connection);
    }

    public void RemoveServer(String name){
        serversMap.remove(name);
    }

    public void interrupt(){
        clientThread.interrupt();
        stopListening();
    }

    public void sendData(String receiverName, String message, Consumer<JsonObject> callback) {
        JsonObject json = new JsonObject();
        json.addProperty("message",message);
        sendData(receiverName,json, callback);
    }

    public void sendData(String receiverName, String message) {
        JsonObject json = new JsonObject();
        json.addProperty("message",message);
        sendData(receiverName,json, null);
    }
    public void sendData(String receiverName, JsonObject data, Consumer<JsonObject> callback){
        if(!serversMap.containsKey(receiverName)){
            System.out.println(("Unknown server name: " + receiverName));
            return;
        }
        data.addProperty("sender",serverName);
        serversMap.get(receiverName).messageQueue.add(new LinkQueue(data,callback));

        if(clientThread == null || !clientThread.isAlive()){
            clientThread = new Thread(()->{

                while(!clientThread.isInterrupted()){
                    for (LinkConnection link:serversMap.values()) {
                        DataInputStream In = null;
                        DataOutputStream Out = null;
                        Socket socket = new Socket();
                        try {
                            if (!link.messageQueue.isEmpty()) {
                                while (!socket.isConnected() && !clientThread.isInterrupted()) {
                                    socket.connect(new InetSocketAddress(link.getAddress(), link.getPort()));
                                    In = new DataInputStream(socket.getInputStream());
                                    Out = new DataOutputStream(socket.getOutputStream());
                                }
                                if (socket.isConnected()) {
//                                    System.out.println("connected to server");
                                    LinkQueue linkQueue = link.messageQueue.remove();
                                    Out.writeUTF(linkQueue.getData().toString());

                                    String rData = In.readUTF();
                                    JsonParser parser = new JsonParser();
                                    if(linkQueue.getCallback() != null){
                                        linkQueue.getCallback().accept(parser.parse(rData).getAsJsonObject());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        } finally {
                            try {
                                if (In != null) {
                                    In.close();
                                }
                                if (Out != null) {
                                    Out.close();
                                }
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            clientThread.start();
        }
    }

    protected JsonObject onUnknownSenderGetData(InetAddress inetAddress, JsonObject data){
        return new JsonObject();
    }

    protected JsonObject onGetData(String sender, JsonObject data){
        return new JsonObject();
    }

    public enum ServerType {
        UNDEFINED,
        SPIGOT,
        BUNGEE
    }
}