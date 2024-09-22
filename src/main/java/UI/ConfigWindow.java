package UI;

import exceptions.BatchFormatException;
import logic.AutoAssign;
import logic.Config;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

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
    private JPanel buttonsPanel;
    private JButton buttonApply;
    private JScrollPane scroll;
    private JPanel optionPanel;
    private JRadioButton deleteCSComments;
    private JRadioButton autoAssign;
    private JTextField batchAutoAssignTextField;
    private JRadioButton nullString;
    private JTextField nullStringText;

    //methods
    /**
     * <h1>Constructor</h1>
     * <p>Initialize the class and set it visible</p>
     */
    public ConfigWindow() {

        //Set up the window
        setContentPane(content);
        setBounds(100,100,500,250);
        setTitle("Configuration");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Logic Code
        setDefaultConfiguration();

        //Listeners
        buttonOK.addActionListener(e -> onOK());

        buttonApply.addActionListener(e -> onApply());

        buttonCancel.addActionListener(e -> onCancel());

        autoAssign.addActionListener(e-> {
            batchAutoAssignTextField.setEnabled(autoAssign.isSelected());
            batchAutoAssignTextField.setText("max");
        });

        nullString.addActionListener(e-> {
            nullStringText.setEnabled(nullString.isSelected());
            nullStringText.setText("?");
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
        if (autoAssign.isSelected()){
            try {
                BatchAutoAssignFormat();
            } catch (BatchFormatException e) {
                JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            }
        }
        Config.setNullString(nullString.isSelected());
        if (Config.isNullString()) {
            Config.setNullString(nullStringText.getText());
        }
        Config.setDeleteCSComments(deleteCSComments.isSelected());
        Config.setAutoAssign(autoAssign.isSelected());
    }

    /**
     * <h1>setDefaultConfiguration()</h1>
     * <p>Get the configuration from Config class</p>
     */
    private void setDefaultConfiguration(){
        deleteCSComments.setSelected(Config.isDeleteCSComments());

        autoAssign.setSelected(Config.isAutoAssign());
        batchAutoAssignTextField.setEnabled(Config.isAutoAssign());
        if (Config.isAutoAssign())batchAutoAssignTextField.setText(AutoAssign.getBatch());

        nullStringText.setEnabled(Config.isNullString());
        nullString.setSelected(Config.isNullString());
        if (Config.isNullString())nullStringText.setText(Config.getNullString());
    }

    /**
     * <h1>BatchAutoAssignFormat()</h1>
     * <p>See if the batch is writen correctly</p>
     *
     * @throws BatchFormatException
     */
    private void BatchAutoAssignFormat() throws BatchFormatException {
        String pattern = "^[1-9]\\d*$";
        if(!Pattern.compile(pattern).matcher(batchAutoAssignTextField.getText()).matches() && !batchAutoAssignTextField.getText().equalsIgnoreCase("max")){
            throw new BatchFormatException();
        } else {
            AutoAssign.setBatch(batchAutoAssignTextField.getText().trim().toLowerCase());
        }
    }
}
