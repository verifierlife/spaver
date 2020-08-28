package org.spaver.painting;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class Circle extends JFrame {
	
	MyJPanel jPanel;

	public Circle(){
		jPanel = new MyJPanel(Color.RED);
		this.setLayout(new BorderLayout());
		this.add(jPanel, BorderLayout.CENTER);
		this.setBounds(300, 200, 300, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		try {
			Thread.sleep(1000);
			jPanel.setC(new Color(210,250,0));
			jPanel.repaint();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Circle();
	}
	
}
