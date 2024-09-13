package logic;

/**
 * <h1>Attribute</h1>
 * <p>Save the attribute name and type</p>
 *
 * @author D4vsus
 */
public class Attribute {

    //variables and objects
    protected final String attributeName;
    protected String attributeType;

    //methods

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class</p>
     *
     * @param attributeName {@link String}
     */
    public Attribute(String attributeName){
        this.attributeName = attributeName;
    }

    public String getAttributeName(){return this.attributeName;}

    public String getAttributeType(){return this.attributeType;}

    public void setAttributeType(String attributeType) {this.attributeType = attributeType;}

    /**
     * <h1>getAttributeARRF(){</h1>
     * <p>Return the attribute to ARFF</p>
     *
     * @return {@link String}
     */
    public String getAttributeARRF(){
        return "@attribute " + this.attributeName + " " +this.attributeType;
    }
}
