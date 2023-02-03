class User{
    private int userid;
    private String name;
    private String username;
    private String password;
    private double wallet_amt;

    public User(int userid, String name, String username, String password, double wallet_amt) {
        this.userid = userid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.wallet_amt = wallet_amt;
    }

    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getWallet_amt() {
        return wallet_amt;
    }
}
