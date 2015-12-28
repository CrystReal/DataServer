package com.updg.SCDataServer.qServer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.updg.SCDataServer.Utils.L;

import java.io.IOException;

/**
 * Created by Alex
 * Date: 29.10.13  20:40
 */
public class qConnection {
    public static Connection c = null;

    public static Connection connect(String host) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            c = factory.newConnection();
        } catch (IOException e) {
            L.$("Cant connect to the Q server");
            System.exit(0);
        }
        return c;
    }
}
