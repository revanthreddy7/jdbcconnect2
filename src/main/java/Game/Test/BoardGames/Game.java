package Game.Test.BoardGames;

import java.util.Random;

public abstract class Game {
    public int throwDice(int max, int min){
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public abstract void startGame();
    public abstract void endGame();

}
