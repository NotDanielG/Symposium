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
	private long start;
	private long attackRate;
	private long lastAttack;
	
	private int direction;
	private int z;
	private int acceleration;
	private int initialyV;
	private double initialxV;
	private int health;
	private double grav;
	
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, w, h);
		start = System.currentTimeMillis();
		attackRate = 1000;
		
		platform = null;
		imageSrc = photo;
		
		z = x;
		initialyV = 0;
		initialxV = 6.0;
		acceleration = 3;
		grav = 1;
		health = 3;
		direction = 1;
		
		setPosx(x);
		setPosy(y);
		
		loadImage();
		this.play();
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
			
			image = (Image) buff;
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
					null);
			
			setPosy(getPosy() + getVy());
			super.setY((int) getPosy());
			
//			setPosx(getPosx() + getVx());
			super.setX((int) getPosx());
		}
	}
	public void run(){
		setRunning(true);
		while(isRunning()){
			try {
				Thread.sleep(REFRESH_RATE);
				if(platform != null){
					if(getX() > platform.getX() + platform.getWidth() ||
							getX() + getWidth () < platform.getX() || jump){
						setStart(System.currentTimeMillis());
						platform = null;
					}
				}
				updatePhysics();
				update();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updatePhysics(){
		if(load){
			if(platform == null){
				super.setVy(-findSpeed());
			}
		}
		if(getPosy() > 900){
			setVy(-5);
			setZ(450);
			setPosy(300);
		}
	}
	public int getAcceleration(){
		return acceleration;
	}
	public void setPlatform(Platform platform){
		this.platform = platform;
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
		setVy(0);
		initialyV = 0;
		super.setY(y);
	}
	public void setStart(long start){
		this.start = start;
		setVy(0);
	}
	public void decreaseHP(){
		health--;
	}
	public int getZ(){
		return z;
	}
	public void setZ(int z){
		this.z = z;
	}
	public void createAttack(){
		if(System.currentTimeMillis() - lastAttack > attackRate){
			PlayerAttack attack = new PlayerAttack(getX(), getY(), 50,50,direction*initialxV,
			z+350+(direction*(50)),"resources/triangle.png");
			Start.screen.addObject(attack);
			attack.play();
			lastAttack = System.currentTimeMillis();
		}
	}
	public void setDirection(int x){
		direction = (int) ((x*initialxV)/initialxV);
	}
	
}
