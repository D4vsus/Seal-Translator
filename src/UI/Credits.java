package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Credits extends JDialog{
    private JPanel credits;
    private JLabel madeBy;
    private JLabel bidusaImg;
    private JLabel creator;

    public Credits() {
        add(credits);
        setBounds(100,100,500,300);
        setTitle("Credits");
        setResizable(false);
        setIconImage(new ImageIcon("res/BIDUSA.jpg").getImage());
        setModal(true);
            creator.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bidusaImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            creator.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI ("https://github.com/D4vsus"));
                        } catch (IOException a) {
                            JOptionPane.showMessageDialog(null, "Error: the browser can't be open");
                        } catch (URISyntaxException e) {
                            JOptionPane.showMessageDialog(null, "Error: the URL doesn't exist");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: the OS don't support open the browser");
                    }
                }
            });

            bidusaImg.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://www.profesorescooperantes.org/bidusa"));
                        } catch (IOException a) {
                            JOptionPane.showMessageDialog(null, "Error: the browser can't be open");
                        } catch (URISyntaxException e) {
                            JOptionPane.showMessageDialog(null, "Error: the URL doesn't exist");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: the OS don't support open the browser");
                    }
                }
            });
        setVisible(true);
    }
}
