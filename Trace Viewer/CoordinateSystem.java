import java.util.*;
import java.awt.*;

public class CoordinateSystem {
	
	private Point topLeft; 
	private Point origin; 
	private Point bottomRight; 
	private ArrayList<Integer> times; 
	private ArrayList<Integer> amounts; 
	private int xMax; 
	private int yMax; 
	private int xNumberOfTicks; 
	private int yNumberOfTicks; 
	private int xInterval; 
	private int yInterval; 
	private double xUnit; 
	private double xTickUnit; 
	private double yTickUnit; 
	private int[] xIntervals; 
	private int[] yIntervals; 
	
	public CoordinateSystem(Point topLeft, Point origin, Point bottomRight){
		this.topLeft = topLeft;
		this.origin = origin;
		this.bottomRight = bottomRight;
		times = new ArrayList<Integer>();
		amounts = new ArrayList<Integer>();
		xMax = 600;
		yMax = 0;
		xIntervals = new int[]{1, 2, 5, 10, 20, 50, 100};
		yIntervals = new int[]{10, 20, 50, 100, 200, 500, 1000, 2000, 5000};
		xNumberOfTicks = 12;
		xTickUnit = 900.0 / 12;
		yNumberOfTicks = 0;
		yTickUnit = 0.0;
	}

	public int getXMax(){
		return xMax;
	}
	
	public int getYMax(){
		return yMax;
	}

	public int getXInterval(){
		return xInterval;
	}

	public int getYInterval(){
		return yInterval;
	}

	public int getXTicks(){
		return xNumberOfTicks;
	}

	public int getYTicks(){
		return yNumberOfTicks;
	}
	
	public double getXTickUnit(){
		return xTickUnit;
	}
	
	public double getYTickUnit(){
		return yTickUnit;
	}
	
	public void setupXaxis(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(topLeft.x, topLeft.y, origin.x, origin.y);
	}
	
	public void setupYaxis(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(origin.x, origin.y, bottomRight.x, bottomRight.y);
	}
	
	public void setupXticks(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i <= xNumberOfTicks; i++){
			g2d.draw(new Line2D.Double(origin.x + i * xTickUnit, origin.y, origin.x + i * xTickUnit, origin.y + 5));
		}
	}
	
	public void setupYticks(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i <= yNumberOfTicks; i++){
			g2d.draw(new Line2D.Double(origin.x, origin.y - i * yTickUnit, origin.x - 5, origin.y - i * yTickUnit));
		}
	}
	
	public void setPlot(Host host, Integer time){
		times = host.getTimes();
		amounts = host.getAmounts();
		xMax = time.intValue();
		yMax = host.getMaxAmount().intValue();
		xUnit = 900.0 / time;
		boolean xIsSet = true;
		boolean yIsSet = true;
		
		for (int i : xIntervals){
			for (int j = 8; j <= 25; j++){
				if (i * j >= xMax && xIsSet){
					xNumberOfTicks = j;
					xInterval = i;
					xIsSet = false;
				}
			}
		}
		xTickUnit = 900.0 / xNumberOfTicks;
		
		for (int m = 8; m >= 0; m--){
			for (int n = 4; n <= 10; n++) {
				if (yIntervals[m] * n * 1024 <= yMax && yIsSet){
					yNumberOfTicks = n;
					yInterval = yIntervals[m];
					yIsSet = false;
				}
			}
		}
		yTickUnit = yInterval * 1024 * 250.0 / yMax;
	} 

	public void drawingPlot(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < times.size(); i++) {
			int time = times.get(i).intValue();
			int amount = amounts.get(i).intValue();
			double width = xUnit * 2;
			double height = ((double) amount / yMax) * 250;
			double h = origin.x + time * xUnit;
			double v = origin.y - height;
			g2d.setColor(Color.orange);
			g2d.draw(new Rectangle2D.Double(h, v, width, height));
		}
	}
}