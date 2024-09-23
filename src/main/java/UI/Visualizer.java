package UI;

import exceptions.TableOverflow;
import logic.DataTable;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
    private int rowsToVisualize;
    private int rowsCursor;
    private DefaultTableModel tableModel;
    private int size;

    //methods

    /**
     * <h1>Visualizer()</h1>
     * <p>Initialize the class</p>
     *
     * @param table {@link DataTable}
     * @param parentFrame {@link DataTable}
     * @throws TableOverflow
     */
    public Visualizer(@NotNull DataTable table,JFrame parentFrame) throws TableOverflow {

        this.table = table;
        size = table.size();
        rowsToVisualize = 20;
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

        //listeners
        nextView.addActionListener(e-> {
            try {
                nextTable();
            } catch (TableOverflow ex) {
                throw new RuntimeException(ex);
            }
        });
        previousView.addActionListener(e-> {
            try {
                previousTable();
            } catch (TableOverflow ex) {
                throw new RuntimeException(ex);
            }
        });

        //keyboard shortcut
        visualizeWindow.registerKeyboardAction(e -> {
            try {
                nextTable();
            } catch (TableOverflow ex) {
                throw new RuntimeException(ex);
            }
        },      KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        visualizeWindow.registerKeyboardAction(e -> {
            try {
                previousTable();
            } catch (TableOverflow ex) {
                throw new RuntimeException(ex);
            }
        },      KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
     * <h1>nextTable()</h1>
     * <p>get the next tab of the table</p>
     *
     * @throws TableOverflow
     */
    private void nextTable() throws TableOverflow {
        if (((rowsCursor + 1) * rowsToVisualize) < size) {
            rowsCursor++;
            loadTable();
        }
    }

    /**
     * <h1>previousTable()</h1>
     * <p>get the previous tab of the table</p>
     *
     * @throws TableOverflow
     */
    private void previousTable() throws TableOverflow {
        if (rowsCursor > 0) {
            rowsCursor--;
            loadTable();
        }
    }

    /**
     * <h1>loadTable()</h1>
     * <p>update or load the tab of the table</p>
     *
     * @throws TableOverflow
     */
    private void loadTable() throws TableOverflow {
        tableModel = new DefaultTableModel() {
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
        for (int x = rowsCursor*rowsToVisualize;x < rowsToVisualize+(rowsCursor*rowsToVisualize) && x < size;x++){
            tableModel.addRow(table.getRow(x));
        }

        numTableView.setText((rowsCursor+1)+"/"+(size/rowsToVisualize + ((size%rowsToVisualize > 0)?1:0)));

        tableView.setModel(tableModel);

        nextView.setEnabled(((rowsCursor + 1) * rowsToVisualize) < size);
        previousView.setEnabled(rowsCursor > 0);
    }
}
