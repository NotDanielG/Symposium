package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.components.Action;
import gui.components.Graphic;
import gui.components.MovingComponent;

public class Enemy extends MovingComponent implements Action, ImageObserver{
	private int w;
	private int h;
	private int z;
	private String imageSrc;
	private Image image;
	private boolean load;
	private Action action;
	private BufferedImage buff;
	private ImageIcon icon;
	public Enemy(int x, int y, int w, int h,int z,String photo) {
		super(x,y,w,h);
		this.imageSrc = photo;
		this.w = w;
		this.h = h;
		this.z = z;
		setX(x);
		setY(y);
		icon = new ImageIcon(photo);
		buff = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		BufferedImage xd = buff.getSubimage(0, 100, 5, 5);
		try {
			buff = ImageIO.read(new File(photo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = image.createGraphics();
//		g.drawImage(icon.getImage(), 0, 0, newWidth, newHeight, 0, 0, icon.getIconWidth(), icon.getIconHeight(),
//				null);
//		loadImage();
		load = true;
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
//			g.drawImage(icon.getImage(), 0, 0, w, h, 0, 0, icon.getIconWidth(), icon.getIconHeight(),null);
			image = (Image) buff;
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
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return true;
	}

}
