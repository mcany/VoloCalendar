package volo.voloCalendar.service;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volo.voloCalendar.model.User;

/**
 * Created by Emin Guliyev on 19/01/2015.
 */
@Service
public class ReportLogic {
    @Autowired
    public CalendarLogic calendarLogic;
    @Autowired
    public UserManagementLocalLogic userManagementLogic;

    public  String monthlyReport(int year, int month){
        //file path
        String path = "D:/temp/" + UUID.randomUUID().toString() + ".pdf";
        Document doc = new Document();
        PdfWriter docWriter = null;

        try {
            //special font sizes
            Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);

            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("volo");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("emanuel");
            doc.addTitle("Monthly report");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();

            //create a paragraph
            Paragraph paragraph = new Paragraph("For volo admins.");

            //specify column widths
            float[] columnWidths = {1f,1f,1f,1f,1f,1f,1f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "Fullname", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Contract", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Hours Done", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Hours Required", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Diff", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "IBAN", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Wage", Element.ALIGN_RIGHT, 1, bfBold12);
            table.setHeaderRows(1);

            LocalDate beginDateOfMonth = LocalDate.of(year, month, 1);
            ArrayList<User> users = calendarLogic.getActiveDriversForMonth(beginDateOfMonth);
            //just some random data to fill
            for(User user: users){
                insertCell(table, user.getName(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getContractType().name(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getDoneHours() + "", Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getPlannedHours() + "", Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getDiffHours() + "", Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getIban(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getDoneHours()*12 + "", Element.ALIGN_RIGHT, 1, bf12);
            }

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null){
                //close the document
                doc.close();
            }
            if (docWriter != null){
                //close the writer
                docWriter.close();
            }
        }

        return path;
    }

    public  String overviewReport(){
        //file path
        String path = "D:/temp/" + UUID.randomUUID().toString() + ".pdf";
        Document doc = new Document();
        PdfWriter docWriter = null;

        try {
            //special font sizes
            Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);

            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("volo");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("emanuel");
            doc.addTitle("Overview report");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();

            //create a paragraph
            Paragraph paragraph = new Paragraph("For volo admins.");

            //specify column widths
            float[] columnWidths = {1f,1f,1f,1f,1f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "Fullname", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Telephone", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "E-mail", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Contract", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "IBAN", Element.ALIGN_RIGHT, 1, bfBold12);
            table.setHeaderRows(1);

            User[] users = userManagementLogic.getActiveDrivers();
            //just some random data to fill
            for(User user: users){
                insertCell(table, user.getName(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getTelephoneNumber(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getEmail(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getContractType().name(), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table, user.getIban(), Element.ALIGN_RIGHT, 1, bf12);
            }

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null){
                //close the document
                doc.close();
            }
            if (docWriter != null){
                //close the writer
                docWriter.close();
            }
        }

        return path;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
}
