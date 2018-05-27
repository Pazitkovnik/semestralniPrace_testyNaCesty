/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import app.Question;
import app.Test;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;

/**
 * FileHandler class implements basic methods for working with files
 * @author Jachym
 */
public class FileHandler
{
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    
    private FileHandler()
    {
        ;
    }
    
    /**
     * returns content of a file in string
     * @param filePath relative path to a file (e.q "data/test.csv")
     * @return
     * @throws IOException 
     */
    public static String getFileContent(String filePath) throws IOException
    {
        return new String ( Files.readAllBytes(new File(filePath).toPath()) );
    }
    
    /**
     * prints text string into a freshly made file,
     * if a file with same name already exits add _kopie
     * @param text
     * @param fileName
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException if the fileName is without extension
     */
    public static String makeFileAndPrint(String text, String fileName) throws FileNotFoundException, UnsupportedEncodingException
    {
        //divide file name and file extension in order to create another file with the same name
        int actEnd = fileName.lastIndexOf('.');
        if(actEnd == -1)
            throw new IllegalArgumentException("nazev souboru nesmi byt bez pripony!");
        
        String actName = fileName.substring(0, actEnd);
        String extName = fileName.substring(actEnd + 1);
        
        
        File temp = new File(fileName);
        while(temp.exists())
        {
            actName += "_kopie";
            temp = new File(actName + "." + extName);
        }
        
        PrintWriter writer = new PrintWriter(temp.getAbsolutePath(), "UTF-8");
        writer.println(text);
        writer.close();
        return temp.getName();
    }
    
    /**
     * 
     * @param t
     * @param questions
     * @param fileName 
     */
    public static void makeTestAndPrintBin(Test t, ArrayList<Question> questions,
            String fileName)
    {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(fileName + ".csv"));
            out.writeBytes(t.getName());
            out.writeChars(",");
            out.writeBytes(t.getType());
            out.writeChars(",");
            out.writeBytes(String.valueOf(t.getQuestionQuant()));
            out.writeChars(",");
            out.writeBytes(String.valueOf(t.getDifficulty()));
            out.writeChars(",");

            for (Question q : questions) {
                out.writeBytes(q.getQuestion());
                out.writeChars(",");
                out.writeBytes(q.getType());
                out.writeChars(",");
                for(String s : q.getChoices())
                {
                    out.writeBytes(s);
                    out.writeChars(",");
                }               
                out.writeBytes(String.valueOf(q.getCorrectAnswer()));
                out.writeBytes(String.valueOf(q.getPoints()));
                out.writeChars("||");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Writing error");
        }
        finally
        {
            try
            {
                out.flush();
                out.close();
            }
            catch(IOException e)
            {
                System.out.println("Error while flushin/closing fileWriter");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * make a result csv file containing basic info about the test with the comma delimeter
     * format: name,type,points,maxpoints,duration,difficulty,slovnihodnoceni
     * @param t 
     */
    public static void makeResultsAndPrintBin(Test t)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter("results/" + t.getName() + ".csv");
            fileWriter.append(t.getName());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(t.getType());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append( String.valueOf(t.getCurrentPoints()) );
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append( String.valueOf(t.getCurrentMaxPoints()) );
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append( Test.formatTestTime(t.getTestTime()) );
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append( String.valueOf(t.getDifficulty()) );
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append( Test.getCheer(t.getCurrentPoints(), t.getCurrentMaxPoints()) );
        }
        catch(Exception e)
        {
            System.out.println("Error in CsvFileWriter");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileWriter.flush();
                fileWriter.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * prints at the end of specified file
     * @param text
     * @param fileName
     * @throws IOException 
     */
    public static void printIntoFile(String text, String fileName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(fileName, true));
        writer.append("\n" + text);
        writer.close();
    }
    
    /**
     * makes pdf file with the fileName + ".pdf" name in the resulsts folder
     * @param text
     * @param fileName
     * @throws FileNotFoundException
     * @throws DocumentException 
     */
    public static void makePDFAndPrint(String text, String fileName) throws FileNotFoundException, DocumentException
    {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, "Cp1250", 16);
        Font redFont = FontFactory.getFont(FontFactory.COURIER, "Cp1250", 16, 1, BaseColor.RED);
        Font greenFont = FontFactory.getFont(FontFactory.COURIER, "Cp1250", 16, 1, BaseColor.GREEN);
        
        String parts[] = text.split("\n");
        Paragraph para = new Paragraph();
        for(String p : parts)
        {
            if(p.trim().matches("SPRAVNE"))
            {
                System.out.println("hura");
                para.add(new Chunk(p, greenFont));
            }
            else if(p.trim().matches("SPATNE"))
                para.add(new Chunk(p, redFont));
            else
                para.add(new Chunk(p, font));
            para.add(Chunk.NEWLINE);
        }
        document.add(para);
        document.close();
    }
}
