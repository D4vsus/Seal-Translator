import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVtoARFF extends JFrame{
    private String[] dataNames;
    private StringBuilder data;
    private String datasetName;
    private JPanel MainWindow;
    private JButton importCSVb;
    private JButton exportARFFb;
    private JPanel Preprocess;
    private JTextField textField1;
    private JScrollPane scroll;
    private JPanel attribute;

    public CSVtoARFF(){
        this.setBounds(100,100,500,250);
        this.add(MainWindow);
        // reset the list
        this.attribute.removeAll();
        // we add constraints to the display
        GridBagConstraints layout = new GridBagConstraints();
        layout.fill = GridBagConstraints.VERTICAL;
        layout.gridy = 0;
        // we initialize the list
        layout.gridy += 1;
        this.attribute.add(new attributeItem("preview").getPanel(),layout);
        this.setVisible(true);
    }

    public void loadCSV(String path){
        try (Scanner scanner = new Scanner(new File(path))){
            if (scanner.hasNextLine()){
                this.datasetName = path.split("\\.")[0];
                String[] dataNames = scanner.nextLine().split("[,;]");

                StringBuilder data = new StringBuilder();
                while (scanner.hasNextLine()){
                    data.append(scanner.nextLine());
                    data.append("\n");
                }
                this.dataNames = dataNames;
                this.data = data;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportARFF(String nameDataset,String... dataTypes) throws IOException {
        File file = new File(datasetName+".arff");
        file.createNewFile();

        String content = "@RELATION '" + nameDataset + "'" + "\n" +
                "\n" +
                writeAttributes(dataTypes) +
                "\n" +
                "@DATA" + "\n" + data;

        writeFile(file, content);
    }

    private String writeAttributes(String... dataTypes){
        StringBuilder attributes = new StringBuilder();
        int size = dataNames.length;
        for (int x = 0;x < size;x++){
            attributes.append("@ATTRIBUTE ").append(dataNames[x]).append(" ").append(dataTypes[x]).append("\n");
        }
        return attributes.toString();
    }

    private void writeFile(File file,String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }
}
