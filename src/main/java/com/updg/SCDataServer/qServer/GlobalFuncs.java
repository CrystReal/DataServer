package com.updg.SCDataServer.qServer;

import com.updg.SCDataServer.Daemon;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alex
 * Date: 24.01.14  22:21
 */
public class GlobalFuncs {
    public static void addKill(int playerId, int victimId, long time, int gameId, GameType type) {
        Jedis j = Daemon.getJedis();

        List<String> kills = new ArrayList<String>();
        String killsRedis = j.get("lastKills_" + playerId);
        if (killsRedis != null) {
            kills.addAll(Arrays.asList(killsRedis.split(":")));
        }

        kills.add(victimId + "|" + time + "|" + gameId + "|" + type.getId());
        while (kills.size() > 50) {
            kills.remove(0);
        }
        killsRedis = GlobalFuncs.implode(":", kills);
        j.set("lastKills_" + playerId, killsRedis);
        j.incr("totalKills_" + playerId);

        // DEATHS

        List<String> deaths = new ArrayList<String>();
        String deathsRedis = j.get("lastDeaths_" + victimId);
        if (deathsRedis != null) {
            deaths.addAll(Arrays.asList(deathsRedis.split(":")));
        }

        deaths.add(playerId + "|" + time + "|" + gameId + "|" + type.getId());
        while (deaths.size() > 50) {
            deaths.remove(0);
        }
        deathsRedis = GlobalFuncs.implode(":", deaths);
        j.set("lastDeaths_" + victimId, deathsRedis);
        j.incr("totalDeaths_" + victimId);
        Daemon.jedisPool.returnResource(j);
    }

    public static String implode(String imp, List<String> l) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < l.size(); i++) {
            b.append(l.get(i));
            if (i < l.size() - 1) {
                b.append(imp);
            }
        }
        return b.toString();
    }

    public static void addWin(int userId) {
        Jedis j = Daemon.getJedis();
        j.incr("winsCount_" + userId);
        Daemon.jedisPool.returnResource(j);
    }

    public static void addPlayerGame(int userId) {
        Jedis j = Daemon.getJedis();
        j.incr("totalGames_" + userId);
        Daemon.jedisPool.returnResource(j);
    }

    public static void addGame(int gameId, GameType type, long time, int winner) {
        Jedis j = Daemon.getJedis();

        List<String> kills = new ArrayList<String>();
        String killsRedis = j.get("lastGames");
        if (killsRedis != null) {
            kills.addAll(Arrays.asList(killsRedis.split(":")));
        }

        kills.add(time + "|" + gameId + "|" + type.getId() + "|" + winner);
        while (kills.size() > 100) {
            kills.remove(0);
        }
        killsRedis = GlobalFuncs.implode(":", kills);
        j.set("lastGames", killsRedis);
        Daemon.jedisPool.returnResource(j);
    }
}
