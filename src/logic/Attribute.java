package logic;

public class Attribute {

    //variables and objects
    protected final String attributeName;
    protected String attributeType;

    //methods
    public Attribute(String attributeName){
        this.attributeName = attributeName;
    }

    public String getAttributeName(){return this.attributeName;}

    public String getAttributeType(){return this.attributeType;}
}
