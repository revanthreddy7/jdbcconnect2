package Game.Test.Transaction;

import Game.Test.util.DataTransferObject;

import java.util.Date;


public class Transaction implements DataTransferObject {
    private long id;
    private long gameId;
    private long userId;
    private String dateTime;
    private double betAmount;
    private double winAmount;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(double winAmount) {
        this.winAmount = winAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", userId=" + userId +
                ", dateTime=" + dateTime +
                ", betAmount=" + betAmount +
                ", winAmount=" + winAmount +
                '}';
    }
}
