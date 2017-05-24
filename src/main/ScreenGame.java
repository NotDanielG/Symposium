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
		arrayP = new Platform[5][3];
		gameRunning = true;
		currentSection = 1;
		Thread x = new Thread(this);
		x.start();
	}
	public void initObjects(List<Visible> viewObjects) {
		
		keyCommands = new ArrayList<Key>();
		player = new Player(350,300,50,50, "resources/square.png");
		viewObjects.add(player);
		player.play();
	}
	public void run() {
		stuff = Collections.synchronizedList(new ArrayList<Enemy>());
		platforms = Collections.synchronizedList(new ArrayList<Platform>());
		keyCommands = Collections.synchronizedList(new ArrayList<Key>());
		
		zList = Collections.synchronizedList(new ArrayList<Integer>());
		yList = Collections.synchronizedList(new ArrayList<Integer>());
		
		int z = 100;
		int y = 450;
		
		for(int i = 0; i < arrayP.length*arrayP[0].length; i++){
			addToList(z,y);
			z+=250;
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
				
				arrayP[i][j] = platform;
				addObject(platform);
				idx++;
				if(j == 1 && i == 0){
					Enemy enemy = new Enemy(10,yList.get(idx) - 50, 50,50,
							zList.get(idx) + 50,"resources/triangle.png");
					enemy.setAction(new Action(){
						public void act(){
							System.out.println("hit");
						}
					});
					addObject(enemy);
					enemy.play();
				
				
				
				
				}
			}
		}
		for(int i = 0; i < 2;i++){
			for(int j = 0; j < arrayP[0].length;j++){
				arrayP[i][j].play();
			}
		}
		while(gameRunning){
			try {
				Thread.sleep(20);
				//currentSection * 800 = lower limit
				//currenSection +1 * 800 = upper limit
				
				if(player.getZ() < currentSection * 800 - 400){
					currentSection--;
					loadSections(currentSection + 1);
				}
				else{
					if(player.getZ() > (currentSection + 1) * 800 - 400){
						currentSection++;
						loadSections(currentSection - 1);
					}
				}
				
				checkButtonList();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	private void loadSections(int previousSection) {
		if(currentSection <= 0){
			for(int a = 0; a < 2; a++){
				for(int b = 0; b < arrayP[0].length; b++){
					arrayP[a][b].play();
				}
			}
			
		}
		else{
			if(currentSection >= arrayP.length - 1){
				for(int a = 0; a < 2; a++){
					for(int b = 0; b < arrayP[0].length; b++){
						arrayP[arrayP.length - 1 - a][b].play();
					}
				}
			}
			else{
				arrayP[currentSection - 1][0].play();
				arrayP[currentSection - 1][1].play();
				arrayP[currentSection - 1][2].play();
				
				arrayP[currentSection][0].play();
				arrayP[currentSection][1].play();
				arrayP[currentSection][2].play();
				
				arrayP[currentSection + 1][0].play();
				arrayP[currentSection + 1][1].play();
				arrayP[currentSection + 1][2].play();
				
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
				player.createAttack();
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
			if(key.getCode() == KeyEvent.VK_SPACE){
				keyCommands.remove(key);
			}
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
