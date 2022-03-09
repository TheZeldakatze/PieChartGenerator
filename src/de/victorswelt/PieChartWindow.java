package de.victorswelt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PieChartWindow extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JFrame frame;
	PieChart pieChart;
	BufferedImage offscreen;
	
	List<Segment> segments;
	
	int originOffset = 0;
	
	public PieChartWindow(PieChart pieChart) {
		this.pieChart = pieChart;
		
		segments = new ArrayList<Segment>();
		
		// create a frame
		frame = new JFrame("Pie chart generator");
		frame.getContentPane().add(this);
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				pieChart.requestQuit(frame);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		// create an offscreen
		setOffscreenSize(1024, 1024);
	}
	
	public void addSegment(Segment s) {
		segments.add(s);
		repaint();
	}

	public RenderedImage getOffscreen() {
		return offscreen;
	}

	public int getOriginOffset() {
		return originOffset;
	}

	public void setOriginOffset(int originOffset) {
		this.originOffset = originOffset;
		repaint();
	}

	public void setOffscreenSize(int width, int height) {
		// clear out the offscreen and create a new object with a transparent background
		if(offscreen != null)
			offscreen.flush();
		offscreen = null;
		offscreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		// resize the window
		Insets insets = frame.getInsets();
		frame.setSize(offscreen.getWidth(null) + insets.left + insets.right, 
				offscreen.getHeight(null) + insets.top + insets.bottom);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// check that there even is an offscreen
		if(offscreen != null) {
			Graphics2D offscreenGraphics = (Graphics2D) offscreen.getGraphics();
			offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
			
			// get the total count
			int totalSize = 0;
			for(Segment s : segments)
				totalSize += s.getSize();
			
			// draw each segment
			int circularOffset = 90-originOffset; // everyone expects the first part of a pie chart to be at the top
			if(totalSize>0) {
				for (int i = 0; i < segments.size(); i++) {
					Segment s = segments.get(i);
					
					// set the color
					offscreenGraphics.setColor(s.getColor());
					
					// calculate the degrees the segment will occupy, make it negative
					int segmentDegrees = (int) -(((double) s.getSize()/totalSize) * 360);
					
					// draw and increment
					offscreenGraphics.fillArc(0, 0, offscreen.getWidth(), offscreen.getHeight(), circularOffset, segmentDegrees);
					circularOffset += segmentDegrees;
				}
			}
			
			// dispose of the graphics
			offscreenGraphics.dispose();
			
			// draw the offscreen
			g.drawImage(offscreen, 0, 0, getWidth(), getHeight(), null);
		}
	}
}
