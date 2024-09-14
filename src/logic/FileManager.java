package logic;

import exceptions.NoDatasetNameException;
import exceptions.NotSelectedAttributeException;

import java.io.*;
import java.util.Scanner;

/**
 * <h1>FileManager</h1>
 * <p>Manage the IO from the files</p>
 *
 * @author D4vsus
 */
public class FileManager {

    //methods
    /**
     * <h1>writeFile()</h1>
     * <p>Write the content into the file</p>
     *
     * @param file : {@link File}
     * @param content : {@link String}
     */
    private static void writeFile(File file, String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }

    /**
     * <h1>exportARFF()</h1>
     * <p>Create the arff file and write it's content</p>
     *
     * @param dataTable : {@link DataTable}
     * @param fileName : {@link String}
     * @author D4vsus
     */
    public static void exportARFF(DataTable dataTable,String fileName) throws Exception{
        //Preprocess
        //see if they added the name to the dataset
        if (dataTable.getRelation().isBlank()) throw new NoDatasetNameException();

        //see they select an attribute
        for (Attribute attribute : dataTable.getAttributes()){
            if (attribute.getAttributeType().equals("Select attribute...")){
                throw new NotSelectedAttributeException();
            }
        }

        //Process
        File file = new File(fileName + ".arff");
        if (!file.createNewFile()) {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }

            //this.data = new StringBuilder(this.data.toString().replace("'","\\'")
            //                                                  .replace("\"","\\\""));

        writeFile(file, dataTable.toARFF());
    }

    /**
     * <h1>loadCSV()</h1>
     * <p>Load the data to the program</p>
     *
     * @param dataTable : {@link DataTable}
     * @param path      : {@link String}
     * @author D4vsus
     */
    public static void loadCSV(DataTable dataTable, String path) throws Exception{
        Scanner scanner = new Scanner(new File(path));

        //Preprocess
        //reset the attribute panel
        dataTable.clearAll();

        //Process

        //get the name of the file without the extension CSV
        String fileName = path.split("\\.")[0];

        String[] dataNames;

        //read the CSV
        if (scanner.hasNextLine()) {

            //delete comments
            if (Config.isDeleteCSComments()){
                String possibleComment = "";
                while (scanner.hasNextLine()){
                    possibleComment = scanner.nextLine();
                    if (possibleComment.charAt(0) != '#') break;
                }
                dataNames = possibleComment.split("[,;]");
            } else {
                //get the attributes name and split it
                dataNames = scanner.nextLine().split("[,;]");
            }

            for (String attribute : dataNames) {
                dataTable.addAttribute(new Attribute(attribute.replace(" ", "-")));
            }

            //get all the data from the file
            StringBuilder data = new StringBuilder();
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine().replace(";", ","));
                data.append("\n");
            }
            //save data
            for (String row : data.toString().split("\n")) {
                dataTable.addRow(row.split(","));
            }
        } else {
            throw new FileNotFoundException();
        }
    }
}
