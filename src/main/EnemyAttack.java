package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gui.components.Action;
import gui.components.MovingComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyAttack extends MovingComponent {
	private String imageSrc;
	private BufferedImage buff;
	private Action action;
	private Player player;
	private int z;
	private double result;
	private boolean load;
	
	private List<BufferedImage> spin;
	private int spinIdx;
	private long spinStart;
	private long spinRate;
	
	
	
	public EnemyAttack(int x, int y, int w, int h, double vx, double vy, int z ,String photo) {
		super(x, y, 24, 24);
		imageSrc = photo;
		spinRate = 150;
		spin = Collections.synchronizedList(new ArrayList<BufferedImage>());
		loadImage();
		this.z = z;
		
		setPosx(x);
		setPosy(y);
		super.setVx(vx);
		super.setVy(vy);
		if(vx < 0){
			flip();
		}
	}
	private void flip(){
		for(int i = 0; i < spin.size(); i++){
			BufferedImage img = spin.get(i);
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-(img.getWidth(null)), 0);
			
			AffineTransformOp op = new AffineTransformOp(tx, 
					 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
			img = op.filter(img, null);
			spin.add(i,img);
			spin.remove(i+1);
		}
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
			BufferedImage sheet= ImageIO.read(new File("resources/enemyattack.png"));
			BufferedImage image1 = sheet.getSubimage(3, 7, 24, 24);
			BufferedImage image2 = sheet.getSubimage(39, 7, 21, 20);
			BufferedImage image3 = sheet.getSubimage(74, 7, 21, 20);
			
			spin.add(image1);
			spin.add(image2);
			spin.add(image3);
			
			buff = image1;
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
			if(getPosx() < -100 || getPosx() > 800 || getPosy() > 600 || getPosy() < -100){
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
				if(System.currentTimeMillis() - spinStart > spinRate){
					clear();
					buff = spin.get(spinIdx%spin.size());
					spinStart = System.currentTimeMillis();
					spinIdx++;
				}
				
				
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
