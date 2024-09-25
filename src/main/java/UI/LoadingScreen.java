package UI;
import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame implements Runnable{

    private final JProgressBar progressBar;

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
        progressBar.setString("Work in progres...");
        add(progressBar, BorderLayout.CENTER);

        // Make it visible
        setVisible(true);
    }

    public void update(){
        if (!progressBar.isIndeterminate()) {
            progressBar.setValue(progressBar.getValue() + 1);
            progressBar.setString("(" + progressBar.getValue() + "/" + progressBar.getMaximum() + ")");
            if (progressBar.getValue() >= progressBar.getMaximum()) {
                dispose();
            }
        }
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    @Override
    public void run() {
        while (progressBar.getMaximum() > progressBar.getValue()) {
            this.update();
            System.out.println("asd");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}