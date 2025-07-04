package com;

import com.Database.MessageService;
import com.serversAndClients.Server;
import com.serversAndClients.socketClient;

public class main {
    public static void main (String [] args){
       Server server = new Server();
       server.startServer();
    }
}




