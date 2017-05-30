package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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
	private BufferedImage buff;
	private Action action;
	private int z;
	private int result;
	private Player player;
	public Platform(int x, int y, int w, int h, int z, String picture) {
		super(x, y, w, h);
		imageSrc = picture;
		loadImage();
		setPosx(x);
		setPosy(y);
		this.z = z;
	}
	private void loadImage() {
		try{
			buff = ImageIO.read(new File(imageSrc));
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
				player = Start.screen.getPlayer();
				result = player.getZ() - this.z;
				if(isCollided()){
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
		if(player.getVy() > 0 
			&& player.getY() + player.getHeight() > getY()
			&& player.getY() + player.getHeight() < getY() + getHeight()
			&&(leftCorner(player) || rightCorner(player))){
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
	public void update(Graphics2D g){
		if(load){
			image = (Image) buff;
			g.drawImage(image,0,0 , getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
					null);
			setPosx(getVx() -result);
			setPosy(getPosy() + getVy());
			super.setX((int) getPosx());
			
			super.setY((int) getPosy());
		}
	}
	public boolean leftCorner(Player player){
		if(player.getX() > getX() && player.getX() < getX() + getWidth()){
			
			return true;
		}
		return false;
	}
	private boolean rightCorner(Player player) {
		if(player.getX() + player.getWidth() > getX() && player.getX() + player.getWidth() < getX() + getWidth()){
			return true;
		}
		return false;
	}
	public int getZ() {
		return z;
	}
}
