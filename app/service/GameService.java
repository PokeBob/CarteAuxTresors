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

		Coordinates a1FirstCoordinates = new Coordinates(0, 3);
		List<Movement> a1Movements = new ArrayList();
		a1Movements.add(Movement.A);
		a1Movements.add(Movement.G);
		a1Movements.add(Movement.A);

		Adventurer a1 = new Adventurer("Lara", a1FirstCoordinates, Orientation.SOUTH, a1Movements, 0);

		Game game = new Game(treasureMap, a1, 0, false);
		
        System.out.println("--------- BEGINNING ------------------");
        System.out.println(game.getTreasureMap());
		System.out.println(game.getAdventurer());
        
		
        playRounds(game); // play all rounds of the game
        if(game.isOver()){
            System.out.println("////////==========////////");
            System.out.println("======= GAME OVER =======");
            System.out.println("////////==========////////");
        } else {
            System.err.println("ERROR : all rounds ended without a proper Game Over");
        }
	}

    // play all rounds of the game until the end of the adventurer's movements 
    // display "GAME OVER" when finished
    public void playRounds(Game game) {
        // calculate expected number of rounds
        int nbOfRounds = game.getAdventurer().getMovements().size();
        for(int i = 0 ; i < nbOfRounds - 1 ; i++) {
            System.out.println("///////////////////////////");
            playRound(game);
            System.out.println("///// Game Round n°" + i + " /////");
        }
        game.setGameOver(true);
        
    }


    // play the current round of the game
	public void playRound(Game game) {
		System.out.println("=====================");
		
        moveAdventurer(game.getTreasureMap(), game.getAdventurer(), game.getCurrentRound());
		
        System.out.println("ROUND " + game.getCurrentRound() + " ==> ");
        System.out.println("treasureMap updated = " + game.getTreasureMap());
		System.out.println("adventurer updated = " + game.getAdventurer());
		
        game.setCurrentRound(game.getCurrentRound() + 1);
		
        System.out.println("NEXT ROUND = " + game.getCurrentRound());
		System.out.println("=====================");
	}
    
    // move an Adventurer forward, left, or right based on their movement for this round
    public Adventurer moveAdventurer(
        TreasureMap treasureMap,
        Adventurer adventurer, 
        int currentRound
    ) {
        Movement currentMovement = adventurer.getMovements().get(currentRound);
        switch(currentMovement) {
            case A:
                adventurer.moveForward(treasureMap);
                break;
            case G:
                adventurer.turnLeft();
                break;
            case D:
                adventurer.turnRight();
                break;
            default:
            System.err.println("invalid movement found");
        }
        return adventurer;
    }
}
