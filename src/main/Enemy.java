package main;

import gui.components.Graphic;
import gui.components.MovingComponent;

public class Enemy extends MovingComponent{
	private int z;
	private String photoLocation;
	public Enemy(int x, int y, int w, int h,int z,String photo) {
		super(x,y,w,h);
		this.photoLocation = photo;
		this.z = z;
	}

}
