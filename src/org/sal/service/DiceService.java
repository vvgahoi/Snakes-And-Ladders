package org.sal.service;

import java.util.Random;
import java.util.Scanner;

public class DiceService {

	public static Scanner scanner;
	private static String diceRollingType = "A";// Manual/Automatic
	private static final String MANUAL = "M";// Manual

	public static void setDiceRollingType(String diceRollingType) {
		DiceService.diceRollingType = diceRollingType;
	}

	public static int roll() {
		if (diceRollingType.equalsIgnoreCase(MANUAL)) {
			System.out.print("Enter dice value : ");
			return scanner.nextInt();
		}
		return new Random().nextInt(6) + 1;
	}
}
