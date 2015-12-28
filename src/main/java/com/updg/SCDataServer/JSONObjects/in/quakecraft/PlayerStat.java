package com.updg.SCDataServer.JSONObjects.in.quakecraft;

import java.util.HashMap;

/**
 * Created by Sceri on 08.01.14.
 */
public class PlayerStat {

    private int _playerId;
    private long _timeInGame;
    private int _shots;
    private HashMap<Long, Integer> killed = new HashMap<Long, Integer>();
    private int _deaths;
    private boolean _exit;

    public int getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(int _playerId) {
        this._playerId = _playerId;
    }

    public long getTimeInGame() {
        return _timeInGame;
    }

    public void setTimeInGame(long _timeInGame) {
        this._timeInGame = _timeInGame;
    }

    public int getShots() {
        return _shots;
    }

    public void setShots(int _shots) {
        this._shots = _shots;
    }

    public void setKilled(HashMap<Long, Integer> killed ) {
        this.killed = killed;
    }

    public HashMap<Long, Integer> getKilled() {
        return this.killed;
    }

    public void setDeaths(int _deaths) {
        this._deaths = _deaths;
    }

    public int getDeaths() {
        return this._deaths;
    }

    public void setExit(boolean _exit) {
        this._exit = _exit;
    }

    public boolean isExit() {
        return this._exit;
    }
}
