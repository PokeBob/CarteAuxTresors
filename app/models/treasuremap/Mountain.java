package models.treasuremap;

public class Mountain {
    private Coordinates coordinates;

    public Mountain(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    // Methods

    public String buildEndResultString() {
        return "M - " + coordinates.getPositionH() + " - " + coordinates.getPositionV();
    }

    // Getters/Setters & overriden java methods

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString(){
        return "Mountain(" + coordinates + ")";
    }
}
