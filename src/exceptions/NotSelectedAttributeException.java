package exceptions;

public class NotSelectedAttributeException extends Exception{
    @Override
    public String toString() {
        return "Error: you haven't select all the attributes types of the field";
    }
}
