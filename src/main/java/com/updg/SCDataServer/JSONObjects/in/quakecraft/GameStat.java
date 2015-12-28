package com.updg.SCDataServer.JSONObjects.in.quakecraft;

import java.util.List;

/**
 * Created by Sceri on 08.01.14.
 */
public class GameStat {

    private int _serverId;
    private int _winner;
    private int _mapId;
    private long _start;
    private long _end;
    private List<PlayerStat> _players;

    public List<PlayerStat> getPlayers() {
        return _players;
    }

    public void setPlayers(List<PlayerStat> _players) {
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

    public void setMapId(int _mapId) {
        this._mapId = _mapId;
    }

    public int getMapId() {
        return this._mapId;
    }

}
