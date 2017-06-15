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
	private Enemy[][] enemies;
	private boolean gameRunning;
	private Player player;
	private List<Key> keyCommands;
	private Platform[][] arrayP;
	private List<Platform> platforms;
	private List<Integer> zList;
	private List<Integer> yList;
	private List<Graphic> hearts;
	
	private int currentSection;
	
	
	public ScreenGame(int width, int height) {
		super(width, height);
		arrayP = new Platform[7][3];
		
		enemies = new Enemy[7][2];
		gameRunning = true;
		currentSection = 0;
		Thread x = new Thread(this);
		x.start();
	}
	public void initObjects(List<Visible> viewObjects) {
		
		keyCommands = new ArrayList<Key>();
		player = new Player(350,300,50,50, "resources/square.png");
		viewObjects.add(player);
		
		player.play();
	}
	public void createPlatform(int x, int y, int width, int height, int z, int i){
		Platform xd = new Platform(x, y, width, height, z, "resources/platform.png");
		xd.setAction(new Action(){
			public void act() {
				getPlayer().hitGround((int)xd.getY() - player.getHeight());
				getPlayer().setStart(System.currentTimeMillis());
				getPlayer().setPlatform(xd);
			}
		});
		addObject(xd);
		xd.play();
	}
	public void createEnemy(int x, int y, int z, int i){
		Enemy enemy = new Enemy(x, y - 37, 50,50, z,"resources/enemyattack.png");
		enemy.setAction(new Action(){
			public void act(){
				player.decreaseHP();
			}
		});
		addObject(enemy);
		enemies[i][0] = enemy;
		enemy.play();
	}
	public void run() {
		
		keyCommands = Collections.synchronizedList(new ArrayList<Key>());
		platforms = Collections.synchronizedList(new ArrayList<Platform>());
		
		zList = Collections.synchronizedList(new ArrayList<Integer>());
		yList = Collections.synchronizedList(new ArrayList<Integer>());
		hearts = Collections.synchronizedList(new ArrayList<Graphic>());
		
		createPlatform(10, 450, 200, 30 , 600, 0);
		createPlatform(10, 450, 200, 30 , 850, 0);
		createPlatform(10, 450, 200, 30 , 1100, 0);
		createPlatform(10, 320, 200, 30 , 1100, 0);
		createPlatform(10, 450, 200, 30 , 1350, 0);
		
		createEnemy(10, 450, 850, 0);
		createEnemy(10, 320, 1150, 1);
		
		createPlatform(10, 450, 200, 30 , 1600, 0);
		createPlatform(10, 320, 200, 30 , 1850, 0);
		createPlatform(10, 450, 200, 30 , 2000, 0);
		
		createEnemy(10, 320, 1950, 2);
		
		createPlatform(10, 450, 200, 30 , 2170, 0);
		createPlatform(10, 320, 200, 30 , 2450, 0);
		createPlatform(10, 220, 200, 30 , 2600, 0);
		
		createEnemy(10, 320, 2500, 3); 
		createEnemy(10, 220, 2650, 4); 
		
		createPlatform(10, 450, 200, 30 , 2850, 0);
		createPlatform(10, 500, 200, 30 , 3000, 0);
		createPlatform(10, 400, 200, 30 , 3250, 0);
		
		createEnemy(10,500,3100,5);
		
		createPlatform(10, 450, 200, 30 , 3400, 0);
		createPlatform(10, 350, 200, 30 , 3550, 0);
		createPlatform(10, 330, 200, 30 , 3700, 0);
		
		createEnemy(10,330, 3750, 6);
		
		//Test
//		Platform xd = new Platform(10,yList.get(idx), 1000, 30, 
//				0, "resources/platform.png");
//		xd.setAction(new Action(){
//			public void act() {
//				getPlayer().hitGround((int)xd.getY() - player.getHeight());
//				getPlayer().setStart(System.currentTimeMillis());
//				getPlayer().setPlatform(xd);
//			}
//		});
//		addObject(xd);
//		xd.play();
		
//		for(int i = 0; i < arrayP.length; i++){
//			for(int j = 0; j < arrayP[i].length; j++){
//				
//				Platform platform = new Platform(10,yList.get(idx), 100, 30, zList.get(idx), "resources/platform.png");
//				platform.setAction(new Action(){
//					public void act() {
//						getPlayer().hitGround((int)platform.getY() - player.getHeight());
//						getPlayer().setStart(System.currentTimeMillis());
//						getPlayer().setPlatform(platform);
//					}
//				});
//				
//				arrayP[i][j] = platform;
//				addObject(platform);
//				idx++;
//				if(j == 1){
//					Enemy enemy = new Enemy(10,yList.get(idx) - 37, 50,50,
//							zList.get(idx) + 50,"resources/triangle.png");
//					enemy.setAction(new Action(){
//						public void act(){
//							System.out.println("hit");
//						}
//					});
//					addObject(enemy);
//					enemies[i][0] = enemy;
//					enemy.play();
//				}
//			}
//		}
		
		while(gameRunning){
			try {
				Thread.sleep(20);
//				if(player.getZ()+300 < currentSection * 800 - 400){
//					currentSection--;
//					loadSections(currentSection + 1);
//				}
//				else{
//					if(player.getZ()+300 > (currentSection + 1) * 800 - 400){
//						currentSection++;
//						loadSections(currentSection - 1);
//					}
//				}
				updateHP();
				if(player.getHP() == 0){
					player.setZ(250);
				}
				checkButtonList();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void updateHP(){
		int x = 20;

		while (hearts.size() > 0) {
			this.remove(hearts.remove(hearts.size() - 1));
		}

		for (int i = 0; i < player.getHP(); i++) {
			Graphic heart = new Graphic(x, 40, 20, 20, "resources/heart.png");
			hearts.add(heart);
			this.addObject(heart);

			x += 30;
		}
	}
	private void loadSections(int previousSection) {
		if(previousSection > currentSection && !(previousSection + 2 > arrayP.length-1)){
			for(int i = 0; i < 3; i++){
				arrayP[previousSection+1][i].setRunning(false);
				if(enemies[previousSection+1][0] != null){
					enemies[previousSection+1][0].setRunning(false);
				}
			}
		}
		else{
			if(previousSection-1 >= 0){
				for(int i = 0; i < 3; i++){
					arrayP[previousSection-1][i].setRunning(false);
					if(enemies[previousSection-1][0] != null){
						enemies[previousSection-1][0].setRunning(false);
					}
				}
			}
		}
		
		if(currentSection <= 0){
			for(int a = 0; a < 2; a++){
				for(int b = 0; b < arrayP[0].length; b++){
					arrayP[a][b].play();
					
				}
				if(enemies[a][0]!= null){
					enemies[a][0].play();
				}
			}
			
		}
		else{
			if(currentSection >= arrayP.length - 1){
				for(int a = 0; a < 2; a++){
					for(int b = 0; b < arrayP[0].length; b++){
						arrayP[arrayP.length - 1 - a][b].play();
					}
					if(enemies[enemies.length - 1 - a][0] != null){
						enemies[enemies.length - 1 - a][0].play();
					}
				}
			}
			else{
				for(int i = -1; i < 2;i++){
					for(int j = 0; j < 3;j++){
						arrayP[currentSection + i][j].play();
					}
					if(enemies[currentSection + i][0] != null){
						enemies[currentSection + i][0].play();
					}
				}
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
						player.setWalk(true);
					}
				});
				player.setDirection(1);
				keyCommands.add(key);
				break;
			case KeyEvent.VK_A:
				key.setCode(keyCode);
				key.setAction(new Action(){
					public void act(){
						player.setZ(player.getZ() - getPlayer().getAcceleration());
						player.setWalk(true);
					}
				});
				player.setDirection(-1);
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
			if(key.getCode() == keyCode) {
				if((keyCode == KeyEvent.VK_D || keyCode ==KeyEvent.VK_A)){
					player.setWalk(false);
				}
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
	public int getCurrentSection(){
		return currentSection;
	}
	public Enemy[][] getEnemies() {
		return enemies;
	}
}
