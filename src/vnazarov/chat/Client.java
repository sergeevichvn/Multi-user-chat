package vnazarov.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private String ip;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private static final String EXIT = "exit";

    public Client() {
        init();
        connectToServer();
        if (in != null && out != null && scanner != null) {
            start();
            stop();
        }
    }

    private void init() {
        while (true) {
            try {
                scanner = new Scanner(System.in);
                System.out.println("Enter IP adress of server (format xx.xx.xx.xx): ");
                ip = scanner.nextLine();
                System.out.println("Enter port of server: ");
                port = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Port is incorrect! Retry.");
            }
        }
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Can't connect to server");
        }
    }

    private void start() {
        String message = "";
        new ReaderMessages().start();
        while (!message.equals(EXIT)) {
            message = scanner.nextLine();
            out.println(message);
        }
    }

    private void stop() {
        try {
            socket.close();
            in.close();
            out.close();
            scanner.close();
        } catch (Exception e) {
            System.out.println("Some error :)");
        }
    }

    private class ReaderMessages extends Thread {

        private boolean status = true;

        public void turnOff() {
            status = false;
        }

        @Override
        public void run() {
            try {
                while (status) {
                    String message = in.readLine();
                    System.out.println(message);
                }
            } catch (Exception e) {
                System.out.println("Error in getting message");
            }
        }

    }
}
