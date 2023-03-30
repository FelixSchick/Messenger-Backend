package de.illegalaccess.messenger.client;

import de.illegalaccess.messenger.server.ChatServer;
import de.illegalaccess.messenger.utils.Features;

import java.lang.annotation.Target;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    private List<Features> activatedFeatures;
 
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        activatedFeatures = new ArrayList<>();
    }
 
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the chat server");
 
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return this.userName;
    }

    List<Features> getActivatedFeatures() {
        return activatedFeatures;
    }
 
 
    public static void main(String[] args) {
        if (args.length < 2) return;
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}