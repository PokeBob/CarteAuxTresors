package models;

import java.util.List;
import java.util.Optional;

import models.treasuremap.Coordinates;
import models.treasuremap.Treasure;

public class Adventurer {

    public enum Orientation { NORTH, SOUTH, EAST, WEST }
    public enum Movement { A, G, D}


    private String name;
    private Coordinates coordinates;
    private Orientation orientation;
    private List<Movement> movements; // ArrayList probably best. Even though we won't touch it once it's filled, we'll need to add chars turned into Movement one by one when reading the file
    private int treasuresFound = 0;
    
    public Adventurer(
        String name, 
        Coordinates coordinates,
        Orientation orientation,
        List<Movement> movements,
        int treasuresFound
    ) {
        this.name = name;
        this.coordinates = coordinates;
        this.orientation = orientation;
        this.movements = movements;
        this.treasuresFound = treasuresFound;
    }


    // methods

    public void turnLeft() {
        switch(orientation) {
            case NORTH  : setOrientation(Orientation.WEST);
            case SOUTH  : setOrientation(Orientation.EAST);
            case WEST   : setOrientation(Orientation.SOUTH);
            case EAST   : setOrientation(Orientation.NORTH);
            default     : System.err.println("invalid orientation found");
        };
    }

    public void turnRight() {
        switch(orientation) {
            case NORTH  : setOrientation(Orientation.EAST);
            case SOUTH  : setOrientation(Orientation.WEST);
            case WEST   : setOrientation(Orientation.NORTH);
            case EAST   : setOrientation(Orientation.SOUTH);
            default     : System.err.println("invalid orientation found");
        };
    }

    public void moveForward(TreasureMap treasureMap) {
        int positionH = coordinates.getPositionH();
        int positionV = coordinates.getPositionV();

        Coordinates originalCoordinates = coordinates;

        // Define new coordinates expected for the adventurer
        Coordinates newCoordinates;
        switch(orientation) {
            case NORTH: 
                newCoordinates = new Coordinates(positionH, positionV - 1);
                break;
            case SOUTH: 
                newCoordinates = new Coordinates(positionH, positionV + 1);
                break;
            case WEST: 
                newCoordinates = new Coordinates(positionH - 1, positionV);
                break;
            case EAST: 
                newCoordinates = new Coordinates(positionH + 1, positionV);
                break;
            default: 
                System.err.println("invalid orientation found");
                newCoordinates = originalCoordinates;
        };
        // System.out.println("original coordinates = " + originalCoordinates);
        // System.out.println("new coordinates = " + newCoordinates);
        
        // update coordinates if new coordinates are neither outOfBounds nor a mountain 
        if(!newCoordinates.isOutOfBounds(treasureMap) && !newCoordinates.hasMountain(treasureMap)) {
            setCoordinates(newCoordinates);
            // check if the adventurer found a new treasure
            if(newCoordinates != originalCoordinates) {
                // System.out.println("newCoordinates are confirmed new");
                Optional<Treasure> maybeTreasure = treasureMap.findTreasureByCoordinates(newCoordinates);

                maybeTreasure.ifPresent( t -> {  
                    setTreasuresFound(getTreasuresFound() + 1); // update treasuresFound
                    t.setNbOfTreasures(t.getNbOfTreasures() - 1); // update Treasure's nbOfTreasures
                });

            }
        } else if(newCoordinates.isOutOfBounds(treasureMap)) {
            System.err.println(this.name+" tries to move OutOfBounds => movement cancelled");
        } else if(newCoordinates.hasMountain(treasureMap)) {
            System.err.println(this.name+" tries to move towards a Mountain => movement cancelled");
        }
    }


    // Getters/Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

    public int getTreasuresFound() {
        return treasuresFound;
    }

    public void setTreasuresFound(int treasuresFound) {
        this.treasuresFound = treasuresFound;
    }

    @Override
    public String toString(){
        return "Adventurer("
        + getName() 
        + " | " + getCoordinates() 
        + " | " + getOrientation()
        + " | treasuresFound = " + getTreasuresFound()
        + ")";
    }
}
