package models.treasuremap;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import models.Adventurer;
import models.TreasureMap;

public class Coordinates {
    private int positionH;
    private int positionV;

    public Coordinates(int positionH, int positionV) {
        this.positionH = positionH;
        this.positionV = positionV;
    }

    // methods

    // check if coordinates within the map limits
    public boolean isOutOfBounds(TreasureMap treasureMap) {
       return ( positionH < 0 || positionH > (treasureMap.getHCells() -1) || 
                positionV < 0 || positionV > (treasureMap.getVCells() -1));
    }

    // check if coordinates a mountain
    public boolean hasMountain(TreasureMap treasureMap) {
        boolean coordinatesHasMountain  = false;
        Set<Mountain> mountains         = treasureMap.getMountains();
        for(Mountain mountain : mountains) {
            if(mountain.getCoordinates().equals(this)){
                coordinatesHasMountain = true;
                break;
            };
        }
        return coordinatesHasMountain;
    }

    // check if coordinates occupied by an adventurer, if so return them
    public Optional<Adventurer> findAdventurerByCoordinates(List<Adventurer> adventurers) {
        Optional<Adventurer> maybeAdventurer = Optional.empty();
        for(Adventurer a : adventurers) {
            if(a.getCoordinates().equals(this)){
                maybeAdventurer = Optional.of(a);
                break;
            };
        }
        return maybeAdventurer;
    }

    // Getters/Setters & overriden java methods

    public int getPositionH() {
        return positionH;
    }

    public void setPositionH(int positionH) {
        this.positionH = positionH;
    }

    public int getPositionV() {
        return positionV;
    }

    public void setPositionV(int positionV) {
        this.positionV = positionV;
    }    

    @Override
    public String toString() { 
        return "(h:" + this.positionH + " / v:" + this.positionV + ")";
    }

    @Override // allow comparing 2 Coordinates
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return positionH == that.positionH && positionV == that.positionV;
    }

    @Override // needed to override properly equals()
    public int hashCode() {
        // avoid hash differences for parameters, avoiding issues within collections like HashSet
        return Objects.hash(positionH, positionV);
    }

}
