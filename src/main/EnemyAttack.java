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
	
	public EnemyAttack(int x, int y, int w, int h, double vx,int z ,String photo) {
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
			
			if(isCollided()){
				action.act();
				setRunning(false);
				Start.screen.remove(this);
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
		if(getX() + getWidth() > player.getX() && 
	       getX() + getWidth() < player.getX() + player.getWidth() &&
	       getY() + getHeight() > player.getY() &&
	       getY() + getHeight() < player.getY() + player.getHeight()){
			return true;
		}
		return false;
	}
	public boolean lowerLeft(Player player){
		if(getX() > player.getX() && 
		   getX() < player.getX() + player.getWidth() &&
		   getY() + getHeight() > player.getY() && 
		   getY() + getHeight() < player.getY() + player.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperRight(Player player){
		if(getX() + getWidth() > player.getX() && 
		   getX() + getWidth() < player.getX() + player.getWidth() &&
		   getY() > player.getY() && 
		   getY() < player.getY() + player.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperLeft(Player player){
		if(getX() > player.getX() && getX() < player.getX() + player.getWidth()
		&& getY() > player.getY() && getY() < player.getY() + player.getHeight()){
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
