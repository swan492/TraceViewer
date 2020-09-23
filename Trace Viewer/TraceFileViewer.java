import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class TraceFileViewer extends JFrame implements ItemListener {
    
    private DrawingPanel panel = new DrawingPanel();
    private Host source; 
	private Host destination; 
    private ArrayList<Host> sourceHosts; 
	private ArrayList<Host> destinationHosts; 
	private JComboBox<String> hostComboBox;
	private int currentHostIndex = -1;
	private Font font;
	private JPanel radioButtonPanel;
	private ButtonGroup radioButtons;
	private JRadioButton radioButtonSourceHost;
	private JRadioButton radioButtonDestinationHost;

    public TraceFileViewer(){
    	super("Trace File Viewer");
    	setLayout(null);
		setSize(1000, 500);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	font = new Font("Sans-serif", Font.PLAIN, 20);
    	setupMenu();
		setupButtonGroup();
		setupComboBox();
		setupPanel();
		setVisible(true);
    }

    private void setupMenu(){
    	JMenuBar menuBar = new JMenuBar();
    	setJMenuBar(menuBar);
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic('F');
    	fileMenu.setFont(font);
    	menuBar.add(fileMenu);
    	JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
    	fileMenuOpen.setFont(font);
    	fileMenuOpen.addActionListener(
    		new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e){
					sourceHosts = new ArrayList<Host>();
					destinationHosts = new ArrayList<Host>();
					Scanner input = null;
    				JFileChooser fileChooser = new JFileChooser(".");
    				int retval = fileChooser.showOpenDialog(TraceFileViewer.this);
    				if (retval == JFileChooser.APPROVE_OPTION) {
    					File f = fileChooser.getSelectedFile();
						try{
							input = new Scanner(f);
						}
						catch (IOException ioExc) { return; }
						while (input.hasNext()){
							String currentLine = input.nextLine();
							String[] split = currentLine.split("\\t");
							if (split.length >= 8) {
								if (!(split[1].equals(""))) {
									if (!(split[2].equals(""))) {
										if (!(split[4].equals(""))) {
											if (!(split[7].equals(""))) {
												source = new Host(split[2]);
												destination = new Host(split[4]); 
												if (sourceHosts.contains(source)){
													source = sourceHosts.get(sourceHosts.indexOf(source));
													source.appendTimeAndAmount(split[1], split[7]);
												}
												else {
													sourceHosts.add(source);
													source.appendTimeAndAmount(split[1], split[7]);
												}
												if (destinationHosts.contains(destination)){
													destination = destinationHosts.get(destinationHosts.indexOf(destination));
													destination.appendTimeAndAmount(split[1], split[7]);
												}
												else {
													destinationHosts.add(destination);
													destination.appendTimeAndAmount(split[1], split[7]);
												}
											}
										}
									}
								}
							}
						}
						Collections.sort(sourceHosts);
						Collections.sort(destinationHosts);
						radioButtonSourceHost.setSelected(true);
						currentHostIndex = 0;
						updateComboBox(sourceHosts);
						updateDrawingPanel(sourceHosts.get(currentHostIndex));
    				}
    			}
    		}
    	);
    	fileMenu.add(fileMenuOpen);
    	JMenuItem fileMenuQuit = new JMenuItem("Quit");
    	fileMenuQuit.setFont(font);
    	fileMenu.add(fileMenuQuit);
    	fileMenuQuit.addActionListener(
    		new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e){
    				System.exit(0);
    			}
    		}
    	);
    }

    private void setupButtonGroup(){
    	radioButtonPanel = new JPanel();
    	radioButtonPanel.setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = GridBagConstraints.RELATIVE;
    	c.anchor = GridBagConstraints.WEST;
    	radioButtons = new ButtonGroup();
    	radioButtonSourceHost = new JRadioButton("Source hosts");
    	radioButtonSourceHost.setFont(font);
    	radioButtonSourceHost.setSelected(true);
		radioButtonSourceHost.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						if (sourceHosts.size() > 0){
							currentHostIndex = 0;
							updateComboBox(sourceHosts);
							updateDrawingPanel(sourceHosts.get(currentHostIndex));
						}
						else
							return;
					}
				}
		);
    	radioButtons.add(radioButtonSourceHost);
    	radioButtonPanel.add(radioButtonSourceHost, c);
    	radioButtonDestinationHost = new JRadioButton("Destination hosts");
    	radioButtonDestinationHost.setFont(font);
		radioButtonDestinationHost.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						if (destinationHosts.size() > 0){
							currentHostIndex = 0;
							updateComboBox(destinationHosts);
							updateDrawingPanel(destinationHosts.get(currentHostIndex));
						}
						else
							return;
					}
				}
		);
    	radioButtons.add(radioButtonDestinationHost);
    	radioButtonPanel.add(radioButtonDestinationHost, c);
		add(radioButtonPanel);
		radioButtonPanel.setBounds(0, 0, 200, 100);
    }

    private void setupComboBox(){
    	hostComboBox = new JComboBox<String>();
    	hostComboBox.setModel((MutableComboBoxModel)
    		new DefaultComboBoxModel());
    	hostComboBox.setMaximumRowCount(8);
    	hostComboBox.addItemListener(this);
    	hostComboBox.setFont(font);
    	hostComboBox.setVisible(false);
    	add(hostComboBox);
		hostComboBox.setBounds(300, 20, 300, 30);
    }

	private void setupPanel(){
		add(panel);
		panel.setBounds(0, 100, 1000, 325);
		panel.repaint();
	}

	private void updateComboBox(ArrayList<Host> hosts) {
		hostComboBox.removeAllItems();
		for (Host hostName : hosts) {
			hostComboBox.addItem(hostName.getHost());
		}
		hostComboBox.setSelectedIndex(currentHostIndex);
		hostComboBox.setVisible(true);
	}
	
	private void updateDrawingPanel(Host host){
		panel.updateLabels(); 
		panel.setHost(host, source.getTime());
		panel.repaint();
	}
	
	public void itemStateChanged(ItemEvent e) {
    	if (e.getStateChange() == ItemEvent.SELECTED) {
			if (radioButtonSourceHost.isSelected())
				updateDrawingPanel(sourceHosts.get(hostComboBox.getSelectedIndex()));
			else
				updateDrawingPanel(destinationHosts.get(hostComboBox.getSelectedIndex()));
    	}
    	return;
    }
    
}
