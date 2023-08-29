package models;

import java.util.List;
import java.util.Optional;

import models.treasuremap.Coordinates;
import models.treasuremap.Treasure;

public class Adventurer {

    public enum Orientation { NORTH, SOUTH, EAST, WEST }
    public enum Movement { FORWARD, LEFT, RIGHT}


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
            case NORTH: 
                setOrientation(Orientation.WEST);     
                break;
            case SOUTH: 
                setOrientation(Orientation.EAST);     
                break;
            case WEST: 
                setOrientation(Orientation.SOUTH);    
                break;
            case EAST: 
                setOrientation(Orientation.NORTH);    
                break;
            default: System.err.println("invalid orientation found after turning left");
        };
        System.out.println(name + " turns left to the " + orientation);
    }

    public void turnRight() {
        switch(orientation) {
            case NORTH: 
                setOrientation(Orientation.EAST);
                break;
            case SOUTH: 
                setOrientation(Orientation.WEST);
                break;
            case WEST: 
                setOrientation(Orientation.NORTH);
                break;
            case EAST: 
                setOrientation(Orientation.SOUTH);
                break;
            default: System.err.println("invalid orientation found after turning right");
        };
        System.out.println(name + " turns right to the " + orientation);
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
        
        System.out.println(name 
            + " tries to move forward to the " + orientation 
            + " from " + originalCoordinates 
            + " to " + newCoordinates
        );

        // update if new coordinates neither outOfBounds nor mountain 
        if(!newCoordinates.isOutOfBounds(treasureMap) && !newCoordinates.hasMountain(treasureMap)) {
            setCoordinates(newCoordinates);
            System.out.println(name + " moved succesfully");
            // check if adventurer found new treasure
            if(newCoordinates != originalCoordinates) {
                Optional<Treasure> maybeTreasure = treasureMap.findTreasureByCoordinates(newCoordinates);
                maybeTreasure.ifPresent( t -> {  
                    setTreasuresFound(getTreasuresFound() + 1); // increase treasuresFound
                    t.setNbOfTreasures(t.getNbOfTreasures() - 1); // decrease nbOfTreasure
                    System.out.println(name + " found a treasure !");
                        System.out.println(
                            "Treasures left at " + t.getCoordinates() + " : " + t.getNbOfTreasures() 
                        );
                    if(!(t.getNbOfTreasures() > 0)) {
                        treasureMap.deleteTreasure(t); // delete from map if no more treasures here
                    }
                });
            }
        } else if(newCoordinates.isOutOfBounds(treasureMap)) {
            System.err.println(name + " tried to move OutOfBounds => movement cancelled");
        } else if(newCoordinates.hasMountain(treasureMap)) {
            System.err.println(name + " tried to move towards a Mountain => movement cancelled");
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
