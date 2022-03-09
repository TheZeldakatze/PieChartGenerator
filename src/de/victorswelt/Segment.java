package de.victorswelt;

import java.awt.Color;

public class Segment {
	Color color;
	int size;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Segment(Color color, int size) {
		super();
		this.color = color;
		this.size = size;
	}
}
