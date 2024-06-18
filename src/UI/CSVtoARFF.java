package UI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CSVtoARFF extends JFrame{

    //variables and objects

    //logic components
    private String fileName;
    private final ArrayList<AttributeItem> dataAttributes;
    private StringBuilder data;

    //UI components
    private JPanel mainWindow;
    private JButton importCSVb;
    private JButton exportARFFb;
    private JPanel Preprocess;
    private JTextField datasetName;
    private JScrollPane scroll;
    private JPanel attribute;
    private final GridBagConstraints layout;

    //methods
    public CSVtoARFF(){

        //we set the properties of the window
        this.setBounds(100,100,500,250);
        this.setTitle("CSV to ARFF");
        this.add(mainWindow);
        this.dataAttributes = new ArrayList<>();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // we add the layout
        this.layout = new GridBagConstraints();
        this.layout.fill = GridBagConstraints.VERTICAL;
        this.layout.gridy = 0;

        //add mnemonics
        this.importCSVb.setMnemonic('i');
        this.exportARFFb.setMnemonic('e');

        //add listeners
        this.importCSVb.addActionListener(e->{
            try {
                new FileSelector(this);
                if (fileName != null) loadCSV(fileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.exportARFFb.addActionListener(e->{
            try {
                exportARFF(this.datasetName.getText(),dataAttributes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //show the window
        this.setVisible(true);
    }

    public void setCSV(String fileName){
        this.fileName = fileName;
    }

    public void addAttribute(AttributeItem attributeItem){
        //moves down the layout
        this.layout.gridy += 1;
        this.attribute.add(attributeItem.getPanel(),layout);
        this.dataAttributes.add(attributeItem);
    }

    public void loadCSV(String path){
        try (Scanner scanner = new Scanner(new File(path))){

            //reset the attribute panel
            layout.gridy = 0;
            this.attribute.removeAll();

            //read the CSV
            if (scanner.hasNextLine()){

                //get the name of the file
                this.fileName = path.split("\\.")[0];

                //get the attributes name and split it
                String[] dataNames = scanner.nextLine().split("[,;]");
                for (String attribute:dataNames){
                    addAttribute(new AttributeItem(attribute.replace(" ","-")));
                }

                //get all the data from the file
                StringBuilder data = new StringBuilder();
                while (scanner.hasNextLine()){
                    data.append(scanner.nextLine().replace(";",","));
                    data.append("\n");
                }

                //save data
                this.data = data;
                this.datasetName.setText(this.fileName);
                this.scroll.revalidate();
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("res\\Strings").getString("The.file.didn't.exist.or.didn't.found"),"File not found",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),e.toString(),JOptionPane.ERROR_MESSAGE,null);
        }
    }

    public void exportARFF(String nameDataset,ArrayList<AttributeItem> dataTypes) throws IOException {
        
        File file = new File(fileName+".arff");
        if (!file.createNewFile()) {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }

        String content = "@relation '" + nameDataset + "'" + "\n" +
                "\n" +
                writeAttributes(dataTypes) +
                "\n" +
                "@data" + "\n" + data;

        writeFile(file, content);
    }

    private String writeAttributes(ArrayList<AttributeItem> dataTypes){
        StringBuilder attributes = new StringBuilder();
        for (AttributeItem attribute:dataTypes){
            attributes.append(attribute.getAttribute()).append("\n");
        }
        return attributes.toString();
    }

    private void writeFile(File file,String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }
}
