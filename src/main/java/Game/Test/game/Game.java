package Game.Test.game;

import Game.Test.util.DataTransferObject;

public class Game implements DataTransferObject {
    private long id;
    private String gameName;
    private int minBetAmt;
    private int maxBetAmt;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getMinBetAmt() {
        return minBetAmt;
    }

    public void setMinBetAmt(int minBetAmt) {
        this.minBetAmt = minBetAmt;
    }

    public int getMaxBetAmt() {
        return maxBetAmt;
    }

    public void setMaxBetAmt(int maxBetAmt) {
        this.maxBetAmt = maxBetAmt;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameName='" + gameName + '\'' +
                ", minBetAmt=" + minBetAmt +
                ", maxBetAmt=" + maxBetAmt +
                '}';
    }
}
