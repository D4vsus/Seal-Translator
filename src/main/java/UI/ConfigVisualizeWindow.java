package UI;

import exceptions.BatchFormatException;
import logic.AutoAssign;
import logic.Config;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

/**
 * <h1>ConfigVisualizeWindow</h1>
 * <p>open the configuration from the visualizer</p>
 *
 * @author D4vsus
 */
public class ConfigVisualizeWindow extends JDialog{

    //variables and objects
    private JPanel okCancelApplyPane;
    private JPanel buttonsPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonApply;
    private JScrollPane scroll;
    private JPanel optionPanel;
    private JTextField batchViewTextField;
    private JPanel content;

    //methods

    /**
     * <h1>ConfigVisualizeWindow()</h1>
     * <p>Initialize the window</p>
     */
    public ConfigVisualizeWindow() {

        //Set up the window
        setContentPane(content);
        setBounds(100, 100, 500, 250);
        setTitle("Configuration");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Logic Code
        setDefaultConfiguration();
        addListeners();

        // call onCancel() on ESCAPE
        content.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call onApply on A
        content.registerKeyboardAction(e -> onApply(), KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setVisible(true);
    }

    /**
     * <h1>onApply()</h1>
     * <p>Apply the configuration when click apply</p>
     */
    private void onApply(){
        applyConfiguration();
    }

    /**
     * <h1>onOk()</h1>
     * <p>Apply the configuration when click ok</p>
     */
    private void onOK() {
        applyConfiguration();
        dispose();
    }

    /**
     * <h1>onCancel()</h1>
     * <p>Apply the configuration when click Cancel</p>
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * <h1>applyConfiguration()</h1>
     * <p>Apply the configuration to the config class</p>
     */
    private void applyConfiguration(){
        try {
            BatchFormat();
            Config.setRowsToVisualize(Integer.parseInt(batchViewTextField.getText()));
        } catch (BatchFormatException e) {
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>setDefaultConfiguration()</h1>
     * <p>Get the configuration from Config class</p>
     */
    private void setDefaultConfiguration(){
        batchViewTextField.setText(String.valueOf(Config.getRowsToVisualize()));
    }

    /**
     * <h1>addListeners()</h1>
     * <p>Add all the listeners</p>
     */
    private void addListeners(){
        //Listeners
        buttonOK.addActionListener(e -> onOK());

        buttonApply.addActionListener(e -> onApply());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    /**
     * <h1>BatchFormat()</h1>
     * <p>See if the batch is written correctly</p>
     *
     * @throws BatchFormatException : if the format not match
     */
    private void BatchFormat() throws BatchFormatException {
        // natural numbers from 1-infinite
        String pattern = "^[1-9]\\d*$";

        //if not match throw the exception
        if(!Pattern.compile(pattern).matcher(batchViewTextField.getText()).matches()){
            throw new BatchFormatException();
        } else {
            AutoAssign.setBatch(batchViewTextField.getText().trim().toLowerCase());
        }
    }
}
