package UI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileSelector extends JFrame{

    //variables and Objects
    private String path;
    private JPanel fileSelector;
    private JTextField pathTe;
    private JTree pathTr;
    private JButton importButton;
    private JButton cancelButton;

    //methods
    public FileSelector(){
        this.setBounds(100,100,250,100);
        this.add(this.fileSelector);
        this.path = ".";
        this.pathTe.setText(this.path);

        this.cancelButton.addActionListener(e->{
            this.dispose();
        });

        this.importButton.addActionListener(e->{

            this.dispose();
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new FileSelector();
    }
}
