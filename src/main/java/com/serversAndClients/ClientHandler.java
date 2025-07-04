package com.serversAndClients;

import com.hibernateClases.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ClientHandler implements Runnable {
    private  static  final Logger LOG  = Logger.getLogger(ClientHandler.class.getName());
    private final Socket socketClient ;
    private  final String serverName;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());


    public ClientHandler(Socket socketClient, String serverName) {
        this.socketClient = socketClient;
        this.serverName = serverName;

    }

    @Override
    public void run() {
        try {
            synchronized (clients) {
                clients.add(this);
            }

        try(PrintWriter out  = new PrintWriter(new OutputStreamWriter(socketClient.getOutputStream()),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()))
        ){


            ServerUtils.clearConsole(out);
            welcome(out,in);
            ServerUtils.welcomeMessage(out);
            String s;
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    try {
                        client.sendMessage("A new user as joined the chat");
                    } catch (IOException e) {
                        LOG.log(Level.WARNING, "Error enviando mensaje a un cliente.", e);
                    }
                }
            }

            while ((s = in.readLine()) != null && !s.equalsIgnoreCase("exit")) {
                broadcastMessage(s);
            }

        }catch (IOException e){
            LOG.log(Level.SEVERE, "Error manejando al cliente",e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        } finally {
            synchronized (clients) {
                clients.remove(this);
            }
            try {
                socketClient.close();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Error cerrando el socket del cliente", e);
            }
        }

    }
    private void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                try {
                    client.sendMessage(message);
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Error enviando mensaje a un cliente.", e);
                }
            }
        }


    }
    private void sendMessage(String message) throws IOException {
        PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
        out.println(message);
    }

    private void welcome(PrintWriter out, BufferedReader in) throws IOException {
    out.printf("Hello welcome to the server: %s%n" , serverName);
    }

}
