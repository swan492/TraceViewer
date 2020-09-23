import java.util.*;
import java.awt.*;
import javax.swing.*;

public class DrawingPanel extends JPanel {

	private CoordinateSystem coordinate; 
	private Point topLeft; 
	private Point origin;  
	private Point bottomRight; 
	private JLabel volume; 
	private JLabel time;  
	private ArrayList<JLabel> xLabel; 
	private ArrayList<JLabel> yLabel;
	private boolean isDrawingGraph;

	public DrawingPanel(){
		setPreferredSize(new Dimension(1000, 325));
		setOpaque(true);
		setBackground(Color.white);
		setLayout(null);
		isDrawingGraph = false;
		setupVolume();
		setupTime();
		setupCoordinate();
		setupXlabels();
		setupYlabels();
	}

	private void setupVolume(){
		volume = new JLabel("Volume[bytes]");
		add(volume);
		Dimension size = volume.getPreferredSize();
		volume.setBounds(0, 0, size.width, size.height);
	}

	private void setupTime(){
		time = new JLabel("Time[s]");
		add(time);
		Dimension size = time.getPreferredSize();
		time.setBounds(490, 300, size.width, size.height);
	}
	
	private void setupCoordinate(){
		topLeft = new Point(50, 20);
		origin = new Point(50, 270);
		bottomRight = new Point(950, 270);
		coordinate = new CoordinateSystem(topLeft, origin, bottomRight);
	}

	private void setupXlabels(){
		xLabel = new ArrayList<JLabel>();
		int numberOfLabels = coordinate.getXTicks();
		int xMax = coordinate.getXMax();
		int interval = xMax / numberOfLabels;
		int unit = (int) coordinate.getXTickUnit();
		for (int i = 0; i <= numberOfLabels; i++){
			xLabel.add(new JLabel(Integer.toString(i * interval)));
			add(xLabel.get(i));
			Dimension size = xLabel.get(i).getPreferredSize();
			xLabel.get(i).setBounds(origin.x + i * unit - 10, origin.y + 8, size.width, size.height);
		}
	}
	
	private void setupYlabels(){
		yLabel = new ArrayList<JLabel>();
		int numberOfLabels = coordinate.getYTicks();
		int yMax = coordinate.getYMax();
		int interval = coordinate.getYInterval();
		double interval2 = 0.0;
		int unit = (int) coordinate.getYTickUnit();
		String label = "K";
		yLabel.add(new JLabel("0"));
		add(yLabel.get(0));
		Dimension size = yLabel.get(0).getPreferredSize();
		yLabel.get(0).setBounds(origin.x - 35, origin.y - 8, size.width, size.height);
		if (numberOfLabels != 0){
			for (int i = 1; i <= numberOfLabels; i++){
				yLabel.add(new JLabel(Integer.toString(i * interval)+label));
				add(yLabel.get(i));
				size = yLabel.get(i).getPreferredSize();
				yLabel.get(i).setBounds(origin.x - 35, origin.y - i * unit - 8, size.width, size.height);
			}
		}
	}
	
	public void updateLabels(){
		for (int i = 0; i < xLabel.size(); i++)
			remove(xLabel.get(i));
		for (int j = 0; j < yLabel.size(); j++)
			remove(yLabel.get(j));
		setupXlabels();
		setupYlabels();
	}
	
	public void setHost(Host host, Integer time){
		coordinate.setPlot(host, time);
		isDrawingGraph = true;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		coordinate.setupXaxis(g);
		coordinate.setupYaxis(g);
		coordinate.setupXticks(g);
		coordinate.setupYticks(g);
		if (isDrawingGraph)
			coordinate.drawingPlot(g);
	}

}