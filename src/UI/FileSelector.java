package UI;

import exceptions.FileNotCSVException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * <h1>FileSelector</h1>
 * <p>This class create a window to search for the files</p>
 * @author D4vsus
 */
public class FileSelector extends JDialog{

    //variables and Objects
    private File actualDirectory;
    private JPanel fileSelector;
    private JTextField pathText;
    private JTree pathTree;
    private JButton importButton;
    private JButton cancelButton;
    private JButton backButton;
    private JScrollPane scrollTree;
    private final CSVtoARFF mainWindow;

    //methods

    /**
     * <h1>FileSelector</h1>
     * <p>Create an instance of the class</p>
     * @param mainWindow : {@link CSVtoARFF}
     */
    public FileSelector(CSVtoARFF mainWindow) throws IOException {
        //Initialize the window
        this.setBounds(100,100,500,250);
        this.add(this.fileSelector);
        this.setModal(true);

        //Set up
        this.mainWindow = mainWindow;
        this.actualDirectory = new File(".");
        this.pathText.setText(actualDirectory.getCanonicalPath());
        this.pathTree.setScrollsOnExpand(true);
        this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder(actualDirectory.getCanonicalPath())));

        //Add Mnemonic
        this.cancelButton.setMnemonic('c');
        this.backButton.setMnemonic('b');
        this.importButton.setMnemonic('i');

        //Add listeners
        this.pathTree.addTreeSelectionListener(e->{
            this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder((e.getPath().getLastPathComponent().toString()))));
            this.pathText.setText(e.getPath().getLastPathComponent().toString());
        });

        this.pathText.addActionListener(e -> enterAction());

        this.backButton.addActionListener(e ->getBack());

        this.cancelButton.addActionListener(e-> this.dispose());

        this.importButton.addActionListener(e-> {
            try {
                this.mainWindow.setCSV(getSelectedFilePath());
            } catch (FileNotCSVException ex) {
                JOptionPane.showMessageDialog(this,ex.toString(),"File not CSV",JOptionPane.ERROR_MESSAGE,null);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            }
            this.dispose();
        });

        //Set visible
        setVisible(true);
    }

    /**
     * <h1>enterAction()</h1>
     * <p>Executed when press enter in the text field</br>Open the field or directory wrote</p>
     * @author D4vsus
     */
    private void enterAction() {
        File fileToSearch;
        try {
            if ((fileToSearch = new File(this.pathText.getText())).exists()){
                try {
                    this.actualDirectory = fileToSearch;
                    this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder(actualDirectory.getCanonicalPath())));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,e.toString(),"File not found",JOptionPane.ERROR_MESSAGE,null);
                }
            }
        }   catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>getActualTreeFolder()</h1>
     * <p>Updates the tree files</p>
     * @param path : {@link String}
     * @return {@link TreeNode}
     */
    private TreeNode getActualTreeFolder(String path){
        File actualFile = new File(path);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(actualFile.getName());
        if (actualFile.isDirectory()) {
            for (File subFiles : Objects.requireNonNull(actualFile.listFiles())) {
                try {
                    root.add(new DefaultMutableTreeNode(subFiles.getCanonicalPath()));
                    this.actualDirectory = actualFile;
                    this.pathText.setText(actualDirectory.getCanonicalPath());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,e.toString(),"File not found",JOptionPane.ERROR_MESSAGE,null);
                }
            }
        } else {
            try {
                root.add(new DefaultMutableTreeNode(actualFile.getCanonicalPath()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,e.toString(),"File not found",JOptionPane.ERROR_MESSAGE,null);
            }
        }
        return root;
    }

    /**
     * <h1>getBack()</h1>
     * <p>Move back to the previous directory</p>
     * @author D4vsus
     */
    private void getBack(){
        try {
            this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder(actualDirectory.getParentFile().getCanonicalPath())));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,ex.toString(),"File not found",JOptionPane.ERROR_MESSAGE,null);
        } catch (NullPointerException ignored){

        }
    }

    /**
     * <h1>getSelectedFilePath()</h1>
     * <p>Return the actual file path</p>
     * @return {@link }
     * @author D4vsus
     */
    public String getSelectedFilePath() throws FileNotCSVException {
        String fileName = this.pathText.getText();
        if (fileName.trim().toLowerCase().contains(".csv"))return fileName;
        else throw new FileNotCSVException();
    }
}
