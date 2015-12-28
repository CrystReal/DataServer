package com.updg.SCDataServer.Utils;

import com.updg.SCDataServer.Daemon;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;

/**
 * Created by Alex
 * Date: 28.01.14  14:47
 */
public class J {
    public static String get(String s) {
        Jedis j = Daemon.getJedis();
        String o = null;
        try {
            o = j.get(s);
        } catch (JedisConnectionException e) {
            if (null != j) {
                Daemon.jedisPool.returnBrokenResource(j);
                j = null;
            }
        } finally {
            if (null != j)
                Daemon.jedisPool.returnResource(j);
        }
        return o;
    }

    public static String set(String s, String val) {
        Jedis j = Daemon.getJedis();
        String o = null;
        try {
            o = j.set(s, val);
        } catch (JedisConnectionException e) {
            if (null != j) {
                Daemon.jedisPool.returnBrokenResource(j);
                j = null;
            }
        } finally {
            if (null != j)
                Daemon.jedisPool.returnResource(j);
        }
        return o;
    }
}
