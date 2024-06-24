package UI;

import exceptions.DuplicatedNameException;
import exceptions.NotSelectedAttributeException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <h1>CSVtoARFF</h1>
 * <p>The main window and where the main logic is in it</p>
 * @author D4vsus
 */
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
    private JPanel preprocess;
    private JTextField datasetName;
    private JScrollPane scroll;
    private JPanel attribute;
    private final GridBagConstraints layout;

    //methods

    /**
     * <h1>CSVtoARFF()</h1>
     * <p>Instantiate the window</p>
     * @author D4vsus
     */
    public CSVtoARFF(){

        //we set the properties of the window
        this.setBounds(100,100,500,250);
        this.setTitle("Seal Translator");
        this.add(mainWindow);
        this.dataAttributes = new ArrayList<>();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("res/sealIcon.png").getImage());

        // we add the layout
        this.layout = new GridBagConstraints();
        this.layout.fill = GridBagConstraints.VERTICAL;
        this.layout.gridy = 0;

        //we add the menu bar
        JMenuBar menu = new JMenuBar();
        JMenuItem credits = new JMenuItem("Credits",'c');
        credits.addActionListener(e->new Credits());
        credits.setToolTipText("Credits");
        menu.add(credits);
        this.setJMenuBar(menu);

        //add mnemonics
        this.importCSVb.setMnemonic('i');
        this.exportARFFb.setMnemonic('e');

        //add listeners
        this.importCSVb.addActionListener(e->{
            try {
                new FileSelector(this);
                if (this.fileName != null) loadCSV(this.fileName);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,ex.toString(),"IOException",JOptionPane.ERROR_MESSAGE,null);
            }
        });

        this.exportARFFb.addActionListener(e->{
            try {
                exportARFF(this.datasetName.getText(),this.dataAttributes);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,ex.toString(),"IOException",JOptionPane.ERROR_MESSAGE,null);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
            }
        });

        //show the window
        this.setVisible(true);
    }

    /**
     * <h1>setCSV()</h1>
     * <p>Set the file name</p>
     * @param fileName : {@link String}
     * @author D4vsus
     */
    public void setCSV(String fileName){
        this.fileName = fileName;
    }

    /**
     * <h1>addAttribute()</h1>
     * <p>Add an attribute to the dataAttributes record and the main window</p>
     * @param attributeItem : {@link AttributeItem}
     * @author D4vsus
     */
    public void addAttribute(AttributeItem attributeItem) throws DuplicatedNameException {
        //moves down the layout
        this.layout.gridy += 1;
        for (AttributeItem comparer:this.dataAttributes) {
            if (comparer.getAttributeName().equals(attributeItem.getAttributeName())) {
                throw new DuplicatedNameException(attributeItem.getAttributeName());
            }
        }
        this.attribute.add(attributeItem.getPanel(),this.layout);
        this.dataAttributes.add(attributeItem);
    }

    /**
     * <h1>loadCSV()</h1>
     * <p>Load the data to the program</p>
     * @param path : {@link String}
     * @author D4vsus
     */
    public void loadCSV(String path){
        try (Scanner scanner = new Scanner(new File(path))){

            //Preprocess
            //reset the attribute panel
            this.layout.gridy = 0;
            this.dataAttributes.clear();
            this.attribute.removeAll();
            this.scroll.revalidate();
            this.preprocess.revalidate();

            //Process

            //get the name of the file
            this.fileName = path.split("\\.")[0];

            //read the CSV
            if (scanner.hasNextLine()){

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
                this.scroll.revalidate();
            }

        } catch (DuplicatedNameException e) {
            JOptionPane.showMessageDialog(this,e.toString(),"Duplicated Name",JOptionPane.ERROR_MESSAGE,null);
        }  catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,"The file didn't exist or didn't found","File not found",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>exportARFF()</h1>
     * <p>Create the arff file and write it's content</p>
     * @param nameDataset : {@link String}
     * @param dataTypes : {@link ArrayList}<{@link AttributeItem}>
     * @author D4vsus
     */
    public void exportARFF(String nameDataset,ArrayList<AttributeItem> dataTypes) throws IOException {
        try {
            //Preprocess
            //see they select an attribute
            for (AttributeItem attributeItem : this.dataAttributes){
                if (attributeItem.getAttributeType().equals("Select attribute...")){
                    throw new NotSelectedAttributeException();
                }
            }

            //Process
            File file = new File(this.fileName + ".arff");
            if (!file.createNewFile()) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }

            String content = "@relation '" + nameDataset + "'" + "\n" +
                    "\n" +
                    writeAttributes(dataTypes) +
                    "\n" +
                    "@data" + "\n" + this.data;

            writeFile(file, content);
            JOptionPane.showMessageDialog(this, "The field has been created", "Created", JOptionPane.INFORMATION_MESSAGE, null);
        } catch (NotSelectedAttributeException e) {
            JOptionPane.showMessageDialog(this,e.toString(),"Not Selected Attribute",JOptionPane.ERROR_MESSAGE,null);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this,e.toString(),"Error",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    /**
     * <h1>writeAttributes()</h1>
     * <p>Write all the attributes into the arff file</p>
     * @param dataTypes : {@link ArrayList}<{@link AttributeItem}>
     * @return {@link String}
     * @author D4vsus
     */
    private String writeAttributes(ArrayList<AttributeItem> dataTypes){
        StringBuilder attributes = new StringBuilder();
        for (AttributeItem attribute:dataTypes){
            attributes.append(attribute.getAttribute()).append("\n");
        }
        return attributes.toString();
    }

    /**
     * <h1>writeFile()</h1>
     * <p>Write the content into the file</p>
     * @param file : {@link File}
     * @param content : {@link String}
     * @author D4vsus
     */
    private void writeFile(File file,String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }
}
