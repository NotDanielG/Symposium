package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gui.Screen;
import gui.components.Graphic;
import gui.components.Visible;

public class ScreenGame extends Screen implements KeyListener, Runnable{
	private List<Enemy> stuff;
	private boolean gameRunning;
	public ScreenGame(int width, int height) {
		super(width, height);
		gameRunning = true;
	}

	@Override
	public void initObjects(List<Visible> viewObjects) {
		Graphic x = new Graphic(100,100, "resources/square.png");
		viewObjects.add(x);
		Enemy y = new Enemy(700,500,50,50,600,"resources/cactus.png");
		viewObjects.add(y);

	}

	@Override
	public KeyListener getKeyListener(){
		return this;
	}
	public void keyPressed(KeyEvent e) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		stuff = Collections.synchronizedList(new ArrayList<Enemy>());
		while(gameRunning){
			
		}
	}

}
