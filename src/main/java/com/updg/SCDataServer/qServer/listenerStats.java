package com.updg.SCDataServer.qServer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.updg.SCDataServer.Daemon;
import com.updg.SCDataServer.JSONObjects.in.defkill.playerStats;
import com.updg.SCDataServer.JSONObjects.in.defkill.playerStatsKills;
import com.updg.SCDataServer.Utils.L;
import com.updg.SCDataServer.qServer.workerers.BowSpleef;
import com.updg.SCDataServer.qServer.workerers.DefKill;
import com.updg.SCDataServer.qServer.workerers.QuakeCraft;
import com.updg.SCDataServer.qServer.workerers.TNTRun;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alex
 * Date: 29.10.13  20:39
 */
public class listenerStats extends Thread implements Runnable {
    private Channel c = null;
    QueueingConsumer consumer = null;

    public void run() {
        try {
            c = qConnection.c.createChannel();
        } catch (IOException e) {
            L.$("FATAL ERROR: Cant create channel for game stats");
            e.printStackTrace();
            System.exit(0);
            return;
        }
        try {
            c.queueDeclare("gamesStats", true, false, false, null);
            consumer = new QueueingConsumer(c);
            c.basicConsume("gamesStats", true, consumer);
        } catch (IOException e) {
            L.$("FATAL ERROR: cant start listen channel for game stats");
            e.printStackTrace();
            System.exit(0);
            return;
        }
        while (true) {
            QueueingConsumer.Delivery delivery = null;
            try {
                delivery = consumer.nextDelivery();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
            String[] line = message.split("\t");
            if (line[0].equals("defkill")) {
                new DefKill().parse(line[1]);
            }
            if (line[0].equals("bowspleef")) {
                new BowSpleef().parse(line[1]);
            }
            if (line[0].equals("tntrun")) {
                new TNTRun().parse(line[1]);
            }
            if (line[0].equals("quakecraft")) {
                new QuakeCraft().parse(line[1]);
            }
        }
    }

}
