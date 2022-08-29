package org.sal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.sal.model.Ladder;
import org.sal.model.Player;
import org.sal.model.Snake;
import org.sal.service.DiceService;
import org.sal.service.SnakeAndLadderService;

public class Driver {
	private static final String YES = "Y";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			int noOfSnakes = scanner.nextInt();
			Map<Integer, Integer> snakes = new HashMap<>();
			for (int i = 0; i < noOfSnakes; i++) {
				snakes.put(scanner.nextInt(), scanner.nextInt());
			}
			int noOfLadders = scanner.nextInt();
			
			Map<Integer, Integer> ladders = new HashMap<>();
			for (int i = 0; i < noOfLadders; i++) {
				ladders.put(scanner.nextInt(), scanner.nextInt());
			}
			int noOfPlayers = scanner.nextInt();
			List<Player> players = new ArrayList<Player>();
			for (int i = 0; i < noOfPlayers; i++) {
				players.add(new Player(scanner.next(), scanner.nextInt()));
			}
			System.out.println("Do you want to enter Dice rolling value manually, type M else type A ? : ");
			DiceService.setDiceRollingType(scanner.next());
			DiceService.scanner = scanner;

			SnakeAndLadderService snakeAndLadderService = new SnakeAndLadderService(16);
			System.out.println("Should Allow Multiple Dice Roll On Six Y/N : ");
			if (YES.equalsIgnoreCase(scanner.next())) {
				snakeAndLadderService.setShouldAllowMultipleDiceRollOnSix(true);
				System.out.println("Enter no. of dices : ");
				snakeAndLadderService.setNoOfDices(scanner.nextInt());
			}
			snakeAndLadderService.setPlayers(players);
			snakeAndLadderService.setSnakes(snakes);
			snakeAndLadderService.setLadders(ladders);
			snakeAndLadderService.startGame();// Game start
		} catch (Exception ex) {
			System.out.println("Exception : " + ex);
		}
	}

}
