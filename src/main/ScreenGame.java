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
	private List<Platform> platforms;
	private Platform[][] arrayP;
	private List<Integer> zList;
	private List<Integer> yList;
	
	private int currentSection;
	
	
	public ScreenGame(int width, int height) {
		super(width, height);
		arrayP = new Platform[4][4];
		gameRunning = true;
		Thread x = new Thread(this);
		x.start();
	}

	@Override
	public void initObjects(List<Visible> viewObjects) {
		
		keyCommands = new ArrayList<Key>();
		player = new Player(400,300,50,50, "resources/square.png");
		viewObjects.add(player);
		player.play();
		
//		Platform z = new Platform(350,450, 400, 30, 250, "resources/platform.png");
//		z.setAction(new Action(){
//			public void act() {
//				getPlayer().hitGround((int)z.getY() - player.getHeight());
//				getPlayer().setStart(System.currentTimeMillis());
//				getPlayer().setPlatform(z);
//			}
//		});
//		viewObjects.add(z);
//		z.play();
//		
//		Platform p1 = new Platform(10,570, 500, 30, 500, "resources/platform.png");
//		p1.setAction(new Action(){
//			public void act() {
//				getPlayer().hitGround((int)p1.getY() - player.getHeight());
//				getPlayer().setStart(System.currentTimeMillis());
//				getPlayer().setPlatform(p1);
//			}
//		});
//		viewObjects.add(p1);
//		p1.play();
		
//		Enemy e = new Enemy(450,350,50,50, 450, "resources/triangle.png");
//		e.setAction(new Action(){
//			public void act(){
//				getPlayer().decreaseHP();
//			}
//		});
//		viewObjects.add(e);
//		e.play();
	}
	public void run() {
		stuff = Collections.synchronizedList(new ArrayList<Enemy>());
		platforms = Collections.synchronizedList(new ArrayList<Platform>());
		keyCommands = Collections.synchronizedList(new ArrayList<Key>());
		
		zList = Collections.synchronizedList(new ArrayList<Integer>());
		yList = Collections.synchronizedList(new ArrayList<Integer>());
		int z = 350;
		int y = 450;
		
		for(int i = 0; i < 16; i++){
			addToList(z,y);
			z+=400;
		}
		int idx = 0;
		for(int i = 0; i < arrayP.length; i++){
			for(int j = 0; j < arrayP[i].length; j++){
				Platform platform = new Platform(10,yList.get(idx), 200, 30, zList.get(idx), "resources/platform.png");
				platform.setAction(new Action(){
					public void act() {
						getPlayer().hitGround((int)platform.getY() - player.getHeight());
						getPlayer().setStart(System.currentTimeMillis());
						getPlayer().setPlatform(platform);
					}
				});
				
				
//				arrayP[zList.get(i)/800][i%4] = platform;
				
				addObject(platform);
				platform.play();
				idx++;
//				remove(platform);
				platform.setRunning(false);
			}
		}
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
	@Override
	public KeyListener getKeyListener(){
		return this;
	}
	public void addToList(int z, int y){
		zList.add(z);
		yList.add(y);
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
						player.setZ(player.getZ() + getPlayer().getAcceleration());
					}
				});
				keyCommands.add(key);
				break;
			case KeyEvent.VK_A:
				key.setCode(keyCode);
				key.setAction(new Action(){
					public void act(){
						player.setZ(player.getZ() - getPlayer().getAcceleration());
					}
				});
				keyCommands.add(key);
				break;
			case KeyEvent.VK_SPACE:
				
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
		
	}
	public Player getPlayer(){
		return player;
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
