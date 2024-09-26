package exceptions;

/**
 * <h1>ArffAttributeNotRecognised</h1>
 * <p>throw when the attribute is not include in the arff attribute supported</p>
 *
 * @author D4vsus
 */
public class ArffAttributeNotRecognisedException extends Exception{
    @Override
    public String toString() {
        return "Error: Attribute format not recognised";
    }
}
