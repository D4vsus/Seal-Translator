package UI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class CSVtoARFF extends JFrame{

    //variables and objects

    //logic components
    private String fileName;
    private String[] dataNames;
    private StringBuilder data;

    //UI components
    private JPanel MainWindow;
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
        this.add(MainWindow);

        // we add the layout
        this.layout = new GridBagConstraints();
        this.layout.fill = GridBagConstraints.VERTICAL;
        this.layout.gridy = 0;

        //debugging
        addAttribute(new AttributeItem("place holder"));

        //add listeners
        this.importCSVb.addActionListener(e->{
            File file = new File ("c:\\");
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.exportARFFb.addActionListener(e->{
            //exportARFF(this.datasetName.getText(),);
        });

        //show the window
        this.setVisible(true);
    }

    public void addAttribute(AttributeItem attributeItem){
        //moves down the layout
        this.layout.gridy += 1;
        this.attribute.add(attributeItem.getPanel(),layout);
    }

    public void loadCSV(String path){
        try (Scanner scanner = new Scanner(new File(path))){

            // reset the attribute panel
            layout.gridy = 0;
            this.attribute.removeAll();

            //read the CSV
            if (scanner.hasNextLine()){

                //get the name of the file
                this.fileName = path.split("\\.")[0];

                //get the attributes name and split it
                this.dataNames = scanner.nextLine().split("[,;]");

                //get all the data from the file
                StringBuilder data = new StringBuilder();
                while (scanner.hasNextLine()){
                    data.append(scanner.nextLine());
                    data.append("\n");
                }

                //save data
                this.data = data;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportARFF(String nameDataset,String... dataTypes) throws IOException {
        
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

    private String writeAttributes(String... dataTypes){
        StringBuilder attributes = new StringBuilder();
        int size = dataNames.length;
        for (int x = 0;x < size;x++){
            attributes.append("@attribute ").append(dataNames[x]).append(" ").append(dataTypes[x]).append("\n");
        }
        return attributes.toString();
    }

    private void writeFile(File file,String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }
}
