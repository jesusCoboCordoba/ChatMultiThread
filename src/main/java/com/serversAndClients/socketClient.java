package com.serversAndClients;

import com.Database.MessageService;
import com.hibernateClases.Message;
import com.hibernateClases.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class socketClient {

    private static final int PORT = 12345;
    private static final String HOSTNAME = "localhost";
    private static User user;

    public static User getUser() {
        return user;
    }

    public void watch(User user){
        user= user;
        try( Socket socket = new Socket(HOSTNAME,PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in)){

            };
            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada.");
                }
            });
            readerThread.start();
            MessageService messageService = new MessageService();
            String input;
            while ((input = console.readLine()) != null)  {
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("closing connection...");
                    writer.println("exit");
                    break;
                }
                Message message = new Message();
                message.setMessage(input);
                message.setUser(user.getName());
                messageService.saveMessage(message);
                writer.println(message.getMessageDate()+"/" +message.getMessageHour()+" "+ user.getName()+" says: "+input);
            }
            readerThread.join();
            messageService.closeSession();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }     System.out.println("Thank you for using our chat! , bye :D");
        System.exit(0);
    }
    }





