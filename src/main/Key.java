package main;

import gui.components.Action;

public class Key implements Action {
	private Action action;
	private int code;
	public Key() {
		code = 0;
	}

	@Override
	public void act() {
		action.act();
	}

	public void setAction(Action action) {
		this.action = action;
		
	}
	public void setCode(int x){
		code = x;
	}
	public int getCode(){
		return code;
	}


}
