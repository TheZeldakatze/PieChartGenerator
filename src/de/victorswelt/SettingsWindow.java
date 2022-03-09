package de.victorswelt;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SettingsWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	PieChart pieChart;
	PieChartWindow pieChartWindow;
	
	
	JFrame frame;
	
	JButton exportButton;
	JTextField originOffset;
	
	public SettingsWindow(PieChart pieChart) {
		this.pieChart = pieChart;
		pieChartWindow = new PieChartWindow(pieChart);
		
		// set up the layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// create the origin offset field
		add(new JLabel("Origin offset (in Degrees): "));
		add(originOffset = new JTextField("0"));
		originOffset.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				changeOffset(originOffset.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changeOffset(originOffset.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changeOffset(originOffset.getText());
			}
			
			private void changeOffset(String s) {
				try {
					pieChartWindow.setOriginOffset(Integer.parseInt(s));
				} catch(NumberFormatException e) {
					
				}
			}
		});
		
		// create the segments
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#98D9EE"), 10)));
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#00E783"), 10)));
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#E55F28"), 10)));
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#000096"), 10)));
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#B4E2F1"), 0)));
		add(new SegmentField(pieChartWindow, new Segment(Color.decode("#F2C74E"), 0)));
		
		// create the export button
		add(exportButton = new JButton("Export"));
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openSaveDialog();
			}
		});
		
		
		// create a frame to show everything
		frame = new JFrame("Pie chart settings");
		frame.getContentPane().add(this);
		frame.setSize(100, 320);
		frame.setVisible(true);
		frame.setLocationRelativeTo(pieChartWindow);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		frame.pack();
	}
	
	public void openSaveDialog() {
		// get the file name
		String s = (String) JOptionPane.showInputDialog(this, "Please enter a name for the export file (make sure it is not used yet! Suffix will be automatically added)", 
				"Export meme", JOptionPane.PLAIN_MESSAGE, null, null, "pie_chart_");
		if(s == null) {
			JOptionPane.showMessageDialog(this, "No filename specified! Save process aborted", "Save error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		s += ".png";
		
		File f = new File(s);
		if(!f.exists()) {
			try {
				ImageIO.write(pieChartWindow.getOffscreen(), "png", f);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error during saving! Save process aborted.\n\nStacktrace:\n" + e.getStackTrace(), "Save error!", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		else
			JOptionPane.showMessageDialog(this, "File already exists! Save process aborted", "Save error!", JOptionPane.ERROR_MESSAGE);
	}
}

class SegmentField extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static int curID = 1;
	int id = curID++;
	
	PieChartWindow pieChartWindow;
	Segment segment;
	JTextField color, size;
	
	
	public SegmentField(PieChartWindow pieChartWindow, Segment s) {
		this.pieChartWindow = pieChartWindow;
		segment = s;
		
		// add the segment to the pie chart
		pieChartWindow.addSegment(s);
		
		// create the layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createTitledBorder("Segment " + id));
		
		// create & add the elements
		add(new JLabel("Size: "));
		add(size = new JTextField("" + s.getSize()));
		add(new JLabel("Color: #"));
		add(color = new JTextField(Integer.toHexString(s.color.getRGB()).substring(2)));
		
		// make the listeners
		size.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				applySize(size.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				applySize(size.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				applySize(size.getText());
			}
		});
		color.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				applyColor(color.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				applyColor(color.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				applyColor(color.getText());
			}
		});
		
	}
	

	
	 private void applyColor(String text) {
		try {
			System.out.println(text);
			Color c = Color.decode("#" + text);
			System.out.println(c);
			segment.setColor(c);
			pieChartWindow.repaint();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void applySize(String text) {
		try {
			int size = Integer.parseInt(text);
			segment.setSize(size);
			pieChartWindow.repaint();
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
