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
	
	private boolean load;
	private boolean jump;
	private long start;
	
	private int z;
	private int acceleration;
	private int initialyV;
	private double grav;
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, w, h);
		start = System.currentTimeMillis();
		z = x;
		imageSrc = photo;
		loadImage();
		initialyV = 0;
		acceleration = 5;
		grav = 1.0;
		
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
			System.out.println(getVy());
		}
	}
	public void run(){
		setRunning(true);
		while(isRunning()){
			try {
				Thread.sleep(REFRESH_RATE);
				updatePhysics();
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	private void updatePhysics(){
		if(load){
			if(jump){
				super.setVy(-findSpeed());
			}
			else{
				long current = System.currentTimeMillis();
				int difference = (int)(current - start);
				double newV = grav*(double)(difference/100);
				super.setVy(newV);
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
		setVy(0);
		initialyV = 0;
		super.setY(y);
	}
	public void setStart(long start){
		this.start = start;
		setVy(0);
	}
	
}
