package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gui.Screen;
import gui.components.Action;
import gui.components.Graphic;
import gui.components.Visible;

public class ScreenGame extends Screen implements KeyListener, Runnable{
	private List<Enemy> stuff;
	private boolean gameRunning;
	private Player player;
	private List<Key> keyCommands;
	public ScreenGame(int width, int height) {
		super(width, height);
		gameRunning = true;
		Thread x = new Thread(this);
		x.start();
	}

	@Override
	public void initObjects(List<Visible> viewObjects) {
		
		keyCommands = new ArrayList<Key>();
		player = new Player(300,300,100,100, "resources/square.png");
		viewObjects.add(player);
		
		Enemy y = new Enemy(700,500,50,50,600,"resources/cactus.png");
		viewObjects.add(y);
		
	}

	@Override
	public KeyListener getKeyListener(){
		return this;
	}
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(!checkDuplicate(keyCode)){
			Key key = new Key();
			switch(keyCode){
			case KeyEvent.VK_W:
				if(!getPlayer().isJump()){
					key.setCode(keyCode);
					key.setAction(new Action(){
						public void act(){
							getPlayer().setJump(true);
						}
					});
					key.act();
				}
				break;
			case KeyEvent.VK_D:
				key.setCode(keyCode);
				key.setAction(new Action(){
					public void act(){
						player.setX(player.getX() + getPlayer().getAcceleration());
					}
				});
				keyCommands.add(key);
				break;
			case KeyEvent.VK_A:
				key.setCode(keyCode);
				key.setAction(new Action(){
					public void act(){
						player.setX(player.getX() - getPlayer().getAcceleration());
					}
				});
				keyCommands.add(key);
				break;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		for(Key key: keyCommands){
			if(key.getCode() == keyCode){
				keyCommands.remove(key);
				break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public Player getPlayer(){
		return player;
	}
	@Override
	public void run() {
		stuff = Collections.synchronizedList(new ArrayList<Enemy>());
		keyCommands = Collections.synchronizedList(new ArrayList<Key>());
		
		while(gameRunning){
			try {
				Thread.sleep(20);
				checkButtonList();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void checkButtonList() {
		for(Key key: keyCommands){
			key.act();
		}
		
	}
	private boolean checkDuplicate(int keyCode){
		for(Key key: keyCommands){
			if(keyCode == key.getCode()){
				return true;
			}
		}
		return false;
	}
}
