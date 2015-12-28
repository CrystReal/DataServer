package com.updg.SCDataServer.qServer;

/**
 * Created by Alex
 * Date: 24.01.14  22:24
 */
public enum GameType {
    DefKill(1), QuakeCraft(2), BowSpleef(3), TNTRun(4);
    private final int id;

    GameType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
