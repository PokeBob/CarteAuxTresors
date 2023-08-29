package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    // launch a test with fixed data
    public void launchTestGame() {

        Set<Mountain> mountains = new HashSet<Mountain>(){{
            add(new Mountain(new Coordinates(2, 1)));
            add(new Mountain(new Coordinates(1, 2)));
        }};

        Set<Treasure> treasures = new HashSet<Treasure>(){{
            add(new Treasure(new Coordinates(0, 0), 1));
            add(new Treasure(new Coordinates(1, 0), 3));
        }};
        

		TreasureMap treasureMap = new TreasureMap(3, 4, mountains, treasures);

		Coordinates a1FirstCoordinates = new Coordinates(1, 1);
		List<Movement> a1Movements = new ArrayList();
		a1Movements.add(Movement.FORWARD);
		a1Movements.add(Movement.FORWARD);
		a1Movements.add(Movement.FORWARD);
		a1Movements.add(Movement.LEFT);
		a1Movements.add(Movement.FORWARD);
		a1Movements.add(Movement.LEFT);
		a1Movements.add(Movement.FORWARD);
		a1Movements.add(Movement.RIGHT);
		a1Movements.add(Movement.FORWARD);
        a1Movements.add(Movement.LEFT);
		a1Movements.add(Movement.FORWARD);
        a1Movements.add(Movement.LEFT);
		a1Movements.add(Movement.FORWARD);

		Adventurer a1 = new Adventurer("Lara", a1FirstCoordinates, Orientation.NORTH, a1Movements, 0);

		Game game = new Game(treasureMap, a1, 0);
		
        System.out.println("--------- BEGINNING ------------------");
        System.out.println("");
        System.out.println(game.getTreasureMap());
		System.out.println(game.getAdventurer());
        
		
        playRounds(game); // play all rounds of the game
        System.out.println("--------- GAME OVER ------------------");
        System.out.println("");
        System.out.println("END treasureMap infos = " + game.getTreasureMap());
        System.out.println("");
		System.out.println("END adventurer infos = " + game.getAdventurer());
        System.out.println("");
        System.out.println("--------------------------------------");
	}

    // play all rounds until end of the adventurer's movements 
    public void playRounds(Game game) {
        // calculate expected number of rounds
        int nbOfRounds = game.getAdventurer().getMovements().size();
        System.out.println("nbOfRounds = " + nbOfRounds);
        System.out.println("");
        for(int round = 0 ; round < nbOfRounds; round++) {
            playRound(game, round);
        }    
    }


    // play the current round of the game
	public void playRound(Game game, int round) {
        System.out.println("///// Game Round n°" + round + " /////");
        
        game.setCurrentRound(round); // update game round for potential use elsewhere in code
        moveAdventurer(game.getTreasureMap(), game.getAdventurer(), game.getCurrentRound());
        
        // System.out.println("");
        // System.out.println("treasureMap updated infos = " + game.getTreasureMap());
        // System.out.println("");
		// System.out.println("adventurer updated infos = " + game.getAdventurer());
        System.out.println("");
	}
    
    // move an Adventurer forward, left, or right based on their movement for this round
    public Adventurer moveAdventurer(
        TreasureMap treasureMap,
        Adventurer adventurer, 
        int currentRound
    ) {
        Movement currentMovement = adventurer.getMovements().get(currentRound);
        switch(currentMovement) {
            case FORWARD:
                adventurer.moveForward(treasureMap);
                break;
            case LEFT:
                adventurer.turnLeft();
                break;
            case RIGHT:
                adventurer.turnRight();
                break;
            default:
            System.err.println("invalid movement found");
        }
        return adventurer;
    }
}
