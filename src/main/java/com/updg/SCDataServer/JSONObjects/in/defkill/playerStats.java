package com.updg.SCDataServer.JSONObjects.in.defkill;

import java.util.List;

/**
 * Created by Alex
 * Date: 12.11.13  21:17
 */
public class playerStats {
    private int _playerId;
    private int _nexusDamage;
    private int _killedGolems;
    private int _deaths;
    private int _team;
    private List<playerStatsKills> _victims;
    private long _timeInGame;
    private boolean _tillFinish;
    private playerStatsMinedOres _minedOres;

    public int getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(int _playerId) {
        this._playerId = _playerId;
    }

    public int getNexusDamage() {
        return _nexusDamage;
    }

    public void setNexusDamage(int _nexusDamage) {
        this._nexusDamage = _nexusDamage;
    }

    public int getKilledGolems() {
        return _killedGolems;
    }

    public void setKilledGolems(int _killedGolems) {
        this._killedGolems = _killedGolems;
    }

    public int getDeaths() {
        return _deaths;
    }

    public void setDeaths(int _deaths) {
        this._deaths = _deaths;
    }

    public long getTimeInGame() {
        return _timeInGame;
    }

    public void setTimeInGame(long _timeInGame) {
        this._timeInGame = _timeInGame;
    }

    public List<playerStatsKills> getVictims() {
        return _victims;
    }

    public void setVictims(List<playerStatsKills> _victims) {
        this._victims = _victims;
    }

    public boolean isTillFinish() {
        return _tillFinish;
    }

    public void setTillFinish(boolean _tillFinish) {
        this._tillFinish = _tillFinish;
    }

    public playerStatsMinedOres getMinedOres() {
        return _minedOres;
    }

    public void setMinedOres(playerStatsMinedOres _minedOres) {
        this._minedOres = _minedOres;
    }

    public int getTeam() {
        return _team;
    }

    public void setTeam(int _team) {
        this._team = _team;
    }
}
