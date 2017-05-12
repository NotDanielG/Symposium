package main;

import java.awt.Image;


import javax.swing.ImageIcon;

import gui.components.MovingComponent;
import main.Start;
import main.ScreenGame;
import gui.Screen;
import gui.components.Action;

public class Platform extends MovingComponent implements Collidable , Action{
	private String imageSrc;
	private Image image;
	private boolean load;
	private Action action;
	public Platform(int x, int y, int w, int h, int z, String picture) {
		super(x, y, w, h);
		imageSrc = picture;
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
	public void run(){
		setRunning(true);
		while(isRunning()){
			try {
				Thread.sleep(REFRESH_RATE);
				
				if(isCollided()){
					System.out.println("S");
					act();
				}
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public boolean isCollided() {
		Player player = Start.screen.getPlayer();
		if(player.getVy() > 0 &&(player.getY() + player.getVy() > getY()) && 
		   player.getX() > getX() && player.getX() < getX() + getWidth()){
			return true;
		}
		return false;
	}
	public void act() {
		action.act();
	}
	public void setAction(Action action){
		this.action = action;
	}
	

}
