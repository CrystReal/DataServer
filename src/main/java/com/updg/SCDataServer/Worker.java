package com.updg.SCDataServer;

import com.updg.SCDataServer.Utils.J;
import com.updg.SCDataServer.Utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Alex
 * Date: 12.11.13  20:06
 */
public class Worker extends Thread implements Runnable {
    private Socket socket;
    private int clientNumber;

    public Worker(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        L.$("New connection with client# " + clientNumber + " at " + socket);
    }

    public void run() {
        try {

            // Decorate the streams so we can send characters
            // and not just bytes.  Ensure output is flushed
            // after every newline.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Get messages from the client, line by line; return them
            // capitalized
            while (true) {
                String input = in.readLine();
                if (input == null || input.equals(".")) {
                    break;
                }
                if (input.startsWith("getPlayerAchievements\t")) {
                    input = input.replaceAll("getPlayerAchievements\t", "");
                    out.println(J.get("ach_" + input.toLowerCase()));
                }
                if (input.startsWith("getPlayerExpAndMoney\t")) {
                    input = input.replaceAll("getPlayerExpAndMoney\t", "");
                    double exp = 0;
                    if (J.get("exp_" + input.toLowerCase()) != null)
                        exp = Double.parseDouble(J.get("exp_" + input.toLowerCase()));

                    double money = 0;
                    if (J.get("money_" + input.toLowerCase()) != null)
                        money = Double.parseDouble(J.get("money_" + input.toLowerCase()));
                    out.println(exp + ":" + money);
                }
                if (input.startsWith("withdrawPlayerExpAndMoney\t")) {
                    input = input.replaceAll("withdrawPlayerExpAndMoney\t", "");
                    String[] t = input.split(":");
                    double newExp = Double.parseDouble(J.get("exp_" + t[0].toLowerCase())) - Double.parseDouble(t[1]);
                    double newMoney = Double.parseDouble(J.get("money_" + t[0].toLowerCase())) - Double.parseDouble(t[2]);
                    J.set("exp_" + t[0].toLowerCase(), newExp + "");
                    J.set("money_" + t[0].toLowerCase(), newMoney + "");
                    out.println(newExp + ":" + newMoney);
                }
                if (input.startsWith("addPlayerExpAndMoney\t")) {
                    input = input.replaceAll("addPlayerExpAndMoney\t", "");
                    String[] t = input.split(":");
                    double exp = 0;
                    if (J.get("exp_" + t[0].toLowerCase()) != null)
                        exp = Double.parseDouble(J.get("exp_" + t[0].toLowerCase()));

                    double money = 0;
                    if (J.get("money_" + t[0].toLowerCase()) != null)
                        money = Double.parseDouble(J.get("money_" + t[0].toLowerCase()));
                    double newExp = exp + Double.parseDouble(t[1]);
                    double newMoney = money + Double.parseDouble(t[2]);
                    J.set("exp_" + t[0].toLowerCase(), newExp + "");
                    J.set("money_" + t[0].toLowerCase(), newMoney + "");
                    out.println(newExp + ":" + newMoney);
                }
                if (input.startsWith("QCgetParams\t")) {
                    input = input.replaceAll("QCgetParams\t", "");
                    out.println("0:0:0:0:0:0");
                }

            }
        } catch (IOException e) {
            L.$("Error handling client# " + clientNumber + ": " + e);
            Daemon.countSocket--;
        } finally {
            try {
                socket.close();
                Daemon.countSocket--;
            } catch (IOException e) {
                L.$("Couldn't close a socket, what's going on?");
            }
            L.$("Connection with client# " + clientNumber + " closed");
        }
    }
}
