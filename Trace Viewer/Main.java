
import javax.swing.SwingUtilities;

public class Main implements Runnable {
	
    public void run(){
        TraceFileViewer tfv = new TraceFileViewer();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
   
}