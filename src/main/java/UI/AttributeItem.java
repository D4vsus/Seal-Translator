package UI;

import exceptions.ArffAttributeNotRecognised;
import logic.Attribute;

import javax.swing.*;
/**
 * <h1>AttributeItem</h1>
 * <p>Manage attributes UI</p>
 *
 * @author D4vsus
 */
public class AttributeItem extends Attribute {

    //variables and objects
    private JPanel attributePanel;
    private JComboBox<String> attributeTypeButton;
    private JLabel attributeLabel;
    private JTextField dataInfoText;
    private JCheckBox drop;

    //methods
    /**
     * <h1>AttributeItem()</h1>
     * <p>Instantiate the attribute</p>
     *
     * @param attributeName : {@link String}
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

        setAttributeType((String)attributeTypeButton.getSelectedItem());
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
     * <h1>AttributeItem()</h1>
     * <p>Instantiate the attribute</p>
     *
     * @param attribute : {@link Attribute}
     * @author D4vsus
     */
    public AttributeItem(Attribute attribute){
        super(attribute.getAttributeName());
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
     * <h1>getAttributeTypeARFF()</h1>
     * <p>Return the attribute in type ARFF</p>
     *
     * @return {@link String}
     */
    public String getAttributeTypeARFF(){
        StringBuilder string = new StringBuilder();
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

    /**
     * <h1>setAttributeTypeARFF()</h1>
     * <p>Set the attribute in type ARFF</p>
     *
     * @param format {@link String...}(first the type, second for date or nominal)
     */
    public void setAttributeTypeARFF(String... format) throws ArffAttributeNotRecognised {
        switch (format[0]){
            case "string":
                this.attributeTypeButton.setSelectedIndex(1);
                break;

            case "numeric":
                this.attributeTypeButton.setSelectedIndex(2);
                break;

            case "date":
                this.attributeTypeButton.setSelectedIndex(3);
                this.dataInfoText.setText(format[1]);
                break;

            case "nominal":
                this.attributeTypeButton.setSelectedIndex(4);
                this.dataInfoText.setText(format[1]);
                break;
            default:
                throw new ArffAttributeNotRecognised();
        }
    }

    /**
     * <h1>toARFF()</h1>
     * <p>Pass the attribute to ARFF</p>
     *
     * @return {@link String}
     */
    public String toARFF(){
      return "@attribute " + this.getAttributeName() +" "+  getAttributeTypeARFF();
    }

    /**
     * <h1>getPanel()</h1>
     * <p>Return the panel UI</p>
     *
     * @return {@link JPanel}
     */
    public JPanel getPanel(){
        return this.attributePanel;
    }

    /**
     * <h1>isSelectedNominalDate()</h1>
     * <p>Return if selected or nominal is selected</p>
     *
     * @return boolean
     */
    private boolean isSelectedNominalDate(){return this.attributeTypeButton.getSelectedIndex() == 4|| this.attributeTypeButton.getSelectedIndex() == 3;}

    public boolean isDropColumn(){
        return drop.isSelected();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
