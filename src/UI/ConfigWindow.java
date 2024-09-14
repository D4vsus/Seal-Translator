package UI;

import logic.Config;

import javax.swing.*;
import java.awt.event.*;

/**
 * <h1>ConfigWindow</h1>
 * <p>Open the configuration window</p>
 *
 * @author D4vsus
 */
public class ConfigWindow extends JDialog {

    //variables and objects
    private JPanel content;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel okCancelApplyPane;
    private JPanel optionsPane;
    private JPanel buttonsPanel;
    private JButton buttonApply;
    private JRadioButton deleteCSComments;
    private JScrollPane scroll;

    //methods
    /**
     * <h1>Constructor</h1>
     * <p>Initialize the class and set it visible</p>
     */
    public ConfigWindow() {

        //Set up the window
        setContentPane(content);
        setBounds(100,100,500,250);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Logic Code
        setDefaultConfiguration();

        //Listeners
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onApply();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        content.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call onApply on A
        content.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onApply();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
        Config.setDeleteCSComments(deleteCSComments.isSelected());
    }

    /**
     * <h1>setDefaultConfiguration()</h1>
     * <p>Get the configuration from Config class</p>
     */
    private void setDefaultConfiguration(){
        deleteCSComments.setSelected(Config.isDeleteCSComments());
    }
}
