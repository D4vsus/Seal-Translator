package UI;

import javax.swing.*;

public class AttributeItem {
    private final String attributeName;
    private String attributeType;
    private JPanel attributePanel;
    private JComboBox attributeTypeb;
    private JLabel attrbutel;
    private JTextField dataInfot;

    public AttributeItem(String attributeName){
        this.attributeName = attributeName;
        this.attrbutel.setText("<html> <b>"+attributeName+"</b> </html>");

        //add box objects
        this.attributeTypeb.addItem("Select attribute...");
        this.attributeTypeb.addItem("string");
        this.attributeTypeb.addItem("numeric");
        this.attributeTypeb.addItem("date");
        this.attributeTypeb.addItem("nominal");

        this.attributeType = (String) attributeTypeb.getSelectedItem();

        this.attributeTypeb.addItemListener(e ->{
            //get the item selected
            this.attributeType = (String) attributeTypeb.getSelectedItem();

            //if in the box is selected type date or nominal enable the type
            dataInfot.setEnabled(attributeTypeb.getSelectedIndex() == 4|| attributeTypeb.getSelectedIndex() == 3);
        });
    }

    public String getAttribute(){
        return "@attribute " + attributeName + " " + attributeType;
    }

    public JPanel getPanel(){
        return attributePanel;
    }

    public String getAttributeName(){return this.attributeName;}

    public String getAttributeType(){return this.attributeType;}
}
