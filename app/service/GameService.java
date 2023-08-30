package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import models.Adventurer;
import models.Adventurer.Movement;
import models.Adventurer.Orientation;
import models.Game;
import models.TreasureMap;
import models.treasuremap.Coordinates;
import models.treasuremap.Mountain;
import models.treasuremap.Treasure;

// use play logger instead of java errors // import play.Logger;

@Singleton
public class GameService {
    
    // launch a test with fixed data mimicking what would be fetched from a file
    public void launchTestGame() {
        List<String> entryFileAsStrings = new ArrayList<>();
        // --\/-- MODIFY HERE FOR ENTRY FILE --\/--
        entryFileAsStrings.add("C - 4 - 5");
        entryFileAsStrings.add("M - 2 - 1");
        entryFileAsStrings.add("M - 1 - 2");
        entryFileAsStrings.add("T - 0 - 0 - 2");
        entryFileAsStrings.add("T - 1 - 0 - 4");
        entryFileAsStrings.add("T - 1 - 4 - 5");
        entryFileAsStrings.add("A - Lara - 1 - 1 - N - ADAGGAAGADDADA");
        entryFileAsStrings.add("A - Indiana - 2 - 2 - E - AAGAGADAGAAA");
        entryFileAsStrings.add("A - Nathan - 3 - 3 - S - GGA");
        // --/\-- MODIFY HERE FOR ENTRY FILE --/\--
        readFileAndGenerateExitFile(entryFileAsStrings);
    }

    // read entry fileAsStrings, process game, and generate exit file
    public void readFileAndGenerateExitFile(List<String> fileAsStrings) {
        // extract Game object from file
        Game game = extractGameDataFromFile(fileAsStrings);
        
        showBeginningLogs(game);
        
        // play all rounds of the game
        playRounds(game);

        showGameOverLogs(game);

        // build end result strings for the exit file
        List<String> endResultStrings = generateEndResultListOfStrings(game);
        
        showFileLogs(fileAsStrings, endResultStrings); // sum up entry & exit files
    }

    private List<String> generateEndResultListOfStrings(Game game) {
        TreasureMap treasureMap = game.getTreasureMap();

        String mapString = treasureMap.buildEndResultMapString();

        List<String> mountainsStrings = treasureMap.getMountains().stream()
            .map(m -> m.buildEndResultString())
            .collect(Collectors.toList());

        List<String> treasuresStrings = treasureMap.getTreasures().stream()
            .map(m -> m.buildEndResultString())
            .collect(Collectors.toList());
        
        List<String> adventurersStrings = game.getAdventurers().stream()
            .map(m -> m.buildEndResultString())
            .collect(Collectors.toList());

        // build final list of Strings
        List<String> endResult  = new ArrayList<>();
        endResult.add(mapString);
        for(String m : mountainsStrings) {
            endResult.add(m);
        }
        for(String t : treasuresStrings) {
            endResult.add(t);
        }
        for(String a : adventurersStrings) {
            endResult.add(a);
        }

        return endResult;
    }    

    // play all rounds until end of the adventurer's movements 
    private void playRounds(Game game) {
        // calculate expected number of rounds
        int nbOfRounds = game.findMaxMovementsSize(game.getAdventurers());
        System.out.println("nbOfRounds = " + nbOfRounds);
        System.out.println("");
        for(int round = 0 ; round < nbOfRounds; round++) {
            playRound(game, round);
        }    
    }

    // play the current round of the game
	private void playRound(Game game, int round) {
        System.out.println("///// Game Round n°" + round + " /////");
                
        List<Adventurer> adventurers = game.getAdventurers();
        for(Adventurer a : adventurers) {
            System.out.println("> " + a.getName());
            
            List<Adventurer> otherAdventurers = new ArrayList<>(adventurers);
            otherAdventurers.remove(a);
            
            moveAdventurer(game.getTreasureMap(), a, otherAdventurers, round);
            
            System.out.println("");
        }
	}
    
    // move an Adventurer forward, left, or right based on their movement for this round
    private void moveAdventurer(
        TreasureMap treasureMap,
        Adventurer adventurer, 
        List<Adventurer> otherAdventurers,
        int round
    ) {
        Optional<Movement> maybeRoundMovement = Optional.ofNullable(adventurer.getMovements())
            .flatMap( movements -> 
                movements.size() > round ? Optional.of(movements.get(round)) : Optional.empty()
            );

        if(maybeRoundMovement.isPresent()) {
            switch(maybeRoundMovement.get()) {
                    case FORWARD:
                        adventurer.moveForward(treasureMap, otherAdventurers);
                        break;
                    case LEFT:
                        adventurer.turnLeft();
                        break;
                    case RIGHT:
                        adventurer.turnRight();
                        break;
                    default:
                        throw new IllegalArgumentException("invalid movement found");
                }
        } else {
            System.out.println(adventurer.getName() + " has no movement for this round");
        }
    }


    /* -- METHODS TO EXTRACT DATA FROM FILE STRINGS -- */

    private Game extractGameDataFromFile(List<String> fileAsStrings) {
        // separate Strings by type
        List<String> mapStrings         = new ArrayList<>();
        List<String> mountainsStrings   = new ArrayList<>();
        List<String> treasuresStrings   = new ArrayList<>();
        List<String> adventurersStrings = new ArrayList<>();

        for(String s : fileAsStrings) {
            String cleanString = s.trim();
            if(cleanString.charAt(0) == 'C') {
                mapStrings.add(cleanString);
            }
            if(cleanString.charAt(0) == 'M') {
                mountainsStrings.add(cleanString);
            }
            if(cleanString.charAt(0) == 'T') {
                treasuresStrings.add(cleanString);
            }
            if(cleanString.charAt(0) == 'A') {
                adventurersStrings.add(cleanString);
            }
        }
        TreasureMap treasureMap = extractAndBuildTreasureMap(
            mapStrings, 
            mountainsStrings,
            treasuresStrings
        );
        List<Adventurer> adventurers    = extractAdventurersFromStrings(adventurersStrings);
        
        return new Game(treasureMap, adventurers);
    }

    // extract treasureMap data
    private TreasureMap extractAndBuildTreasureMap(
        List<String> mapStrings, 
        List<String> mountainsStrings, 
        List<String> treasuresStrings
    ) {
        // extract mapData
        int hCells = 0;
        int vCells = 0;
        if(mapStrings.size() == 1) {
            String[] mapInfos   = mapStrings.get(0).split("-");
            hCells              = Integer.parseInt(mapInfos[1].trim());
            vCells              = Integer.parseInt(mapInfos[2].trim());
        } else if(mapStrings.size() > 1) {
            throw new IllegalArgumentException(
                "ERROR : more than one line corresponding to the map found !"
            );
        } else {
            throw new IllegalArgumentException(
                "ERROR : no line corresponding to the map found !"
            );
        }
        // extract other data
        Set<Mountain> mountains = extractMountainsFromStrings(mountainsStrings);
        Set<Treasure> treasures = extractTreasuresFromStrings(treasuresStrings);

        return new TreasureMap(hCells, vCells, mountains, treasures);
    }

    private Set<Mountain> extractMountainsFromStrings(List<String> mountainsStrings) {
        Set<Mountain> mountains = new HashSet<>();
        for(String m : mountainsStrings) {
            Mountain mountain = extractMountainFromString(m);
            mountains.add(mountain);
        }
        return mountains;
    }

    private Mountain extractMountainFromString(String mountainString) {
        String[] mountainInfos  = mountainString.split("-");
        int positionH           = Integer.parseInt(mountainInfos[1].trim());
        int positionV           = Integer.parseInt(mountainInfos[2].trim());
        return new Mountain(new Coordinates(positionH, positionV));
    }

    private Set<Treasure> extractTreasuresFromStrings(List<String> treasuresStrings) {
        Set<Treasure> treasures = new HashSet<>();
        for(String t : treasuresStrings) {
            Treasure treasure = extractTreasureFromString(t);
            treasures.add(treasure);
        }
        return treasures;
    }
    
    private Treasure extractTreasureFromString(String treasureString) {
        String[] treasureInfos  = treasureString.split("-");
        int positionH           = Integer.parseInt(treasureInfos[1].trim());
        int positionV           = Integer.parseInt(treasureInfos[2].trim());
        int nbOfTreasures       = Integer.parseInt(treasureInfos[3].trim());
        return new Treasure(new Coordinates(positionH, positionV), nbOfTreasures);
    }

    private List<Adventurer> extractAdventurersFromStrings(List<String> adventurersStrings) {
        List<Adventurer> adventurers = new ArrayList<>();
        for(String a : adventurersStrings) {
            Adventurer adventurer = extractAdventurerFromString(a);
            adventurers.add(adventurer);
        }
        return adventurers;
    }

    private Adventurer extractAdventurerFromString(String adventurerString) {
        String[] adventurerInfos    = adventurerString.split("-");
        // name
        String name                 = adventurerInfos[1].trim();
        // Coordinates
        int positionH               = Integer.parseInt(adventurerInfos[2].trim());
        int positionV               = Integer.parseInt(adventurerInfos[3].trim());
        // Orientation
        String orientationString    = adventurerInfos[4].trim();
        Orientation orientation;
        if(orientationString.equals("N")) {
            orientation = Orientation.NORTH;
        } 
        else if(orientationString.equals("S")) {
            orientation = Orientation.SOUTH;
        } 
        else if(orientationString.equals("E")) {
            orientation = Orientation.EAST;
        } 
        else if(orientationString.equals("O")) {
            orientation = Orientation.WEST;
        } else {
            throw new IllegalArgumentException(
                "Orientation '" + orientationString + "' found in file does not exist"
            );
        }
        // Movements
        String movementsString      = adventurerInfos[5].trim();
        char[] movementsChars       = movementsString.toCharArray();
        List<Movement> movements    = new ArrayList<>();
        for(char movement : movementsChars) {
            if(movement == 'A') {
                movements.add(Movement.FORWARD);
            }
            else if(movement == 'D') {
                movements.add(Movement.RIGHT);
            }
            else if(movement == 'G') {
                movements.add(Movement.LEFT);
            } else {
                throw new IllegalArgumentException(
                    "Movement '" + movement + "' found in file does not exist"
                );
            }
        }
        return new Adventurer(
            name, 
            new Coordinates(positionH, positionV), 
            orientation, 
            movements,
            0
        );
    }


    /* -- LOG METHODS (to avoid polluting code) -- */

    private void showBeginningLogs(Game game) {
        System.out.println("--------- BEGINNING ------------------");
        System.out.println("");
        System.out.println(game.getTreasureMap());
        
        for(Adventurer a : game.getAdventurers()) {
            System.out.println("> " + a.getName());
            System.out.println(a);
        }
    }

    private void showGameOverLogs(Game game) {
        System.out.println("--------- GAME OVER ------------------");
        System.out.println("");
        System.out.println("END treasureMap infos = " + game.getTreasureMap());
        System.out.println("");
        for(Adventurer a : game.getAdventurers()) {
		    System.out.println("END adventurer " + a.getName() + " infos = " + a);
        }
        System.out.println("");
        System.out.println("--------------------------------------");
        System.out.println("");
    }

    private void showFileLogs(List<String> fileAsStrings, List<String> endResultStrings) {
        System.out.println("--------- ENTRY FILE ------------------");
        for(String s : fileAsStrings) {
		    System.out.println(s);
        }
        System.out.println("--------------------------------------");
        System.out.println("");
        System.out.println("--------- EXIT FILE ------------------");
        for(String s : endResultStrings) {
		    System.out.println(s);
        }
        System.out.println("--------------------------------------");
    }
}
