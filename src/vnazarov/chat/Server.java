package vnazarov.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;

    private static final String EXIT = "exit";
    private static final int PORT = 1233;
    private Connection con;

    public Server() {
        try {
            server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                con = new Connection(socket);
                connections.add(con);
                con.start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            server.close();
            synchronized (connections) {
                for (Connection c : connections) {
                    c.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class Connection extends Thread {

        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;
        String user_name = "";

        public Connection(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
            }
        }

        @Override
        public void run() {
            try {
                con.out.println("Enter your name: ");
                while(user_name.equals(""))
                    user_name = in.readLine();

                synchronized (connections) {
                    for (Connection c : connections) {
                        c.out.println(user_name + " join now");
                    }
                }
                String msg = "";
                while (true) {
                    msg = in.readLine();
                    if (msg.toLowerCase().equals("exit")) {
                        break;
                    }

                    for (Connection c : connections) {
                        c.out.println(user_name + ": " + msg);
                    }
                }

                for (Connection c : connections) {
                    c.out.println(user_name + ": has left");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                close();
            }
        }

        public void close() {
            try {
                in.close();
                out.close();
                socket.close();
                connections.remove(this);
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
