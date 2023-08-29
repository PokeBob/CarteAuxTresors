package models;

public class Game {
    private TreasureMap treasureMap;
    private Adventurer adventurer; // private Sequence<Adventurer> adventurers
    private int currentRound = 0;

    public Game(
        TreasureMap treasureMap, 
        Adventurer adventurer, 
        int currentRound
    ) {
        this.treasureMap = treasureMap;
        this.adventurer = adventurer;
        this.currentRound = currentRound;
    }

    // Getters/Setters

    public TreasureMap getTreasureMap() {
        return treasureMap;
    }

    public void setTreasureMap(TreasureMap treasureMap) {
        this.treasureMap = treasureMap;
    }

    public Adventurer getAdventurer() {
        return adventurer;
    }

    public void setAdventurer(Adventurer adventurer) {
        this.adventurer = adventurer;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
