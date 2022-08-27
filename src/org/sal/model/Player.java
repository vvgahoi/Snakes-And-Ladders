package org.sal.model;

import java.util.UUID;

public class Player {

	private String name;
	private String id;
	private int position;

	public Player(String name, int position) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

}