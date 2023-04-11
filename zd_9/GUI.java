import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

//autor: Jakub Marczyk

//do przechowywania punktów wykresu
class Point {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "("+Double.toString(x) + "," + Double.toString(y) + ")";
	}
}

public class GUI extends JFrame {
	
	
	private int N=1;
	private double K=1.0;
	
	
	private static final long serialVersionUID = 1L;
	private JLabel showN = new JLabel("N="+N);
	private JLabel showK = new JLabel("K="+K);
	
	private JSlider setN = new JSlider(JSlider.HORIZONTAL, 1, 25, 1);
	private JTextField setK = new JTextField("1.0");
	
	private Wykres wykres;
	
	class Wykres extends JPanel{
		
		int pointStep = 2;
		int numberOfPoints;
		int numberOfY;
		int width;
		int height;
		
		double chartStep;
		
		List<Point> points;	
		
		public Wykres() {
			setBackground(Color.white);
			points = new ArrayList<Point>();
		}
		
		public void paintComponent(Graphics g) {
			width = getWidth();
			height = getHeight();
			super.paintComponent(g);
			g.setColor(Color.blue);
			drawTheChart(g);
			
		}
		
		public void drawTheChart(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			
			if(K<1.0 || K>10.0) {
				g2d.drawString("Unble to draw the graph, the K value is incorrect", width/2-150, height/2);
			} 
			
			else {
			setBackground(Color.white);
			numberOfPoints = getWidth()/pointStep;
			numberOfY = getHeight()/pointStep;
			
			chartStep = 2*K*Math.PI/numberOfPoints;
			
			double currentLocation = -K*Math.PI;
			
			points.clear();
			
			//na początku liczę wartości dla szerokość/numberOfPoints punktów
			for(int i=0;i<numberOfPoints;i++) {
				points.add(new Point(currentLocation, formula(currentLocation)));
				currentLocation+=chartStep;
			}
			
			//System.out.print(points);
			
			
			//znajduję maksymalną i minimalną wartość funkcji, by móc dopasować wykres do wysokości panelu
			Point min = points.get(0);
			Point max = min;
			
			for(Point p: points) {
				if(p.y<min.y) min = p;
				if(p.y>max.y) max = p;
			}
			
			//System.out.println("Max=" + max + " min=" + min);
			
			//współczynnik do dopasowywania wartości grafu do wysokości
			double mapCoefY = getHeight()/(Math.abs(min.y)+Math.abs(max.y));
			
			int chartLocation = 0;
			for(int i=0;i<numberOfPoints;i++) {
				Point currentPoint = points.get(i);
				
				//muszę zmapować wartości
				int y = (int) (currentPoint.y*mapCoefY);
				
				//współrzędne w lewym górnym rogu, więc height/2 będzie osią OX
				//rysuję punkty jako koła o średnicy 2px
				g2d.fillOval(chartLocation-2, height/2+y-2,4, 4);
				
				//liczba punktów zależy od szerokości panelu
				chartLocation+=pointStep;
			}
			//System.out.println("I drew the chart");
			}
			
		}
		
		double formula(double x) {
			
			double sum=0;
			for(int n=1;n<=N;n++) {
				sum+= ( (-2)*Math.pow(-1, n)/n ) * Math.sin(n*x);
			}
			return sum;
		}
		
	}
	
	
	
	
	public GUI(){
		super("Jakub Marczyk Zadanie 9");
		
		setResizable(true);
		setSize(900, 900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		setK.getDocument().addDocumentListener(new Listenerek());
		setN.addChangeListener(new ListenSlider());
		
		add(setK, BorderLayout.NORTH);
		add(showN, BorderLayout.WEST);
		add(showK, BorderLayout.EAST);
		
		add(setN, BorderLayout.SOUTH);
		
		wykres = new Wykres();
		wykres.addComponentListener(new PanelListener());
		
		add(wykres, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	//do nasłuchiwania zmian panelu
	private class PanelListener implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			//System.out.println("I've been resized!");
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}
		
	}
	
	//do nasłuchiwania zmian JLabel
	private class ListenSlider implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			N = ((JSlider)e.getSource()).getValue();
			System.out.println("N="+N);
			showN.setText("N="+Integer.toString(((JSlider)e.getSource()).getValue()));
			wykres.repaint();
		}
		
	}
	
	//do nasłuchiwania zmian JTextField
	private class Listenerek implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			try {
				System.out.println(e.getDocument().getText(0,e.getDocument().getLength()));
				K = Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()));
				showK.setText("K="+e.getDocument().getText(0,e.getDocument().getLength()));
				wykres.repaint();
			} catch (BadLocationException e1) {
				K=-1;
				wykres.repaint();
				//e1.printStackTrace();
			} catch (NullPointerException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The JTextField value is null");
			} catch (NumberFormatException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The text value is not a double");
			}
			
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			try {
				System.out.println(e.getDocument().getText(0,e.getDocument().getLength()));
				K = Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()));
				showK.setText("K="+ e.getDocument().getText(0,e.getDocument().getLength()));
				System.out.println("K="+e.getDocument().getText(0,e.getDocument().getLength()));
				wykres.repaint();
			} catch (BadLocationException e1) {
				K=-1;
				wykres.repaint();
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The JTextField value is null");
			} catch (NumberFormatException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The text value is not a double");
			}
			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			try {
				showN.setText(e.getDocument().getText(0,e.getDocument().getLength()));
				K = Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()));
				showK.setText(e.getDocument().getText(0,e.getDocument().getLength()));
				System.out.println("K="+e.getDocument().getText(0,e.getDocument().getLength()));
				wykres.repaint();
			} catch (BadLocationException e1) {
				K=-1;
				wykres.repaint();
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The JTextField value is null");
			} catch (NumberFormatException e1) {
				K=-1;
				wykres.repaint();
				System.out.println("The text value is not a double");
			}
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
}
