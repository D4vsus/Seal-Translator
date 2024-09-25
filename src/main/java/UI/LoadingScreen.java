package UI;
import javax.swing.*;
import java.awt.*;

/**
 * <h1>LoadingScreen</h1>
 * <p>create a loading screen</p>
 *
 * @author D4vsus
 */
public class LoadingScreen extends JFrame{

    //variables and objects
    private final JProgressBar progressBar;

    //methods

    /**
     * <h1>LoadingScreen()</h1>
     * <p>Initialize a defined loading bar</p>
     *
     * @param max : int
     */
    public LoadingScreen(int max) {
        // Set up the frame
        setTitle("Loading...");
        setSize(400, 75);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a progress bar
        progressBar = new JProgressBar(0, max);
        progressBar.setValue(0);
        progressBar.setString("("+0+"/"+max+")");
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);

        // Make it visible
        setVisible(true);
    }

    /**
     * <h1>LoadingScreen</h1>
     * <p>Initialize a undefined loading bar</p>
     */
    public LoadingScreen() {
        // Set up the frame
        setTitle("Loading...");
        setSize(400, 75);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Work in progress...");
        add(progressBar, BorderLayout.CENTER);

        // Make it visible
        setVisible(true);
    }

    /**
     * <h1>update</h1>
     * <p>update the bar if it's defined</p>
     */
    public void update(){
        if (!progressBar.isIndeterminate()) {
            progressBar.setValue(progressBar.getValue() + 1);
            progressBar.setString("(" + progressBar.getValue() + "/" + progressBar.getMaximum() + ")");
            if (progressBar.getValue() >= progressBar.getMaximum()) {
                dispose();
            }
        }
    }
}