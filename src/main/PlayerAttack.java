package main;

import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import gui.components.Action;
import gui.components.MovingComponent;

public class PlayerAttack extends MovingComponent {
	private String imageSrc;
	private BufferedImage buff;
	private Action action;
	private Player player;
	private int z;
	private int idx;
	private double vx;
	private double result;
	private boolean load;
	private boolean hit;
	private boolean firstCheck;
	
	private List<BufferedImage> frames;
	private long lastChange;
	private long turnRate;
	
	
	public PlayerAttack(int x, int y, int w, int h, double vx,int z ,String photo) {
		super(x, y, 13, 10);
		imageSrc = photo;
		idx = 1;
		this.vx = vx;
		
		frames = new ArrayList<BufferedImage>();
		turnRate = 200;
		loadImage();
		this.z = z;
		setPosy(y);
		super.setVx(vx);
		super.setVy(0);
	}
	private void loadImage() {
		try {
			load = true;
			
			BufferedImage sheet= ImageIO.read(new File("resources/bullets.png")); 
			BufferedImage image1 = sheet.getSubimage(7, 12, 8, 6);
			BufferedImage image2 = sheet.getSubimage(21, 9, 12, 12);
			BufferedImage image3 = sheet.getSubimage(38, 8, 13, 13);
			BufferedImage image4 = sheet.getSubimage(56, 7, 15, 15);
			
			frames.add(image1);
			frames.add(image2);
			frames.add(image3);
			frames.add(image4);
			
			
			buff = frames.get(0);
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
			
			if(getPosx() < -100 || getPosx() > 800 || idx == 5){
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
				if( getVx() < 0&& !firstCheck){
					clear();
					flip();
					firstCheck = true;
				}
				
				player = Start.screen.getPlayer();
				result = player.getZ() - this.z;
				if(!hit){	
					Enemy[][] e = Start.screen.getEnemies();
	
					for(int i = 0; i <  e.length;i++){
						if(e[i][0] !=null){
							if(isCollided(e[i][0])){
								hit = true;
								e[i][0].reduceHP();
								if(e[i][0].getHP() <= 0){
									Start.screen.getEnemies()[i][0].setRunning(false);
									Start.screen.remove(Start.screen.getEnemies()[i][0]);
									Start.screen.getEnemies()[i][0] = null;
								}
								
								setVx(0);
								break;
							}
						}
					}
				}
				
				if(hit && idx < 5 && System.currentTimeMillis() - lastChange > turnRate ){
					if(idx < 4){
						buff = frames.get(idx);
					}
					clear();
					lastChange = System.currentTimeMillis();
					idx++;
						
					
				}
				firstCheck = true;
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
	public void flip(){
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-(frames.get(idx-1)).getWidth(null), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx, 
				 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
		buff = op.filter(buff, null);
	}
	public boolean lowerRight(Enemy enemy){
		if(getX() + getWidth() >= enemy.getX() && 
	       getX() + getWidth() <= enemy.getX() + enemy.getWidth() &&
	       getY() + getHeight() >= enemy.getY() &&
	       getY() + getHeight() <= enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean lowerLeft(Enemy enemy){
		if(getX() >= enemy.getX() && 
		   getX() <= enemy.getX() + enemy.getWidth() &&
		   getY() + getHeight() >= enemy.getY() && 
		   getY() + getHeight() <= enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperRight(Enemy enemy){
		if(getX() + getWidth() >= enemy.getX() && 
		   getX() + getWidth() <= enemy.getX() + enemy.getWidth() &&
		   getY() >= enemy.getY() && 
		   getY() <= enemy.getY() + enemy.getHeight()){
			return true;
		}
		return false;
	}
	public boolean upperLeft(Enemy enemy){
		if(getX() >= enemy.getX() && getX() <= enemy.getX() + enemy.getWidth()
		&& getY() >= enemy.getY() && getY() <= enemy.getY() + enemy.getHeight()){
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
