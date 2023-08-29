package models;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import models.treasuremap.Coordinates;
import models.treasuremap.Mountain;
import models.treasuremap.Treasure;

public class TreasureMap {
    private int hCells;
    private int vCells;
    private Set<Mountain> mountains = Collections.emptySet();
    private Set<Treasure> treasures = Collections.emptySet();

    public TreasureMap(int hCells, int vCells, Set<Mountain> mountains, Set<Treasure> treasures) {
        this.hCells = hCells;
        this.vCells = vCells;
        this.mountains = mountains;
        this.treasures = treasures;
    }

    // methods

    public Optional<Treasure> findTreasureByCoordinates(Coordinates treasureCoordinates) {
        // System.out.println("looking for Treasure at Coordinates: " + treasureCoordinates);
        Optional<Treasure> maybeTreasure = Optional.empty();
        for(Treasure treasure : treasures) {
            // System.out.println("coordinatesSearched: " + treasure.getCoordinates());
            if(treasure.getCoordinates().equals(treasureCoordinates)) {
                // System.out.println("treasure found !");
                maybeTreasure = Optional.of(treasure);
                break;
            }
        }
        return maybeTreasure;
    }

    // to remove a Treasure from the Set once its nbOfTreasures reaches 0
    public void deleteTreasure(Treasure treasure) {
        if(treasure.getNbOfTreasures() == 0) {
            Iterator<Treasure> iterator = treasures.iterator();
            while (iterator.hasNext()) {
                Treasure t = iterator.next();
                if (t.equals(treasure)) {
                    iterator.remove();
                }
            }
            System.out.println(
                "Treasure object located at " + treasure.getCoordinates() + " deleted from the map."
            );
        } else if(treasure.getNbOfTreasures() > 0){
            System.err.println("ERROR : trying to remove a Treasure whose nbOfTreasures is above 0");
        } else {
            System.err.println("ERROR : this Treasure has a negative nbOfTreasures !");
        }
    }

    // Getters/Setters

    public int getVCells() {
        return vCells;
    }

    public void setVCells(int vCells) {
        this.vCells = vCells;
    }

    public int getHCells() {
        return hCells;
    }

    public void setHCells(int hCells) {
        this.hCells = hCells;
    }

    public Set<Mountain> getMountains() {
        return mountains;
    }

    public void setMountains(Set<Mountain> mountains) {
        this.mountains = mountains;
    }

    public Set<Treasure> getTreasures() {
        return treasures;
    }

    public void setTreasures(Set<Treasure> treasures) {
        this.treasures = treasures;
    }

    @Override
    public String toString(){
        String mapSizeString = "mapSize = " + "h:" + getHCells() + " x v:" + getVCells();
        String mountainsString = " | mountains ";
        for(Mountain mountain : mountains){
            mountainsString += (mountain + " ");
        }
        String treasuresString = " | treasures ";
        for(Treasure treasure : treasures){
            treasuresString += (treasure + " ");
        }
        return "TreasureMap : " + mapSizeString + mountainsString + treasuresString;
    }
}
