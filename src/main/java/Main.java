import UI.CSVtoARFF;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //set the Look and Feel
        FlatIntelliJLaf.setup();
        //Open the main window
        try {
            new CSVtoARFF();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }
}