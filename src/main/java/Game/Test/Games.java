package Game.Test;

import java.util.*;
public class Games {
    private double betAmt;
    private final int betType;
    private int luckyNumber;
    Scanner sc = new Scanner(System.in);

    public Games(double betAmt, int betType){
        this.betAmt = betAmt;
        this.betType = betType;
    }
    public int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    public double playRoulette(){

        int rand = getRandom(0,36);
        int rouletteVal = rand%2;
        //even bet
        if (betType == 1 && rouletteVal == 0){
            return betAmt*2;
        }
        //odd bet
        else if (betType == 2 && rouletteVal == 1) {
            return betAmt*2;
        }
        else if (betType == 3){
            System.out.println("Please Enter the lucky number you wish to bet on");
            this.luckyNumber = sc.nextInt();

            if (luckyNumber>36 || luckyNumber<0){
                this.playRoulette();
            }
            //Number Bet
            if(luckyNumber == rand){
                return betAmt*10;
            }
        }

        return 0;
    }
    public double playDice(){

        int diceOne = getRandom(0,6);
        int diceTwo = getRandom(0,6);

        int diceVal = (diceOne + diceTwo)%2;

        //even sum bet
        if (betType == 1 && diceVal == 0){
            return betAmt*2;
        }
        // odds sum bet
        else if (betType == 2 && diceVal == 1) {
            return betAmt*2;
        }
        // sum bet
        else if (betType == 3){
            System.out.println("Please Enter the first dice number: ");
            int d1 = sc.nextInt();
            System.out.println("Please Enter the second dice number: ");
            int d2 = sc.nextInt();
            this.luckyNumber = d1+d2;
            if (luckyNumber>12 || luckyNumber<2){
                this.playDice();
            }
            if(luckyNumber == diceOne+diceTwo){
                return betAmt*10;
            }
        }

        return 0;
    }
}
