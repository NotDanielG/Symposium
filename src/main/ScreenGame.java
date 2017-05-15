package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gui.Screen;
import gui.components.Action;
import gui.components.Graphic;
import gui.components.Visible;

public class ScreenGame extends Screen implements KeyListener, MouseListener,Runnable{
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
		player = new Player(400,400,50,50, "resources/square.png");
		viewObjects.add(player);
		
		Platform z = new Platform(350,450, 400, 30, 500, "resources/platform.png");
		z.setAction(new Action(){
			public void act() {
				getPlayer().hitGround((int)z.getY() - player.getHeight());
				getPlayer().setStart(System.currentTimeMillis());
				getPlayer().setPlatform(z);
			}
		});
		viewObjects.add(z);
		
		Platform p1 = new Platform(10,570, 500, 30, 500, "resources/platform.png");
		p1.setAction(new Action(){
			public void act() {
				getPlayer().hitGround((int)p1.getY() - player.getHeight());
				getPlayer().setStart(System.currentTimeMillis());
				getPlayer().setPlatform(p1);
			}
		});
		viewObjects.add(p1);
		
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
				if(!getPlayer().isJump() && getPlayer().getVy() < 1.5){
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

	public MouseListener getMouseListener(){
		return this;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("x: " + e.getX() +"y: " + e.getY());
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
