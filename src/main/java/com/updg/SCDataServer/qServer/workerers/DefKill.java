package com.updg.SCDataServer.qServer.workerers;

import com.updg.SCDataServer.Daemon;
import com.updg.SCDataServer.JSONObjects.in.defkill.playerStats;
import com.updg.SCDataServer.JSONObjects.in.defkill.playerStatsKills;
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
public class DefKill implements GameStatsWorkerer {
    @Override
    public void parse(String data) {
        try {
            com.updg.SCDataServer.JSONObjects.in.defkill.gameStats game = Daemon.mapper.readValue(data, com.updg.SCDataServer.JSONObjects.in.defkill.gameStats.class);
            try {
                PreparedStatement ps;
                Daemon.DB.setAutoCommit(true);
                ps = Daemon.DB.prepareStatement("INSERT INTO dk_stats_games (serverId,map,winner,winType,start,end) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, game.getServerId());
                ps.setInt(2, game.getMap());
                ps.setInt(3, game.getWinner());
                ps.setInt(4, game.getWinType());
                ps.setLong(5, game.getStart());
                ps.setLong(6, game.getEnd());
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
                    ps = Daemon.DB.prepareStatement("INSERT INTO dk_stats_players (gameId,playerId,team,nexusDamage,killedGolems,death,timeInGame,tillFinish) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, gameId);
                    ps.setInt(2, stats.getPlayerId());
                    ps.setInt(3, stats.getTeam());
                    ps.setInt(4, stats.getNexusDamage());
                    ps.setInt(5, stats.getKilledGolems());
                    ps.setInt(6, stats.getDeaths());
                    ps.setLong(7, stats.getTimeInGame());
                    ps.setBoolean(8, stats.isTillFinish());
                    ps.execute();
                    for (playerStatsKills victim : stats.getVictims()) {
                        ps = Daemon.DB.prepareStatement("INSERT INTO dk_stats_players_victims (gameId,time,playerId,victim) VALUES (?, ?, ?, ?)");
                        ps.setInt(1, gameId);
                        ps.setLong(2, victim.getTime());
                        ps.setInt(3, stats.getPlayerId());
                        ps.setInt(4, victim.getVictim());
                        ps.execute();
                        GlobalFuncs.addKill(stats.getPlayerId(), victim.getVictim(), victim.getTime(), gameId, GameType.DefKill);
                    }
                    ps = Daemon.DB.prepareStatement("INSERT INTO dk_stats_mined_ores (gameId,playerId,wood,coal,iron,gold,diamonds) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, gameId);
                    ps.setInt(2, stats.getPlayerId());
                    ps.setInt(3, stats.getMinedOres().getWood());
                    ps.setInt(4, stats.getMinedOres().getCoal());
                    ps.setInt(5, stats.getMinedOres().getIron());
                    ps.setInt(6, stats.getMinedOres().getGold());
                    ps.setInt(7, stats.getMinedOres().getDiamonds());
                    ps.execute();
                    GlobalFuncs.addPlayerGame(stats.getPlayerId());
                    if (stats.getTeam() == game.getWinner()) {
                        GlobalFuncs.addWin(stats.getPlayerId());
                    }
                }
                GlobalFuncs.addGame(gameId, GameType.DefKill, game.getEnd(), game.getWinner());
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
