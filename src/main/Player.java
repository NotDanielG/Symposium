package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.components.MovingComponent;

public class Player extends MovingComponent {
	
	private String imageSrc;
	private Image image;
	
	private BufferedImage buff;
	private Platform platform;
	
	private boolean load;
	private boolean jump;
	private boolean onLand;
	private long start;
	private long start1;
	
	private int z;
	private int acceleration;
	private int initialyV;
	private int grav;
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, w, h);
		z = x;
		imageSrc = photo;
		loadImage();
		initialyV = 0;
		acceleration = 5;
		grav = 1;
		
		setPosx(x);
		setPosy(y);
		this.play();
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
			load = true;
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void update(Graphics2D g){
		if(load){
			image = (Image) buff;
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
					null);
			setPosx(getPosx() + getVx());
			setPosy(getPosy() + getVy());
			super.setX((int) getPosx());
			super.setY((int) getPosy());
		}
	}
	public void run(){
		setRunning(true);
		setLand(false);
		while(isRunning()){
			try {
				Thread.sleep(REFRESH_RATE);
				if(platform != null){
					if(!platform.isCollided()){
						setLand(false);
						setPlatform(null);
					}
				}
				else{
					setLand(false);
				}
				updatePhysics();
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void updatePhysics(){
		if(jump){
			super.setVy(-findSpeed());
		}
		else{
			if(!onLand){
				long current = System.currentTimeMillis();
				int difference = (int)(current - start1);
				double newV = grav*(double)(difference/100);
				super.setVy(-newV);
			}
		}
		
		
	}
	public int getAcceleration(){
		return acceleration;
	}
	public boolean isJump() {
		return jump;
	}
	public void setJump(boolean jump) {
		start = System.currentTimeMillis();
		initialyV = 7;
		this.jump = jump;
	}
	public double findSpeed(){
		long current = System.currentTimeMillis();
		int difference = (int)(current - start);
		double newV = initialyV - grav*(double)(difference/100);
		return newV;
	}
	public void hitGround(int y){
		jump = false;
		onLand = true;
		setVy(0);
		initialyV = 0;
		super.setY(y);
	}
	public void setLand(boolean b){
		if(b == false){
			start1 = System.currentTimeMillis();
		}
		onLand = b;
	}
	public boolean getLand(){
		return onLand;
	}
	public void setPlatform(Platform platform){
		this.platform = platform;
	}
	
}
