package com.updg.SCDataServer.qServer.workerers;

import com.updg.SCDataServer.Daemon;
import com.updg.SCDataServer.JSONObjects.in.tntrun.playerStats;
import com.updg.SCDataServer.qServer.GameType;
import com.updg.SCDataServer.qServer.GlobalFuncs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alex
 * Date: 14.12.13  18:48
 */
public class TNTRun implements GameStatsWorkerer {
    @Override
    public void parse(String data) {
        try {
            com.updg.SCDataServer.JSONObjects.in.tntrun.gameStats game = Daemon.mapper.readValue(data, com.updg.SCDataServer.JSONObjects.in.tntrun.gameStats.class);
            try {
                PreparedStatement ps;
                Daemon.DB.setAutoCommit(true);
                ps = Daemon.DB.prepareStatement("INSERT INTO tntrun_stats_games (serverId, winnerId, start, end, mapId) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, game.getServerId());
                ps.setInt(2, game.getWinner());
                ps.setLong(3, game.getStart());
                ps.setLong(4, game.getEnd());
                ps.setInt(5, game.getMapId());
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
                for (playerStats stats : game.getPlayers()) {
                    ps = Daemon.DB.prepareStatement("INSERT INTO tntrun_stats_players (gameId, playerId, position, isWinner, timeInGame)  VALUES (?, ?, ?, ?, ?)");
                    ps.setInt(1, gameId);
                    ps.setInt(2, stats.getPlayerId());
                    ps.setInt(3, stats.getPosition());
                    ps.setBoolean(4, stats.isIsWinner());
                    ps.setLong(5, stats.getTimeInGame());
                    ps.execute();
                    if (stats.getPlayerId() == game.getWinner()) {
                        GlobalFuncs.addWin(stats.getPlayerId());
                    }
                    GlobalFuncs.addPlayerGame(stats.getPlayerId());
                }
                GlobalFuncs.addGame(gameId, GameType.TNTRun, game.getEnd(), game.getWinner());
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
