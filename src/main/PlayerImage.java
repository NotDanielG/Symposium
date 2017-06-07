package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class PlayerImage extends BufferedImage {
	public PlayerImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}
	private boolean flipped;
	public boolean getFlipped(){
		return flipped;
	}
	public void setFlipped(boolean b){
		flipped = b;
	}
	

}
