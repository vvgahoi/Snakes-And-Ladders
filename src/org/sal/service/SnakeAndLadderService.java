package org.sal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.IntStream;

import org.sal.model.Ladder;
import org.sal.model.Player;
import org.sal.model.Snake;
import org.sal.model.SnakeAndLadderBoard;

public class SnakeAndLadderService {

	private SnakeAndLadderBoard snakeAndLadderBoard;
	private int initialNumberOfPlayers;
	private Queue<Player> players;

	private int noOfDices;
	private boolean shouldAllowMultipleDiceRollOnSix;

	private Map<Integer, Integer> snakes = new HashMap<>();
	private Map<Integer, Integer> ladders = new HashMap<>();

	private static final int DEFAULT_BOARD_SIZE = 100;
	private static final int DEFAULT_NO_OF_DICES = 1;
	private static final String MAX = "MAX";
	private static final String MIN = "MIN";
	private static final String SUM = "SUM";
	private static final String MS = MAX; // Movement Strategy e.g. SUM/MAX/MIN

	public SnakeAndLadderService(int boardSize) {
		this.snakeAndLadderBoard = new SnakeAndLadderBoard(boardSize);
		this.players = new LinkedList<Player>();
		if (!this.shouldAllowMultipleDiceRollOnSix) {
			this.noOfDices = SnakeAndLadderService.DEFAULT_NO_OF_DICES;
		}
	}

	public SnakeAndLadderService() {
		this(SnakeAndLadderService.DEFAULT_BOARD_SIZE);
	}

	public Map<Integer, Integer> getSnakes() {
		return snakes;
	}

	public void setSnakes(Map<Integer, Integer> snakes) {
		this.snakes = snakes;
	}

	public Map<Integer, Integer> getLadders() {
		return ladders;
	}

	public void setLadders(Map<Integer, Integer> ladders) {
		this.ladders = ladders;
	}

	public void setNoOfDices(int noOfDices) {
		this.noOfDices = noOfDices;
	}

	public void setShouldAllowMultipleDiceRollOnSix(boolean shouldAllowMultipleDiceRollOnSix) {
		this.shouldAllowMultipleDiceRollOnSix = shouldAllowMultipleDiceRollOnSix;
	}

	public void setPlayers(List<Player> players) {
		this.players = new LinkedList<Player>();
		this.initialNumberOfPlayers = players.size();
		Map<String, Integer> playerPieces = new HashMap<String, Integer>();
		for (Player player : players) {
			this.players.add(player);
			playerPieces.put(player.getId(), player.getPosition());
		}
		snakeAndLadderBoard.setPlayerPieces(playerPieces);
	}

	public void setSnakes(List<Snake> snakes) {
		snakeAndLadderBoard.setSnakes(snakes);
	}

	public void setLadders(List<Ladder> ladders) {
		snakeAndLadderBoard.setLadders(ladders);
	}

	private int getNewPositionAfterGoingThroughSnakesAndLadders(int newPosition) {
		int previousPosition;

		do {
			previousPosition = newPosition;
			if (snakes.get(newPosition) != null) {
				if (snakes.get(newPosition) < newPosition) {
					newPosition = snakes.get(newPosition);
				}
			}
			if (ladders.get(newPosition) != null) {
				newPosition = ladders.get(newPosition);
			}
		} while (newPosition != previousPosition);
		return newPosition;
	}

	private void resetPlayer(String playerId, int position) {
		Map<String, Integer> playerPieces = snakeAndLadderBoard.getPlayerPieces();
		playerPieces.forEach((id, pos) -> {
			if (id != playerId && pos == position) {
				playerPieces.put(id, 1);
			}
		});
	}

	private void movePlayer(Player player, int positions) {
		int oldPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
		int newPosition = oldPosition + positions;
		int boardSize = snakeAndLadderBoard.getSize();
		if (newPosition > boardSize) {
			newPosition = oldPosition;
		} else {
			newPosition = getNewPositionAfterGoingThroughSnakesAndLadders(newPosition);
		}
		resetPlayer(player.getId(), newPosition);
		snakeAndLadderBoard.getPlayerPieces().put(player.getId(), newPosition);
		System.out.println(
				player.getName() + " rolled a " + positions + " and moved from " + oldPosition + " to " + newPosition);
	}

	private int getTotalValueAfterDiceRolls() {
		List<Integer> list = new ArrayList<>();
		if (this.shouldAllowMultipleDiceRollOnSix) {
			for (int i = 1; i < noOfDices; i++) {
				list.add(DiceService.roll());
				
			}
			//IntStream.range(1, noOfDices).forEachOrdered(n -> list.add(DiceService.roll()));
			if (MS.equals(SUM)) {
				return list.stream().reduce(0, Integer::sum);
			} else if (MS.equals(MAX)) {
				return list.stream().mapToInt(n -> n).max().orElse(DiceService.roll());
			} else if (MS.equals(MIN)) {
				return list.stream().mapToInt(n -> n).min().orElse(DiceService.roll());
			}
		}
		return DiceService.roll();
	}

	private boolean hasPlayerWon(Player player) {
		int playerPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
		int winningPosition = snakeAndLadderBoard.getSize();
		return playerPosition == winningPosition;
	}

	private boolean isGameCompleted() {
		int currentNumberOfPlayers = players.size();
		return currentNumberOfPlayers < initialNumberOfPlayers;
	}

	public void startGame() {
		while (!isGameCompleted()) {
			int totalDiceValue = getTotalValueAfterDiceRolls();
			Player currentPlayer = players.poll();
			movePlayer(currentPlayer, totalDiceValue);
			if (hasPlayerWon(currentPlayer)) {
				System.out.println(currentPlayer.getName() + " wins the game");
				snakeAndLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
			} else {
				players.add(currentPlayer);
			}
		}
	}
}
