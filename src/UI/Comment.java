package UI;

import javax.swing.*;
import java.awt.event.*;

/**
 * <h1>Comment</h1>
 * <p>A dialog to write a comment on the ARFF file</p>
 * @author D4vsus
 */
public class Comment extends JDialog {

    //variables and objects
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea commentArea;
    private JScrollPane scroll;
    private JPanel okCancelPanel;
    private JPanel decisionPanel;
    private JPanel commentPanel;

    //methods
    /**
     * <h1>Constructor()</h1>
     * <p>Create the window</p>
     *
     * @param savedComment : {@link StringBuilder}
     */
    public Comment(StringBuilder savedComment) {
        setContentPane(contentPane);
        setModal(true);
        setBounds(100,100,400,200);
        getRootPane().setDefaultButton(buttonOK);
        commentArea.setText(savedComment.toString());

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setVisible(true);
    }

    /**
     * <h1>onOk()</h1>
     * <p>Let the window dispose</p>
     */
    private void onOK() {
        JOptionPane.showMessageDialog(this, "Comment added", "Comment", JOptionPane.INFORMATION_MESSAGE, null);
        dispose();
    }

    /**
     * <h1>onCancel()</h1>
     * <p>Let the window dispose deleting the comment</p>
     */
    private void onCancel() {
        commentArea.setText("");
        dispose();
    }

    /**
     * <h1>getComment()</h1>
     * <p>return the text comment</p>
     *
     * @return {@link String}
     */
    public String getComment(){
        return commentArea.getText();
    }
}
