package com.hereafter.wxarticle.model;

public class MenuItem {
	private boolean isSelected;
	private String text;

	public MenuItem(String text, boolean isSelected) {
		this.text = text;
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
