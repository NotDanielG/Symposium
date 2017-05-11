package main;

import java.awt.Image;


import javax.swing.ImageIcon;

import gui.components.MovingComponent;
import main.Start;
import main.ScreenGame;
import gui.Screen;

public class Platform extends MovingComponent implements Collidable {
	private String imageSrc;
	private Image image;
	private boolean load;
	public Platform(int x, int y, int w, int h, String picture) {
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
	@Override
	public boolean isCollided() {
		Player player = Start.screen.getPlayer();
		if(player.getVx() > 0 &&(player.getX() > getX() && player.getX() < getX() + getWidth())){
			
		}
		return false;
	}
	

}
