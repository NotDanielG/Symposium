package main;

import gui.components.MovingComponent;

public class Player extends MovingComponent {
	private int z;
	public Player(int x, int y, int w, int h) {
		super(x, y, w, h);
		z = x;
	}
}
