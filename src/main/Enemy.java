package main;

import java.awt.Graphics2D;


import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.components.Action;
import gui.components.Graphic;
import gui.components.MovingComponent;

public class Enemy extends MovingComponent implements Action{
	private String imageSrc;
	private Image image;
	private Action action;
	private BufferedImage buff;
	private Player player;
	
	private List<BufferedImage> idle;
	private boolean idling;
	private int idleIdx;
	private long idleStart;
	private long idleRate;
	
	
	private List<BufferedImage> attack;
	private boolean attacking;
	private long atklast;
	private long atkRate;
	private int attackIdx;
	
	
	private int w;
	private int h;
	private int z;
	
	private int side;
	
	
	private int hp;
	private double attackSpeed;
	private double result;
	private long lastAttack;
	private long attackRate;
	
	private boolean damaged;
	private boolean load;
	
	public Enemy(int x, int y, int w, int h,int z,String photo) {
		super(x,y,32,37);
		this.imageSrc = photo;
		this.w = w;
		this.h = h;
		this.z = z;
		this.hp = 3;
		
		atkRate = 150;
		idleRate = 400;
		attack = Collections.synchronizedList(new ArrayList<BufferedImage>());
		idle = Collections.synchronizedList(new ArrayList<BufferedImage>());
		attackRate = 3000;
		damaged = false;
		attackSpeed = -1.0;
		setPosx(x);
		setPosy(y);
		
		lastAttack = System.currentTimeMillis();
		loadImage();
	}
	private void loadImage() {
		try {
			buff = ImageIO.read(new File(imageSrc));
			BufferedImage sheet = ImageIO.read(new File("resources/enemy.png"));
			BufferedImage image1 = sheet.getSubimage(10, 5, 32, 37);
			BufferedImage image2 = sheet.getSubimage(60, 5, 32, 37);
			BufferedImage image3 = sheet.getSubimage(110, 5, 32, 38);
			
			idle.add(image1);
			idle.add(image2);
			idle.add(image3);
			
			BufferedImage image4 = sheet.getSubimage(159, 5, 34, 37);
			BufferedImage image5 = sheet.getSubimage(207, 5, 38, 38);
			BufferedImage image6 = sheet.getSubimage(256, 4, 40, 39);
			BufferedImage image7 = sheet.getSubimage(305, 5, 41, 37);
			
			attack.add(image4);
			attack.add(image5);
			attack.add(image6);
			attack.add(image7);
			buff = image1;
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
			
			setPosy(getPosy() + getVy());
			super.setY((int) getPosy());
			setPosx(getVy() - result);
			super.setX((int) getPosx());
		}
	}
	public void run(){
		setRunning(true);
		while (isRunning()) {
			try {
				Thread.sleep(REFRESH_RATE);
				player = Start.screen.getPlayer();
				result = player.getZ() - this.z;
				if(player.getZ()+300 > z && attackSpeed < 0){
					attackSpeed*=-1;
					side = getWidth();
					flip();
				}
				else{
					if(player.getZ()+300 < z && attackSpeed > 0){
						attackSpeed*=-1;
						side = 0;
						flip();
					}
				}
				checkAction();
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void flip(){
		for(int i = 0; i < attack.size(); i++){
			BufferedImage img = attack.get(i);
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-(img.getWidth(null)), 0);
			
			AffineTransformOp op = new AffineTransformOp(tx, 
					 AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
			img = op.filter(img, null);
			attack.add(i,img);
			attack.remove(i+1);
			
		}
	}
	private void checkAction() {
		if(isCollided() && !damaged){
			damaged = true;
			action.act();
		}
		long current = System.currentTimeMillis();
		
		if(current - lastAttack >= attackRate){
			attacking = true;
			idling = false;
			if(attacking && System.currentTimeMillis() - atklast >= atkRate){
				buff = attack.get(attackIdx%attack.size());
				setWidth(buff.getWidth(null));
				setHeight(buff.getHeight(null));
				attackIdx++;
				atklast = System.currentTimeMillis();
				clear();
			}
			if(attackIdx >= attack.size()){
				EnemyAttack atk = new EnemyAttack((int)getPosx(), (getY() + getHeight()/2) - 10, 30,30,
				attackSpeed, 0,z+side,"resources/circle.png");
				atk.setAction(new Action(){
					public void act(){
						Start.screen.getPlayer().decreaseHP();
					}
				});
				atk.play();
				Start.screen.addObject(atk);
				lastAttack = current;
				attacking = false;
				attackIdx = 0;
				idleIdx = 0;
				clear();
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
	@Override
	public void act() {
		action.act();
		
	}
	public void setAction(Action action){
		this.action = action;
	}
	public void reduceHP(){
		this.hp--;
	}
	public int getHP(){
		return this.hp;
	}
	public void setAttack(boolean b){
		if(!b){
			
		}
	}

}
