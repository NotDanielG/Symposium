package main;

import java.awt.Graphics2D;


import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.components.MovingComponent;

public class Player extends MovingComponent {
	
	private String imageSrc;
	private Image image;
	private BufferedImage buff;
	private Platform platform;
	
	private boolean load;
	
	private boolean idling;
	private long idleRate;
	private long idleStart;
	private int idleIdx;
	
	private boolean walk;
	private long walkRate;
	private long walkStart;
	private int walkIdx;
	
	
	private boolean fire;
	
	
	
	private boolean jump;
	private long start;
	private long attackRate;
	private long lastAttack;
	
	private long jumpStart;
	private long jumpRate;
	private int jumpIdx;
	
	
	
	private int direction;
	private int lastDirection;
	private int z;
	private int acceleration;
	private int initialyV;
	private double initialxV;
	private int health;
	private double grav;
	
	private List<BufferedImage> idle;
	private List<BufferedImage> walking;
	private List<BufferedImage> jumping;
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, 30, 34);
		start = System.currentTimeMillis();
		idleStart = System.currentTimeMillis();
		attackRate = 500;
		idleRate = 1000;
		
		idle = Collections.synchronizedList(new ArrayList<BufferedImage>());
		walking = Collections.synchronizedList(new ArrayList<BufferedImage>());
		jumping = Collections.synchronizedList(new ArrayList<BufferedImage>());
		
		fire = false;
		
		walkRate = 100;
		walkStart = System.currentTimeMillis();
		
		jumpRate = 300;
		jumpStart = System.currentTimeMillis();		
		
		platform = null;
		imageSrc = photo;
		
		z = x;
		initialyV = 0;
		initialxV = 6.0;
		acceleration = 2;
		grav = .8;
		health = 3;
		direction = 1;
		lastDirection = 1;
		
		setPosx(x);
		setPosy(401);
		loadImage();
		this.play();
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
			
			BufferedImage sheet= ImageIO.read(new File("resources/sprite_sheet.png"));
			
			BufferedImage image1 = sheet.getSubimage(213, 18, 30, 34);
			BufferedImage image2 = sheet.getSubimage(247, 18, 30, 34);
			BufferedImage image3 = sheet.getSubimage(281, 18, 30, 34);
			
			idle.add(image1);
			idle.add(image2);
			idle.add(image3);
			
			BufferedImage image4 = sheet.getSubimage(319, 19, 30, 34);
			BufferedImage image5 = sheet.getSubimage(350, 19, 20, 34);
			BufferedImage image6 = sheet.getSubimage(371,18,23,35);
			BufferedImage image7 = sheet.getSubimage(394, 19, 31, 34);
			BufferedImage image8 = sheet.getSubimage(426, 20, 34, 33);
			BufferedImage image9 = sheet.getSubimage(460,20,26,33);
			
			BufferedImage image10 = sheet.getSubimage(489,19, 22, 34);
			BufferedImage image11 = sheet.getSubimage(511, 18, 25, 35);
			BufferedImage image12 = sheet.getSubimage(539,20,30,33);
			BufferedImage image13 = sheet.getSubimage(570, 20, 34, 33);
			BufferedImage image14 = sheet.getSubimage(604,20,29,34);
			
			walking.add(image4);
			walking.add(image5);
			walking.add(image6);
			walking.add(image7);
			walking.add(image8);
			walking.add(image9);
			walking.add(image10);
			walking.add(image11);
			walking.add(image12);
			walking.add(image13);
			walking.add(image14);
			
			BufferedImage image15 = sheet.getSubimage(5,73,24,37);
			BufferedImage image16 = sheet.getSubimage(34,69,15,41);
			BufferedImage image17 = sheet.getSubimage(55,64,19,46);
			BufferedImage image18 = sheet.getSubimage(77,69,23,41);
			BufferedImage image19 = sheet.getSubimage(102,68,27,42);
			BufferedImage image20 = sheet.getSubimage(134,72,24,38);
			BufferedImage image21 = sheet.getSubimage(159,78,30,32);
			
			jumping.add(image15);
			jumping.add(image16);
			jumping.add(image17);
			jumping.add(image18);
			jumping.add(image19);
			jumping.add(image20);
			jumping.add(image21);
			
			
			buff = image1;
			
			load = true;
			update();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void update(Graphics2D g){
		if(load){
			image = (Image) buff;
			setWidth(buff.getHeight(null));
			setHeight(buff.getHeight(null));
			
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, getWidth(), getHeight(),
					null);
			
			setPosy(getPosy() + getVy());
			super.setY((int) getPosy());
			
			super.setX((int) getPosx());
		}
	}
	public void run(){
		setRunning(true);		
		while(isRunning()){
			try {
				Thread.sleep(REFRESH_RATE);
				if(direction != lastDirection){
					lastDirection = direction;
					flip();
				}
				if(jump){
					if(System.currentTimeMillis() - jumpStart >= jumpRate){
						if(getVy() < 0 && jumpIdx != 2){
							jumpIdx++;
							clear();
						}
						else{
							if(getVy() > 0 && jumpIdx != jumping.size() -1){
								jumpIdx++;
								clear();
							}
						}
						buff = jumping.get(jumpIdx%jumping.size());
						jumpStart = System.currentTimeMillis();
					}
				}
				else{
					if(idling){
						if(System.currentTimeMillis() - idleStart >= idleRate){
							idleIdx++;
							buff = idle.get(idleIdx%idle.size());
							idleStart = System.currentTimeMillis();
							clear();
						}
					}
					else{
						if(walk){
							if(System.currentTimeMillis() - walkStart >= walkRate){
								buff = walking.get(walkIdx%walking.size());
								
								walkStart = System.currentTimeMillis();
								if(walkIdx >= walking.size()){
									walkIdx = 3;
								}
								else{
									walkIdx+=1;
								}
								clear();
							}
						}
					}
				}
				
				try{
					if(platform != null){
						if(getY() + getHeight() < platform.getY()){
							setY(platform.getY() - 34);
						}
						if(platform != null && (getX() > platform.getX() + platform.getWidth() ||
								getX() + getWidth () < platform.getX() || jump)){
							setStart(System.currentTimeMillis());
							platform = null;
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				if(walk){
					setWidth(buff.getWidth(null));
					setHeight(buff.getHeight(null));
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
			setZ(400);
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
		clear();
		start = System.currentTimeMillis();
		initialyV = 4;
		this.jump = jump;
	}
	public double findSpeed(){
		long current = System.currentTimeMillis();
		int difference = (int)(current - start);
		double newV = initialyV - grav*(double)(difference/100);
		return newV;
	}
	public void hitGround(int y){
		clear();
		jump = false;
		setVy(0);
		initialyV = 0;
		jumpIdx = 0;
		setHeight(34);
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
	public void flip(){
			for(int i = 0; i < idle.size(); i++){
				BufferedImage img = idle.get(i);
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-(img.getWidth(null)), 0);
				
				AffineTransformOp op = new AffineTransformOp(tx, 
						 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
				img = op.filter(img, null);
				idle.add(i,img);
				idle.remove(i+1);
				
			}
			for(int i = 0; i < jumping.size(); i++){
				BufferedImage img = jumping.get(i);
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-(img.getWidth(null)), 0);
				
				AffineTransformOp op = new AffineTransformOp(tx, 
						 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
				img = op.filter(img, null);
				jumping.add(i,img);
				jumping.remove(i+1);
				
			}
			for(int i = 0; i < walking.size(); i++){
				BufferedImage img = walking.get(i);
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-(img.getWidth(null)), 0);
				
				AffineTransformOp op = new AffineTransformOp(tx, 
						 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
				img = op.filter(img, null);
				walking.add(i,img);
				walking.remove(i+1);
				
			}
	}
	public void setWalk(boolean b){
		idling = !b;
		this.walk = b;
		if(walk == false){
			walkIdx = 0;
			
		}
	}
	public int getHP(){
		return health;
	}
	public void increaseHP(){
		health++;
	}
}
