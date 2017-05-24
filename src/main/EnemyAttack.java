package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gui.components.Action;
import gui.components.MovingComponent;

public class EnemyAttack extends MovingComponent {
	private String imageSrc;
	private BufferedImage buff;
	private Action action;
	private Player player;
	private int z;
	private double result;
	private boolean load;
	
	public EnemyAttack(int x, int y, int w, int h, double vx, String photo) {
		super(x, y, w, h);
		imageSrc = photo;
		loadImage();
		z = x;
		setPosx(x);
		setPosy(y);
		super.setVx(vx);
		super.setVy(0);
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
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
			
			setPosx(result + getVx());
			super.setX((int) getPosx());
			
			if(isCollided()){
				action.act();
				setRunning(false);
				Start.screen.remove(this);
			}
			if(getX() < -50){
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
				//Assuming stationary, change to accommodate that this class is moving
				update();
			}
			catch(Exception e){
				
			}
		}
	}
	public boolean isCollided(){
		Player player = Start.screen.getPlayer();
		if(lowerRight(player) || lowerLeft(player) || 
		   upperRight(player) || upperLeft(player)){
			return true;
		}
		return false;
	}
	public boolean lowerRight(Player player){
		if(player.getX() + player.getWidth() > getX() && 
	       player.getX() + player.getWidth() < getX() + getWidth() &&
	       player.getY() + player.getHeight() > getY() &&
	       player.getY() + player.getHeight() < getY() + getHeight()){
			return true;
		}
		return false;
	}
	public boolean lowerLeft(Player player){
		if(player.getX() > getX() && player.getX() < getX() + getWidth() &&
		   player.getY() + player.getHeight() > getY() && 
		   player.getY() + player.getHeight() < getY() + getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperRight(Player player){
		if(player.getX() + player.getWidth() > getX() && 
		   player.getX() + player.getWidth() < getX() + getWidth() &&
		   player.getY() > getY() && player.getY() < getY() + getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperLeft(Player player){
		if(player.getX() > getX() && player.getX() < getX() + getWidth()
		&& player.getY() > getY() && player.getY() < getY() + getHeight()){
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
