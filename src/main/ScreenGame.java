package main;

import java.util.List;

import gui.Screen;
import gui.components.Graphic;
import gui.components.Visible;

public class ScreenGame extends Screen {

	public ScreenGame(int width, int height) {
		super(width, height);
		
	}

	@Override
	public void initObjects(List<Visible> viewObjects) {
		Graphic x = new Graphic(100,100, "resources/square.png");
		viewObjects.add(x);

	}

}
