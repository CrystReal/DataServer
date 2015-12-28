package com.updg.SCDataServer.qServer.workerers;

import com.updg.SCDataServer.Daemon;
import com.updg.SCDataServer.JSONObjects.in.quakecraft.GameStat;
import com.updg.SCDataServer.JSONObjects.in.quakecraft.PlayerStat;
import com.updg.SCDataServer.qServer.GameType;
import com.updg.SCDataServer.qServer.GlobalFuncs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Alex
 * Date: 14.12.13  18:48
 */
public class QuakeCraft implements GameStatsWorkerer {
    @Override
    public void parse(String data) {
        try {
            GameStat game = Daemon.mapper.readValue(data, GameStat.class);
            try {
                PreparedStatement ps;
                Daemon.DB.setAutoCommit(true);
                ps = Daemon.DB.prepareStatement("INSERT INTO qc_stats_games (serverId, mapId, winnerId, start, end) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, game.getServerId());
                ps.setInt(2, game.getMapId());
                ps.setInt(3, game.getWinner());
                ps.setLong(4, game.getStart());
                ps.setLong(5, game.getEnd());
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Saving game failed, no rows affected.");
                }

                ResultSet generatedKeys = ps.getGeneratedKeys();
                int gameId = 0;
                if (generatedKeys.next()) {
                    gameId = generatedKeys.getInt(1);
                }
                Daemon.DB.setAutoCommit(false);
                for (PlayerStat stats : game.getPlayers()) {
                    ps = Daemon.DB.prepareStatement("INSERT INTO qc_stats_players (gameId, playerId, kills, deaths, shots, tillFinish, timeInGame) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, gameId);
                    ps.setInt(2, stats.getPlayerId());
                    ps.setInt(3, stats.getKilled().size());
                    ps.setInt(4, stats.getDeaths());
                    ps.setInt(5, stats.getShots());
                    ps.setBoolean(6, stats.isExit());
                    ps.setLong(7, stats.getTimeInGame());
                    ps.execute();
                    Iterator it = stats.getKilled().entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry) it.next();
                        ps = Daemon.DB.prepareStatement("INSERT INTO qc_stats_players_victims (gameId,time,playerId,victim) VALUES (?, ?, ?, ?)");
                        ps.setInt(1, gameId);
                        ps.setLong(2, (Long) pairs.getKey());
                        ps.setInt(3, stats.getPlayerId());
                        ps.setInt(4, (Integer) pairs.getValue());
                        ps.execute();
                        GlobalFuncs.addKill(stats.getPlayerId(), (Integer) pairs.getValue(), (Long) pairs.getKey(), gameId, GameType.QuakeCraft);
                    }
                    if (stats.getPlayerId() == game.getWinner()) {
                        GlobalFuncs.addWin(stats.getPlayerId());
                    }
                    GlobalFuncs.addPlayerGame(stats.getPlayerId());
                }
                GlobalFuncs.addGame(gameId, GameType.QuakeCraft, game.getEnd(), game.getWinner());
                Daemon.DB.commit();
                Daemon.DB.setAutoCommit(true);
                game = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
