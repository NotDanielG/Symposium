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
	private List<BufferedImage> shooting;
	private List<BufferedImage> shootJump;
	private List<BufferedImage> shootWalk;
	
	private List<BufferedImage> currentList;
	private int idx;
	
	
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, 30, 34);
		start = System.currentTimeMillis();
		idleStart = System.currentTimeMillis();
		attackRate = 500;
		idleRate = 1000;
		
		idle = new ArrayList<BufferedImage>();
		walking = new ArrayList<BufferedImage>();
		jumping = new ArrayList<BufferedImage>();
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
		grav = .5;
		health = 3;
		direction = 1;
		lastDirection = 1;
		
		setPosx(x);
		setPosy(401);
		currentList = idle;
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
			BufferedImage image7 = sheet.getSubimage(394, 19, 32, 34);
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
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, getWidth(), getHeight(),
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
				System.out.println("Prev: "+ lastDirection);
				System.out.println("Current: " + direction);
				if(lastDirection != direction){
					flip(currentList);
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
						setWidth(buff.getWidth());
						setHeight(buff.getHeight());
					}
				}
				else{
					if(idling){
						if(System.currentTimeMillis() - idleStart >= idleRate){
							idleIdx++;
							buff = idle.get(idleIdx%idle.size());
							idleStart = System.currentTimeMillis();
							setWidth(buff.getWidth());
							setHeight(buff.getHeight());
							clear();
						}
					}
					else{
						if(walk){
							if(System.currentTimeMillis() - walkStart >= walkRate){
								walkIdx++;
								if(walkIdx == 11){
									walkIdx = 1;
									buff = walking.get(walkIdx);
									
								}
								else{
									buff = walking.get(walkIdx%walking.size());
								}
								clear();
								walkStart = System.currentTimeMillis();
								setWidth(buff.getWidth());
								setHeight(buff.getHeight());
							}
						}
					}
				}
				
				if(platform != null){
					if(getY() + getHeight() < platform.getY()){
						setY(platform.getY() - getHeight());
					}
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
		initialyV = 4;
		currentList = jumping;
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
		jumpIdx = 0;
		currentList = idle;
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
	public void flip(List list){
		for(int i = 0; i < list.size();i++){
			
		}
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-(buff.getWidth(null)), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx, 
				 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
		buff = op.filter(buff, null);
	}
	public void setWalk(boolean b){
		idling = !b;
		this.walk = b;
		if(walk == false){
			walkIdx = 0;
			currentList = idle;
		}
		currentList = walking;
	}
	
}
