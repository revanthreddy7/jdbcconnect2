package Game.Test.Currency;

import Game.Test.util.DataTransferObject;

public class Currency implements DataTransferObject {
    private long id;
    private String currency;
    private Double multiplier;

    @Override
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }
}
