package exceptions;

public class DuplicatedNameException extends Exception{
    String duplicatedName;
    public DuplicatedNameException(String duplicatedName){
        this.duplicatedName = duplicatedName;
    }
    @Override
    public String toString() {
        return "Error: the name of the attribute '"+this.duplicatedName+"' is duplicated";
    }
}
