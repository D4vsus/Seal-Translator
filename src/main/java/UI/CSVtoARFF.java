package UI;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatTextBorder;
import exceptions.*;
import logic.Attribute;
import logic.DataTable;
import logic.FileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * <h1>CSVtoARFF</h1>
 * <p>The main window and where the main logic is in it</p>
 *
 * @author D4vsus
 */
public class CSVtoARFF extends JFrame{

    //variables and objects

    //logic components
    private String fileName;
    private final ArrayList<AttributeItem> dataAttributes;
    private final DataTable table;

    //UI components
    private JPanel mainWindow;
    private JButton importCSVb;
    private JButton exportARFFb;
    private JPanel preprocess;
    private JTextField datasetName;
    private JScrollPane scroll;
    private JPanel attribute;
    private final GridBagConstraints layout;

    //methods

    /**
     * <h1>CSVtoARFF()</h1>
     * <p>Instantiate the window</p>
     */
    public CSVtoARFF(){
        //initialize not graphical objects
        this.dataAttributes = new ArrayList<>();
        this.table = new DataTable();
        this.fileName = "";

        //set the properties of the window
        this.setBounds(100,100,500,250);
        this.setTitle("Seal Translator");
        this.add(mainWindow);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            this.setIconImage(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("sealIcon.png")))).getImage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }

        //add the layout
        this.layout = new GridBagConstraints();
        this.layout.fill = GridBagConstraints.VERTICAL;
        this.layout.gridy = 0;

        //add the menu bar
        JMenuBar menu = new JMenuBar();

        JMenuItem credits = new JMenuItem("Credits",'c');
        credits.addActionListener(e->new Credits());
        credits.setToolTipText("Credits (Alt + C)");
        menu.add(credits);

        JMenuItem addComment = new JMenuItem("Add Comment",'a');
        addComment.addActionListener(e->openComment());
        addComment.setToolTipText("Add Comment (Alt + A)");
        menu.add(addComment);

        JMenuItem configuration = new JMenuItem("Configuration",'c');
        configuration.addActionListener(e->new ConfigWindow());
        configuration.setToolTipText("configuration (Ctrl + A)");
        menu.add(configuration);

        this.setJMenuBar(menu);

        //add mnemonics
        credits.setMnemonic(KeyEvent.VK_C);
        addComment.setMnemonic(KeyEvent.VK_A);
        this.importCSVb.setMnemonic(KeyEvent.VK_I);
        this.exportARFFb.setMnemonic(KeyEvent.VK_E);

        //add menu shortcuts
        this.mainWindow.registerKeyboardAction(e -> new Credits(),      KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.mainWindow.registerKeyboardAction(e -> openComment(),      KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.mainWindow.registerKeyboardAction(e -> new ConfigWindow(), KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //add listeners
        this.importCSVb.addActionListener(e->{
            try {
                openFileChooser();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            }
        });

        this.exportARFFb.addActionListener(e->{
            try {
                exportARFF(this.datasetName.getText(),this.dataAttributes);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            }
        });

        this.attribute.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                fileDropper(evt);
            }
        });

        //show the window
        this.setVisible(true);
    }

    /**
     * <h1>setCSV()</h1>
     * <p>Set the file name</p>
     *
     * @param fileName : {@link String}
     */
    public void setCSV(String fileName){
        this.fileName = fileName;
    }

    /**
     * <h1>addAttribute()</h1>
     * <p>Add an attribute to the dataAttributes record and the main window</p>
     *
     * @param attributeItem : {@link AttributeItem}
     */
    public void addAttribute(AttributeItem attributeItem) throws DuplicatedNameException {
        //moves down the layout
        this.layout.gridy += 1;
        for (AttributeItem comparer:this.dataAttributes) {
            if (comparer.getAttributeName().equals(attributeItem.getAttributeName())) {
                throw new DuplicatedNameException(attributeItem.getAttributeName());
            }
        }
        this.attribute.add(attributeItem.getPanel(),this.layout);
        this.dataAttributes.add(attributeItem);
    }

    /**
     * <h1>openComment()</h1>
     * <p>open the comment window</p>
     */
    private void openComment(){
        Comment commentWindow = new Comment(new StringBuilder(table.getComments()));
        table.setComments(commentWindow.getComment());
    }

    /**
     * <h1>loadCSV()</h1>
     * <p>Load the data to the program</p>
     *
     * @param path : {@link String}
     */
    public void loadFile(String path){
        try{
            //Preprocess
            //reset the attribute panel
            this.layout.gridy = 0;
            this.dataAttributes.clear();
            this.attribute.removeAll();
            this.scroll.revalidate();
            this.preprocess.revalidate();
            this.exportARFFb.setEnabled(true);

            //Process
            //get the name of the file without the extension CSV
            this.fileName = path.split("\\.")[0];
            FileManager.loadFile(table,path);
            for (Attribute attribute : table.getAttributes()) {
                addAttribute(new AttributeItem(attribute));
            }

            this.scroll.revalidate();

        } catch (DuplicatedNameException e) {
            JOptionPane.showMessageDialog(this,e.toString(),"Duplicated Name",JOptionPane.ERROR_MESSAGE,null);
            this.exportARFFb.setEnabled(false);
        }  catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,"The file didn't exist or didn't found","File not found",JOptionPane.ERROR_MESSAGE,null);
            this.exportARFFb.setEnabled(false);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            this.exportARFFb.setEnabled(false);
        }
    }

    /**
     * <h1>exportARFF()</h1>
     * <p>Create the arff file and write it's content</p>
     *
     * @param nameDataset : {@link String}
     * @param dataTypes : {@link ArrayList}<{@link AttributeItem}>
     */
    public void exportARFF(String nameDataset,ArrayList<AttributeItem> dataTypes) {
        try {
            if (datasetName.getText().isBlank()) throw new NoDatasetNameException();
            table.setRelation(datasetName.getText());
            ArrayList<String> attributesType = new ArrayList<>();
            for (AttributeItem dataType : dataAttributes){
                attributesType.add(dataType.getAttributeTypeARFF());
            }
            table.loadARFFAttributes(attributesType.toArray(new String[0]));
            FileManager.exportARFF(table,fileName);

            datasetName.setBorder(new FlatTextBorder());
            JOptionPane.showMessageDialog(this, "The field has been created", "Created", JOptionPane.INFORMATION_MESSAGE, null);

        } catch (NoDatasetNameException e){
            datasetName.setBorder(new LineBorder(new Color(255, 0, 0),2));
            JOptionPane.showMessageDialog(this,e.toString(),"Not Selected Attribute",JOptionPane.ERROR_MESSAGE,null);
        } catch (NotSelectedAttributeException e) {
            JOptionPane.showMessageDialog(this,e.toString(),"Not Selected Attribute",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>fileDropper()</h1>
     * <p>set the drop file to the attribute panel</p>
     *
     * @param evt : {@link DropTargetDropEvent}
     */
    private void fileDropper(DropTargetDropEvent evt){
        try {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
                if (Arrays.stream(FileManager.SUPPORTEDFORMATS).toList().contains(file.getCanonicalPath().trim().toLowerCase().split("\\.")[1])) loadFile(file.getAbsolutePath());
                else {
                    throw new FileFormatNotRecognisedException();
                }
            }
            evt.dropComplete(true);
        } catch (ClassCastException | UnsupportedFlavorException ex){
            JOptionPane.showMessageDialog(this,"Error: you haven't drop a file. Please drop a csv","Error",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>openFileChooser()</h1>
     * <p>Open the file chooser and import it</p>
     *
     * @throws IOException
     * @throws ImportException
     */
    private void openFileChooser() throws IOException, ImportException {
        //initialize File Chooser
        JFileChooser chooser = new JFileChooser();

        //open file format supported
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                String.join(",",FileManager.SUPPORTEDFORMATS),FileManager.SUPPORTEDFORMATS);
        chooser.setFileFilter(filter);

        //see if they selected a file
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.fileName = chooser.getSelectedFile().getCanonicalPath();
            if (!this.fileName.isBlank()) loadFile(this.fileName);
        } else if (returnVal == JFileChooser.ERROR_OPTION) {
            throw new ImportException();
        }
    }
}
