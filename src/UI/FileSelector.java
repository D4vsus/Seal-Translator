package UI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
            this.mainWindow.setCSV(this.pathText.getText());
            this.dispose();
        });

        //Set visible
        setVisible(true);
    }

    private void enterAction() {
        File fileToSearch;
        if ((fileToSearch = new File(this.pathText.getText())).exists()){
            try {
                this.actualDirectory = fileToSearch;
                this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder(actualDirectory.getCanonicalPath())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                root.add(new DefaultMutableTreeNode(actualFile.getCanonicalPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return root;
    }

    private void getBack(){
        try {
            this.pathTree.setModel(new DefaultTreeModel(getActualTreeFolder(actualDirectory.getParentFile().getCanonicalPath())));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (NullPointerException ignored){

        }
    }

    public String getSelectedFilePath(){
        String fileName = this.pathText.getText();
        if (fileName.trim().contains(".CSV")) return fileName;
        else return null;
    }
}
