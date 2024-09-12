package UI;

import logic.Attribute;

import javax.swing.*;

/**
 * <h1>AttributeItem</h1>
 * <p>This Class let you manage attributes, what type they are and export them</p>
 * @author D4vsus
 */
public class AttributeItem extends Attribute {

    //variables and objects
    private JPanel attributePanel;
    private JComboBox attributeTypeButton;
    private JLabel attributeLabel;
    private JTextField dataInfoText;

    //methods
    /**
     * <h1>AttributeItem()</h1>
     * <p>Instantiate the attribute</p>
     * @param attributeName : {@link String}
     * @author D4vsus
     */
    public AttributeItem(String attributeName){
        super(attributeName);
        this.attributeLabel.setText("<html> <b>"+attributeName+"</b> </html>");

        //add box objects
        this.attributeTypeButton.addItem("Select attribute...");
        this.attributeTypeButton.addItem("string");
        this.attributeTypeButton.addItem("numeric");
        this.attributeTypeButton.addItem("date");
        this.attributeTypeButton.addItem("nominal");

        setAttributeType((String)this.attributeTypeButton.getSelectedItem());
        this.attributeLabel.setToolTipText(attributeName);

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
    @Override
    public String getAttribute(){
        StringBuilder string = new StringBuilder("@attribute " + this.attributeName + " ");
        if (this.attributeTypeButton.getSelectedIndex() == 4){
            string.append("{").append(this.dataInfoText.getText()).append("}");
        } else {
            string.append(this.attributeType);
            if (this.attributeTypeButton.getSelectedIndex() == 3){
                string.append(" ").append("\"").append(this.dataInfoText.getText()).append("\"");
            }
        }
        return string.toString();
    }

    public JPanel getPanel(){
        return this.attributePanel;
    }

    private boolean isSelectedNominalDate(){return this.attributeTypeButton.getSelectedIndex() == 4|| this.attributeTypeButton.getSelectedIndex() == 3;}
}
