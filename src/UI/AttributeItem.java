package UI;

import javax.swing.*;

/**
 * <h1>AttributeItem</h1>
 * <p>This Class let you manage attributes, what type they are and export them</p>
 * @author D4vsus
 */
public class AttributeItem {

    //variables and objects
    private final String attributeName;
    private String attributeType;
    private JPanel attributePanel;
    private JComboBox attributeTypeButton;
    private JLabel attrbuteLabel;
    private JTextField dataInfoText;

    //methods
    /**
     * <h1>AttributeItem()</h1>
     * <p>Instantiate the attribute</p>
     * @param attributeName : {@link String}
     * @author D4vsus
     */
    public AttributeItem(String attributeName){
        this.attributeName = attributeName;
        this.attrbuteLabel.setText("<html> <b>"+attributeName+"</b> </html>");

        //add box objects
        this.attributeTypeButton.addItem("Select attribute...");
        this.attributeTypeButton.addItem("string");
        this.attributeTypeButton.addItem("numeric");
        this.attributeTypeButton.addItem("date");
        this.attributeTypeButton.addItem("nominal");

        this.attributeType = (String) this.attributeTypeButton.getSelectedItem();
        this.attrbuteLabel.setToolTipText(attributeName);

        this.attributeTypeButton.addItemListener(e ->{
            //get the item selected
            this.attributeType = (String) this.attributeTypeButton.getSelectedItem();

            //if in the box is selected type date or nominal enable the type
            this.dataInfoText.setEnabled(isSelectedNominalDate());

            //else sweep the text
            if (!isSelectedNominalDate()) this.dataInfoText.setText("");
        });
    }

    /**
     * <h1>getAttribute()</h1>
     * <p>Return the ARFF version of the attribute</p>
     * @return {@link String}
     * @author D4vsus
     */
    public String getAttribute(){
        StringBuilder string = new StringBuilder("@attribute " + this.attributeName + " " + this.attributeType);
        if (isSelectedNominalDate()){
            if (this.attributeTypeButton.getSelectedIndex() == 4){
                string.append("{").append(this.dataInfoText.getText()).append("}");
            } else {
                string.append("\"").append(this.dataInfoText.getText()).append("\"");
            }
        }
        return string.toString();
    }

    public JPanel getPanel(){
        return this.attributePanel;
    }

    public String getAttributeName(){return this.attributeName;}

    public String getAttributeType(){return this.attributeType;}

    private boolean isSelectedNominalDate(){return this.attributeTypeButton.getSelectedIndex() == 4|| this.attributeTypeButton.getSelectedIndex() == 3;}
}
