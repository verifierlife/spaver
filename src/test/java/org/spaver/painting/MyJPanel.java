package org.spaver.painting;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyJPanel extends JPanel {
	Color c;

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public MyJPanel(Color c) {
		this.c = c;
	}

	public void paint(Graphics g) {
		g.setColor(c);
		g.fillOval(50, 8, 100, 100);
	}
	
}
