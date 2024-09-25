package exceptions;

import javax.swing.*;

/**
 * <h1>ImportException</h1>
 * <p>throw when import a file from the {@link JFileChooser}</p>
 *
 * @author D4vsus
 */
public class ImportException extends Exception{
    @Override
    public String toString() {
        return "Error: The file cant be imported";
    }
}
