package com.updg.SCDataServer.JSONObjects.in.defkill;

/**
 * Created by Alex
 * Date: 12.11.13  21:20
 */
public class playerStatsKills {
    private long _time;
    private int _victim;

    public long getTime() {
        return _time;
    }

    public void setTime(long time) {
        this._time = time;
    }

    public int getVictim() {
        return _victim;
    }

    public void setVictim(int victim) {
        this._victim = victim;
    }
}
