
package com;


import com.Database.UserService;
import com.hibernateClases.User;
import com.serversAndClients.Server;
import com.serversAndClients.socketClient;

import java.io.*;

public class WelcomePage {

    private static final String CLEAR_CHARACTER = "\033[H\033[2J";
    private static final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintWriter consoleWriter = new PrintWriter(System.out,true);
    private static final UserService userService = new UserService();


    public static void main(String[] args) throws IOException {
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.Welcome();
    }
    public void Welcome() throws IOException {
            welcomeMessage();
            String in;
            while ((in= consoleReader.readLine())!=null){
                if(in.equalsIgnoreCase("1")){
                    searchUser();
                }else if (in.equalsIgnoreCase("2")){
                    registerUser();
                }else if(in.equalsIgnoreCase("exit") || in.equalsIgnoreCase("3") ){
                    break;
                }else{
                    consoleWriter.println("Please try again enter a number (1 or 2) or type \"exit\" to close the application");
                }
            }
        consoleWriter.println("Thank you for using our chat! , bye :D");
            consoleReader.close();
            consoleWriter.close();

    }

    private void welcomeMessage() {
        consoleWriter.println("Hello there! welcome to the chatroom");
        consoleWriter.println("Please enter number as an option");
        consoleWriter.println("1 = log in");
        consoleWriter.println("2 = sign in (if you're new)");
        consoleWriter.println("3 = Exit");
    }

    private void searchUser() throws IOException {
            User user;

        consoleWriter.println("Please enter your name");
        String name  = consoleReader.readLine();
    try {
    user = userService.readUserByUserName(name);
    while (user == null) {
        consoleWriter.println("There is no one registered in our DB with that username, please try again");
        consoleWriter.println("if you want to register please type 2");
        name = consoleReader.readLine();
        if (name.equals("2")){
            registerUser();
        }
        user = userService.readUserByUserName(name);
    }
    consoleWriter.println("enter your password");
    String passwordGiven = consoleReader.readLine();
    while (!passwordVerification(passwordGiven, user)) {
        consoleWriter.println("Incorrect password, please try again");
        passwordGiven = consoleReader.readLine();
    }

    consoleWriter.println("welcome back " + user.getName() +"!");
    socketClient socketClient = new socketClient();
    socketClient.watch(user);
    }catch (Exception e){
     e.printStackTrace();

    }
        userService.closeSession();

    }

    private void registerUser() throws IOException {
        User user = new User();
        consoleWriter.println("Please write your name");
        String name = consoleReader.readLine();
        while(userService.readUserByUserName(name)!=null){
         consoleWriter.println("There is already an user with that name, try something else");
            name = consoleReader.readLine();
        }
        consoleWriter.println("Please write your password");
        String password = consoleReader.readLine();
        consoleWriter.println("Please write your email");
        String email = consoleReader.readLine();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        userService.saveUser(user);
        searchUser();
    }

    private boolean passwordVerification(String password,User user) {
        String passwordDb = user.getPassword();
        return passwordDb.equals(password);
    }
}
