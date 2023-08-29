package models.treasuremap;

public class Mountain {
    private Coordinates coordinates;

    public Mountain(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

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
