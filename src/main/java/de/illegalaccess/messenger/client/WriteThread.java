package de.illegalaccess.messenger.client;

import de.illegalaccess.messenger.utils.Features;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_RESET = "\u001B[0m";

    public void run() {

        Console console = System.console();

        System.out.println(ANSI_RESET);

        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            text = console.readLine("[" + userName + "]: ");
            if (text.equalsIgnoreCase("--help")) {
                System.out.println("You can use the following commands:");
                System.out.println("-getMyIP: if u want to show ur ip");
                System.out.println("-fact: tells u random facts");
            } else if (text.equalsIgnoreCase("-getMyIP")) {
                try {
                    System.out.println("Ur IP is: " + InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    System.out.println("Error while getting IP address");
                }
            } else if (text.startsWith("-h4cker")) {
                String[] args = text.split(" ");
                if (args.length >= 2) {
                    if (args[1].equals("anonymous")) {
                        if (client.getActivatedFeatures().contains(Features.H4CKER)) {
                            System.out.println(ANSI_RESET);
                            System.out.println("Disabled hackermode");
                            client.getActivatedFeatures().remove(Features.H4CKER);
                        } else {
                            System.out.println(ANSI_GREEN);
                            System.out.println("Activated hackermode");
                            client.getActivatedFeatures().add(Features.H4CKER);
                        }
                    } else
                        System.out.println(ANSI_GREEN + "U will never be a real hacker..." + ANSI_RESET);
                } else
                    System.out.println(ANSI_GREEN + "U will never be a real hacker..." + ANSI_RESET);

            } else if (text.equalsIgnoreCase("-fact")) {
                int randomNum = ThreadLocalRandom.current().nextInt(1,5+1);
                switch (randomNum) {
                    case 1:
                        System.out.println("Spoiler: Ironman stirbt.");
                        break;
                    case 2:
                        System.out.println("Wusstest du dass... dieser Messenger von nur einer Person programmiert wurde.");
                        break;
                    case 3:
                        System.out.println("Die Raumfähre Enterprise (NASA Orbiter Vehicle Designation: OV-101) war die erste Raumfähre. Es wurde für die NASA als Teil des Space Shuttle-Programms gebaut, um Testflüge in der Atmosphäre durchzuführen.");
                        break;
                    case 4:
                        System.out.println("Der größte Feind des Wissens ist nicht Unwissenheit, sondern die Illusion, wissend zu sein. ~Stephen Hawking");
                        break;
                    case 5:
                        System.out.println("Wenn etwas wichtig genug ist, dann mach es, auch wenn alle Chancen gegen dich stehen. ~Elon Musk");
                        break;
                }
            }  else
                writer.println(text);
        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}