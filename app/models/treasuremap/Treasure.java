package models.treasuremap;

public class Treasure {
    private Coordinates coordinates;
    private int nbOfTreasures;

    public Treasure(Coordinates coordinates, int nbOfTreasures) {
        this.coordinates = coordinates;
        this.nbOfTreasures = nbOfTreasures;
    }

    // Methods

    public String buildEndResultString() {
        return 
            "T - " + coordinates.getPositionH() + " - " + coordinates.getPositionV() 
            + " - " + nbOfTreasures;

    }

    // Getters/Setters & overriden java methods

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getNbOfTreasures() {
        return nbOfTreasures;
    }

    public void setNbOfTreasures(int nbOfTreasures) {
        this.nbOfTreasures = nbOfTreasures;
    }

    @Override
    public String toString(){
        return "Treasure(" + coordinates + " | nbOfTreasures = " + nbOfTreasures + ")";
    }
}
