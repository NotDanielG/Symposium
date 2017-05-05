package main;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import gui.components.Action;
import gui.components.Graphic;
import gui.components.MovingComponent;

public class Enemy extends MovingComponent implements Action{
	private int z;
	private String imageSrc;
	private Image image;
	private boolean load;
	private Action action;
	public Enemy(int x, int y, int w, int h,int z,String photo) {
		super(x,y,w,h);
		this.imageSrc = photo;
		this.z = z;
		setX(x);
		setY(y);
		loadImage();
		this.play();
	}
	private void loadImage() {
		try{
			image = new ImageIcon(imageSrc).getImage();
			load = true;
			update();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void update(Graphics2D g){
		if(load){
			g.drawImage(image, 0,0 , getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
					null);
			//setVx()/Vy()
			setPosx(getPosx() + getVx());
			setPosy(getPosy() + getVy());
			super.setX((int) getPosx());
			super.setY((int) getPosy());
		}
	}
	public void run(){
		setRunning(true);
		while (isRunning()) {
			try {
				Thread.sleep(REFRESH_RATE);
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
