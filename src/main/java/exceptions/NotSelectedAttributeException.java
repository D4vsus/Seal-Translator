package exceptions;

/**
 * <h1>NotSelectedAttributeException</h1>
 * <p>throw when you don't select a type of an attribute that is not going to be deleted</p>
 *
 * @author D4vsus
 */
public class NotSelectedAttributeException extends Exception{
    @Override
    public String toString() {
        return "Error: you haven't select all the attributes types of the field";
    }
}
