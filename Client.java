/*
  Filename:   Client.java
  Date:       7/22/2021
  Author:     James Anyabine
  Email:      joa170000@utdallas.edu
  Version:    1.0
  Copyright:  2021, All Rights Reserved

  Description:
    This is the main source file of the code. 
    
*/


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{

    public static void main(String[] args) throws IOException {

        //This is the Sending thread, also known as the Client Thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run(){
                int serverPort = 6000;
                Socket socket = null;
                ObjectOutputStream toServer = null;
                boolean test = true;
                /*
                if (args.length != 3) {
                    System.out.println("Need 1 argument");
                    System.exit(1);
                }
                */
                int clients = Integer.parseInt(args[0]);
                System.out.println(args[1]);
                int ID = Integer.parseInt(args[1]);
                //string IP = args[3];
                String IPS[];
                IPS = new String[clients];
        
                //Inputs user provided IPs for mapping.
                for (int i = 0; i < clients; i++) {
                    IPS[i] = args[i + 2];
                }
                while (test) {
                    try {
                        Scanner sc = new Scanner(System.in);
                        String sendorStop = sc.nextLine();
                        int temp1 = sendorStop.indexOf(" ");
                        int temp2 = sendorStop.indexOf(" ", temp1+1);
                        String first = sendorStop.substring(0, temp1);
                        //System.out.println(sendorStop.length() + " " + temp1 + " " + first);
                        String second = "";
                        String third = "";
                        if(temp1+1 < sendorStop.length()){
                            second = sendorStop.substring(temp1+1, temp2);
                            //System.out.println( temp2 + " "+ second + " #####");
                            third = sendorStop.substring(temp2 + 1);
                            System.out.println(third);
        
                        }
                        if (first.equals("stop")) {
                            for (int i = 0; i < clients; i++) {
                                if (i == ID - 1) {
                                    continue;
                                } else {
                                    //Creates a socket in the 6000s
                                    //socket.setReuseAddress(true);
                                    String host = IPS[i];
                                    InetAddress serverHost = InetAddress.getByName(host);
                                    socket = new Socket(serverHost, serverPort);
                                    toServer = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                    //Message msgToSend = new Message("stop");
                                    toServer.writeObject("stop ");
                                    toServer.flush();
                                    toServer.close();
                                }
                            }
                            //try { 
                                //obj1.wait(100);
                                System.out.println("Exiting now...");
                                test = false;
                                socket.close();
                                System.exit(0);
                            //} catch (InterruptedException e)  {
                                //Thread.currentThread().interrupt(); 
                                //Log.error("Thread interrupted", e); 
                            //}
                           
                            //This is the Sending Portion of the Client
                        }
                        else if (first.equals("send")) {
                            if (second.equals("0")) {
                                for (int i = 0; i < clients; i++) {
                                    if (i == ID - 1) {
                                        continue;
                                    } else {
                                        //Creates a socket in the 6000s                                                                                                                                
                                        //socket.setReuseAddress(true);
                                        String host = IPS[i];
                                        InetAddress serverHost = InetAddress.getByName(host);
                                        socket = new Socket(serverHost, serverPort);
                                        toServer = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                        //Message msgToSend = newMessage(third);
                                        //System.out.println(third);
                                        toServer.writeObject(third);
                                        toServer.flush();
                                        socket.close();
                                    }
                                }
                            } else {
                                int secondint = Integer.parseInt(second);
                                if ((secondint < 0) || (secondint > clients) || (secondint == ID)) {
                                    System.err.println("This is an invalid ID number, please try again.");
                                    System.exit(1);
                                } else {
                                    //socket.setReuseAddress(true);
                                    String host = IPS[secondint - 1];
                                    InetAddress serverHost = InetAddress.getByName(host);
                                    socket = new Socket(serverHost, serverPort);
                                    toServer = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                    //Message msgToSend = new Message(third);
                                    toServer.writeObject(third);
                                    toServer.flush();
                                    socket.close();
                                }
                            }
                        }
                        else {
                            System.err.println("This is an invalid input, Exiting Now.");
                            test = false;
                            System.exit(1);
                        }
        
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    } finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        });

        //This is the receiveing thread, otherwise known as the server thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run(){
                int serverPort = 6000;
                ServerSocket serverSocket = null;
                ObjectOutputStream toClient = null;
                ObjectInputStream fromClient = null;
                boolean test = true;
                try {
                    serverSocket = new ServerSocket(serverPort);
                    while(test) {
                        try {
                            //System.out.println("Entering Server... ");
                            Socket socket = serverSocket.accept();
                            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
                            toClient = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                            fromClient = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                            String word = (String)fromClient.readObject();
                            //String word = msgRequest.word;
                            //toClient.writeObject(new Message(number*number));
                            if(word.equals("stop ")){
                                System.out.println("Exiting now...");
                                socket.close();
                                test = false; 
                                System.exit(0);
                            }
                            System.out.println("Client: "+ word); 
                            toClient.flush();

                            
                        }catch(IOException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }catch(ClassNotFoundException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
    
            }
        });

        sendMessage.start();
        readMessage.start();
    }
}