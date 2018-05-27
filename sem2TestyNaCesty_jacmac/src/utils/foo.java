/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author Jachym
 */
public class foo
{
    public void makePdf() throws FileNotFoundException, DocumentException
    {
        String fileName = "test.pdf";
        String text = "text\ntext2\ntexterino";
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, "Cp1250",
                16, 1, BaseColor.RED);
        
        String parts[] = text.split("\n");
        Paragraph para = new Paragraph();
        
        for(String p : parts)
        {
            para.add(new Chunk(p, font));
            para.add(Chunk.NEWLINE);
        }
        document.add(para);
        document.close();
    }
}
