package UI;

import exceptions.FileFormatNotRecognisedException;
import exceptions.TableOverflow;
import logic.Config;
import logic.DataTable;
import logic.FileManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <h1>Visualizer</h1>
 * <p>Window to view all the data</p>
 *
 * @author D4vsus
 */
public class Visualizer extends JFrame{

    //variables and objects
    private JPanel visualizeWindow;
    private JButton previousView;
    private JButton nextView;
    private JPanel viewGraphicsNColumns;
    private JScrollPane scrollColumns;
    private JTable tableView;
    private JLabel numTableView;

    private final DataTable table;
    private int rowsCursor;
    private int size;

    //methods

    /**
     * <h1>Visualizer()</h1>
     * <p>Initialize the class</p>
     *
     * @param table {@link DataTable}
     * @param parentFrame {@link DataTable}
     * @throws TableOverflow : if they get out of the table
     */
    public Visualizer(@NotNull DataTable table,JFrame parentFrame) throws TableOverflow {

        this.table = table;
        size = table.size();
        rowsCursor = 0;

        //set the properties of the window
        setTitle("Visualizer");
        try {
            this.setIconImage(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("sealIcon.png")))).getImage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
        setBounds(100,100,600,400);
        add(visualizeWindow);
        setResizable(true);
        setUndecorated(false);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        loadTable();

        //menu
        JMenuBar menu = new JMenuBar();

        JMenuItem credits = new JMenuItem("Configuration",'c');
        credits.addActionListener(e->openConfiguration());
        credits.setToolTipText("Configuration View (Alt + C)");
        menu.add(credits);

        setJMenuBar(menu);

        //listeners
        nextView.addActionListener(e-> nextTable());
        previousView.addActionListener(e-> previousTable());

        this.visualizeWindow.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                fileDropper(evt);
            }
        });

        //keyboard shortcut
        visualizeWindow.registerKeyboardAction(e -> nextTable(),KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        visualizeWindow.registerKeyboardAction(e -> previousTable(),KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //set closing action
        addWindowListener(new WindowAdapter(){
            //when close the window, set visible again the parent
            @Override
            public void windowClosing(WindowEvent e){
                parentFrame.setVisible(true);
                dispose();
            }
        });

        //set visible
        parentFrame.setVisible(false);
        setVisible(true);
    }

    /**
     * <h1>openConfiguration()</h1>
     * <p>Open the configuration of the visualizer</p>
     */
    private void openConfiguration(){
        new ConfigVisualizeWindow();
        try {
            loadTable();
        } catch (TableOverflow e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h1>nextTable()</h1>
     * <p>get the next tab of the table</p>
     */
    private void nextTable()  {
        try {
            if (((rowsCursor + 1) * Config.getRowsToVisualize()) < size) {
                rowsCursor++;
                loadTable();
            }
        } catch (TableOverflow e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>previousTable()</h1>
     * <p>get the previous tab of the table</p>
     */
    private void previousTable() {
        try {
            if (rowsCursor > 0) {
                rowsCursor--;
                loadTable();
            }
        } catch (TableOverflow e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>loadTable()</h1>
     * <p>update or load the tab of the table</p>
     *
     * @throws TableOverflow :if they get put of the table
     */
    public void loadTable() throws TableOverflow {
        //all cells false
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        //load attributes names
        for (AttributeItem attribute:table.getAttributes()) {
            tableModel.addColumn(attribute.getAttributeName());
        }

        //load the data
        for (int x = rowsCursor * Config.getRowsToVisualize();x < Config.getRowsToVisualize()+(rowsCursor*Config.getRowsToVisualize()) && x < size;x++){
            tableModel.addRow(table.getRow(x));
        }

        numTableView.setText((rowsCursor + 1)+"/"+(size/Config.getRowsToVisualize() + ((size%Config.getRowsToVisualize() > 0)?1:0)));

        tableView.setModel(tableModel);

        nextView.setEnabled(((rowsCursor + 1) * Config.getRowsToVisualize()) < size);
        previousView.setEnabled(rowsCursor > 0);
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
            //create the filter
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    String.join(",", FileManager.SUPPORTEDFORMATS),FileManager.SUPPORTEDFORMATS);

            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
                if (filter.accept(file) && !file.isDirectory()) {
                    FileManager.loadFile(table, file.getCanonicalPath());
                    size = table.size();
                    loadTable();
                }
                else {
                    table.clearAll();
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
}
