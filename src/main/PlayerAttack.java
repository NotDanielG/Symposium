package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gui.components.Action;
import gui.components.MovingComponent;

public class PlayerAttack extends MovingComponent {
	private String imageSrc;
	private BufferedImage buff;
	private Action action;
	private Player player;
	private int z;
	private double result;
	private boolean load;
	private Enemy[][] enemies;
	
	public PlayerAttack(int x, int y, int w, int h, double vx,int z ,String photo) {
		super(x, y, w, h);
		imageSrc = photo;
		loadImage();
		this.z = z;
		setPosx(x);
		setPosy(y);
		super.setVx(vx);
		super.setVy(0);
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
			enemies = Start.screen.getEnemies();
			load = true;
			update();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void update(Graphics2D g){
		if(load){
			Image image = (Image) buff;
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
					null);
			
			setPosy(getPosy() + getVy());
			super.setY((int) getPosy());
			result*= -1;
			setPosx(result + getVx());
			super.setX((int) getPosx());
			z = (int) (z + getVx());
			for(int i = 0; i < enemies.length;i++){
				Enemy enemy = enemies[i][0];
				if(isCollided(enemy)){
//					action.act();
					System.out.println("Hi");
					setRunning(false);
					Start.screen.remove(this);
					i = enemies.length;
				}
			}
			if(getPosx() < -100){
				setRunning(false);
				Start.screen.remove(this);
			}
		}
	}
	public void run(){
		setRunning(true);	
		while(isRunning()){
			try{
				Thread.sleep(REFRESH_RATE);
				player = Start.screen.getPlayer();
				result = player.getZ() - this.z;
				enemies = Start.screen.getEnemies();
				update();
			}
			catch(Exception e){
				
			}
		}
	}
	public boolean isCollided(Enemy enemy){
		
		if(lowerRight(enemy) || lowerLeft(enemy) || 
		   upperRight(enemy) || upperLeft(enemy)){
			return true;
		}
		return false;
	}
	public boolean lowerRight(Enemy enemy){
		if(getX() + getWidth() > enemy.getX() && 
	       getX() + getWidth() < enemy.getX() + enemy.getWidth() &&
	       getY() + getHeight() > enemy.getY() &&
	       getY() + getHeight() < enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean lowerLeft(Enemy enemy){
		if(getX() > enemy.getX() && 
		   getX() < enemy.getX() + enemy.getWidth() &&
		   getY() + getHeight() > enemy.getY() && 
		   getY() + getHeight() < enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperRight(Enemy enemy){
		if(getX() + getWidth() > enemy.getX() && 
		   getX() + getWidth() < enemy.getX() + enemy.getWidth() &&
		   getY() > enemy.getY() && 
		   getY() < enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperLeft(Enemy enemy){
		if(getX() > enemy.getX() && getX() < enemy.getX() + enemy.getWidth()
		&& getY() > enemy.getY() && getY() < enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	private void act(){
		action.act();
	}
	public void setAction(Action action){
		this.action = action;
	}

}
