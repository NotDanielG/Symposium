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
	private int z;
	private String imageSrc;
	private Image image;
	private boolean load;
	private BufferedImage buff;
	
	private long start;
	private int acceleration;
	private boolean jump;
	private int initialyV;
	private int grav;
	
	public Player(int x, int y,int w ,int h,String photo) {
		super(x, y, w, h);
		z = x;
		imageSrc = photo;
		loadImage();
		initialyV = 7;
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
	private void updatePhysics() {
		if(jump){
			long current = System.currentTimeMillis();
			int difference = (int)(current - start);
			double newV = initialyV - grav*(double)(difference/100);
			if(getPosy() + getVy() >= 301){
				jump = false;
				setVy(0);
			}
			else{
				super.setVy(-newV);
			}
		}
		else{
			super.setY(300);
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
		this.jump = jump;
	}
	
}
