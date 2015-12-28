package com.updg.SCDataServer.qServer.workerers;

import com.updg.SCDataServer.Daemon;
import com.updg.SCDataServer.JSONObjects.in.bowspleef.playerStats;
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
public class BowSpleef implements GameStatsWorkerer {
    @Override
    public void parse(String data) {
        try {
            com.updg.SCDataServer.JSONObjects.in.bowspleef.gameStats game = Daemon.mapper.readValue(data, com.updg.SCDataServer.JSONObjects.in.bowspleef.gameStats.class);
            try {
                PreparedStatement ps;
                Daemon.DB.setAutoCommit(true);
                ps = Daemon.DB.prepareStatement("INSERT INTO bs_stats_games (serverId, mapId, winnerId, start, end) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
                for (playerStats stats : game.getPlayers()) {
                    ps = Daemon.DB.prepareStatement("INSERT INTO bs_stats_players (gameId, playerId, position, isWinner, shots, timeInGame) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, gameId);
                    ps.setInt(2, stats.getPlayerId());
                    ps.setInt(3, stats.getPosition());
                    ps.setBoolean(4, stats.isIsWinner());
                    ps.setInt(5, stats.getShots());
                    ps.setLong(6, stats.getTimeInGame());
                    ps.execute();
                    GlobalFuncs.addPlayerGame(stats.getPlayerId());
                }
                GlobalFuncs.addWin(game.getWinner());
                GlobalFuncs.addGame(gameId, GameType.BowSpleef, game.getEnd(), game.getWinner());
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
