package UI;

import exceptions.FileFormatNotRecognisedException;
import exceptions.TableOverflow;
import logic.Config;
import logic.DataTable;
import logic.FileManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * <h1>Visualizer</h1>
 * <p>Window to view all the data</p>
 *
 * @author D4vsus
 */
public class Visualizer {

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
     * @throws TableOverflow : if they get out of the table
     */
    public Visualizer(@NotNull DataTable table) throws TableOverflow {

        this.table = table;
        size = table.size();
        rowsCursor = 0;

        loadTable();
        addListeners();
        addShortCuts();
    }

    /**
     * <h1>addShortCuts()</h1>
     * <p>Add all the shortcuts to the window</p>
     */
    public void addShortCuts(){
        visualizeWindow.registerKeyboardAction(e -> getBackToMainWindow(),KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        visualizeWindow.registerKeyboardAction(e -> openConfiguration(),KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        visualizeWindow.registerKeyboardAction(e -> nextTable(),KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        visualizeWindow.registerKeyboardAction(e -> previousTable(),KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * <h1>addListeners()</h1>
     * <p>Add all the listeners to the window</p>
     */
    private void addListeners(){
        nextView.addActionListener(e-> nextTable());
        previousView.addActionListener(e-> previousTable());

        visualizeWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                try {
                    size = table.size();
                    rowsCursor = 0;
                    loadTable();
                } catch (TableOverflow ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        visualizeWindow.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                fileDropper(evt);
            }
        });
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
            JOptionPane.showMessageDialog(visualizeWindow,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
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
            JOptionPane.showMessageDialog(visualizeWindow,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>getBackToMainWindow()</h1>
     * <p>set visible again the attribute window</p>
     */
    private void getBackToMainWindow(){
        ((CardLayout)visualizeWindow.getParent().getLayout()).show(visualizeWindow.getParent(),CSVtoARFF.getPanelID());
    }

    /**
     * <h1>getVisualizerMenu()</h1>
     * <p>return the visualize menu of the visualizer</p>
     *
     * @return {@link JMenuBar}
     */
    public JMenuBar getVisualizerMenu(){
        //menu
        JMenuBar menu = new JMenuBar();

        JMenuItem configuration = new JMenuItem("Configuration",'c');
        configuration.addActionListener(e->openConfiguration());
        configuration.setToolTipText("Configuration View (Alt + C)");
        menu.add(configuration);

        JMenuItem attributes = new JMenuItem("Attributes",'a');
        attributes.addActionListener(e->getBackToMainWindow());
        attributes.setToolTipText("Get you back to Attribute Window (Alt + A)");
        menu.add(attributes);

        attributes.setMnemonic(KeyEvent.VK_A);
        configuration.setMnemonic(KeyEvent.VK_C);

        return menu;
    }

    /**
     * <h1>loadTable()</h1>
     * <p>update or load the tab of the table</p>
     *
     * @throws TableOverflow :if they get put of the table
     */
    public void loadTable() throws TableOverflow {
        size = table.size();
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
        tableView.getTableHeader().setReorderingAllowed(false);

        nextView.setEnabled(((rowsCursor + 1) * Config.getRowsToVisualize()) < size);
        previousView.setEnabled(rowsCursor > 0);
    }

    /**
     * <h1>getVisualizeWindow()</h1>
     * <p>return the Visualizer panel</p>
     *
     * @return {@link JPanel}
     */
    public JPanel getVisualizeWindow(){
        return visualizeWindow;
    }

    /**
     * <h1>getPanelID()</h1>
     * <p>Return the panel ID</p>
     *
     * @return {@link String} : ID of the panel
     */
    public static String getPanelID(){
        return "Visualizer";
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
            JOptionPane.showMessageDialog(visualizeWindow,"Error: you haven't drop a file. Please drop a csv","Error",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(visualizeWindow,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }
}
