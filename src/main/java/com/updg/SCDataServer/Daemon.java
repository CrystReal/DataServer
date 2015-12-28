package com.updg.SCDataServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.updg.SCDataServer.JSONObjects.out.APIProject;
import com.updg.SCDataServer.Utils.L;
import com.updg.SCDataServer.qServer.listenerStats;
import com.updg.SCDataServer.qServer.qConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Alex
 * Date: 12.11.13  20:01
 */
public class Daemon {
    public static JedisPool jedisPool;
    public static ObjectMapper mapper = new ObjectMapper();
    public static ArrayList<APIProject> projects = new ArrayList<APIProject>();
    public static Connection DB;
    public static int countSocket = 0;

    public static void main(String[] args) throws Exception {
        jedisPool = new JedisPool("localhost", 6379);

        qConnection.connect("localhost");
        new listenerStats().start();
        L.$("qServer connected!");

        try {
            DB = (java.sql.Connection) DriverManager.getConnection("jdbc:mysql://localhost/crmc?autoReconnect=true&user=sc_main&password=pass&characterEncoding=UTF-8");
        } catch (SQLException e) {
            L.$("Driver loaded, but cannot connect to db: " + e);
            System.exit(0);
        }

        L.$("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                if (countSocket < 50) {
                    countSocket++;
                    new Worker(listener.accept(), clientNumber++).start();
                }
            }
        } finally {
            listener.close();
            jedisPool.destroy();
        }
    }

    public static Jedis getJedis() {
        Jedis j = jedisPool.getResource();
        j.select(10);
        return j;
    }

    public static void loadProjects() {

    }
}
