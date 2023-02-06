package Game.Test.User;

import Game.Test.util.DataTransferObject;

public class User implements DataTransferObject{
    private long id;
    private String username;
    private String name;
    private String password;
    private double wallet_amt;
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", wallet_amt=" + wallet_amt +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWallet_amt() {
        return wallet_amt;
    }

    public void setWallet_amt(double wallet_amt) {
        this.wallet_amt = wallet_amt;
    }


}
