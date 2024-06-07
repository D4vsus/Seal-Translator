import javax.swing.*;

public class attributeItem {
    private JPanel attributePanel;
    private JComboBox attributeTypeb;
    private JLabel attrbutel;
    private JTextField dataInfot;
    private final String attributeName;

    public attributeItem(String attributeName){
        //this.dataInfot.setVisible(false);
        this.dataInfot.setEnabled(false);
        this.attributeName = attributeName;
        this.attrbutel.setText("<html> <b>"+attributeName+"</b> </html>");
        this.attributeTypeb.addItem("Select attribute...");
        this.attributeTypeb.addItem("string");
        this.attributeTypeb.addItem("number");
        this.attributeTypeb.addItem("date");
        this.attributeTypeb.addItem("nominal");
        this.attributeTypeb.addItemListener(e ->{
            if (attributeTypeb.getSelectedIndex() == 4){
                dataInfot.setEnabled(true);
            } else {
                dataInfot.setEnabled(false);
            }
        });
    }

    public JPanel getPanel(){
        return attributePanel;
    }

    public String getAttributeName(){
        return this.attributeName;
    }
}
