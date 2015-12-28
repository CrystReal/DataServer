package com.updg.SCDataServer.JSONObjects.in.defkill;

import java.util.List;

/**
 * Created by Alex
 * Date: 17.11.13  2:16
 */
public class gameStats {
    private int _serverId;
    private int _map;
    private int _winner;
    private int _winType;
    private long _start;
    private long _end;
    private List<playerStats> _players;

    public List<playerStats> getPlayers() {
        return _players;
    }

    public void setPlayers(List<playerStats> _players) {
        this._players = _players;
    }

    public int getServerId() {
        return _serverId;
    }

    public void setServerId(int _serverId) {
        this._serverId = _serverId;
    }

    public int getWinner() {
        return _winner;
    }

    public void setWinner(int _winner) {
        this._winner = _winner;
    }

    public int getWinType() {
        return _winType;
    }

    public void setWinType(int _winType) {
        this._winType = _winType;
    }

    public long getStart() {
        return _start;
    }

    public void setStart(long _start) {
        this._start = _start;
    }

    public long getEnd() {
        return _end;
    }

    public void setEnd(long _end) {
        this._end = _end;
    }

    public int getMap() {
        return _map;
    }

    public void setMap(int _map) {
        this._map = _map;
    }
}
