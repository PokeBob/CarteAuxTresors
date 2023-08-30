package models;

import java.util.List;
import java.util.Optional;

public class Game {
    private TreasureMap treasureMap;
    private List<Adventurer> adventurers;

    public Game(TreasureMap treasureMap, List<Adventurer> adventurers) {
        this.treasureMap = treasureMap;
        this.adventurers = adventurers;
    }

    // Methods

    // nbOfRounds determined by largest number of movements existing for one adventurer
    public int findMaxMovementsSize(List<Adventurer> adventurers) {
        Optional<Integer> maxMovementsSize = adventurers.stream()
            .map(adventurer -> adventurer.getMovements().size()) // map to movements' size
            .max(Integer::compareTo); 
            /*
                .max(...) equivalent to .max((size1, size2) -> size1.compareTo(size2))
                comparing sizes, returning a negative, 0, or positive value for each element 
                of the stream, thus allowing max to determine which element of the stream is
                the larger integer. 
                Returns an Optional<Integer>
            */
        return maxMovementsSize.orElseGet(() -> {
            throw new IllegalArgumentException(
                "ERROR : no round for this game as no adventurer was found"
            );
        });
    }

    // Getters/Setters

    public TreasureMap getTreasureMap() {
        return treasureMap;
    }

    public void setTreasureMap(TreasureMap treasureMap) {
        this.treasureMap = treasureMap;
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void setAdventurers(List<Adventurer> adventurers) {
        this.adventurers = adventurers;
    }
}
