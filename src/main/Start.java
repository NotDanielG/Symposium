package main;

import gui.GUIApplication;
import gui.Screen;

public class Start extends GUIApplication{
	private static Start gui;
	private static Screen screen;
	public Start(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		gui = new Start(800,600);
		Thread application = new Thread(gui);
		application.start();
	}

	@Override
	public void initScreen() {
		screen = new ScreenGame(getWidth(),getHeight());
		setScreen(screen);
		
	}

}
