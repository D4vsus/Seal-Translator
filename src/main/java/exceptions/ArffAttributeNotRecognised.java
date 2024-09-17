package exceptions;

public class ArffAttributeNotRecognised extends Exception{
    @Override
    public String toString() {
        return "Error: Attribute format not recognised";
    }
}
