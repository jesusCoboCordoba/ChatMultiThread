package com.serversAndClients;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int PORT  = 12345;
    private static final String SERVER_NAME = "Yisus-server";
    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    public void startServer(){
        try{
            serverSocket = new ServerSocket(PORT);
            threadPool  = Executors.newCachedThreadPool();
            LOG.info("Se ha inicializado el servidor en el puerto" + PORT);

            while (!serverSocket.isClosed()){
                Socket socketClient = serverSocket.accept();
                    LOG.info("Se ha conectado un cliente al servidor" + socketClient.getInetAddress());
                    threadPool.submit(new ClientHandler(socketClient, SERVER_NAME));

            }


        }catch(IOException e){
            LOG.log(Level.SEVERE,"ERROR AL INICIAR EL SERVIDOR",e);

        }
    }



}
