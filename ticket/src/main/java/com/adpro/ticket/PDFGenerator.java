package com.adpro.ticket;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.Set;

import com.adpro.ticket.model.Booking;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import be.quodlibet.boxable.*;
import be.quodlibet.boxable.line.LineStyle;

import com.adpro.ticket.model.Ticket;
import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.movies.MovieSession;
import com.adpro.ticket.api.movies.Theater;

public class PDFGenerator {

    private static final PDFont fontPlain = PDType1Font.HELVETICA;
    private static final PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private BookingData bookingData;

    public static byte[] generateTicket(BookingData bookingData) throws IOException {
        PDDocument doc = new PDDocument();
        Set<Ticket> tickets = bookingData.getTickets();

        int amount = tickets.size();

        PDDocumentInformation pdd = doc.getDocumentInformation();

        pdd.setAuthor("C8 Advance Programming Team");
        pdd.setTitle(bookingData.getMovieSession() + " Ticket");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (Ticket x : tickets) {
            PDPage page = new PDPage();
            doc.addPage(page);

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream cos = new PDPageContentStream(doc, page);

            PDImageXObject img = PDImageXObject.createFromFile("src/main/resources/assets/logo.jpeg", doc);
            cos.drawImage(img, 50, 50);
            cos.newLine();
            cos.beginText();
            cos.newLineAtOffset(25, 700);
            String line1 = "Booking.in";
            cos.setFont(fontBold, 30);
            cos.showText(line1);
            cos.newLine();
            // creating table properties
            float margin = 50;
            // starting y position is whole page height subtracted by top and bottom margin
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
            // we want table across whole page width (subtracted by left and right margin of course)
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

            boolean drawContent = true;
            float bottomMargin = 70;
            // y position is your coordinate of top left corner of the table
            float yPosition = 550;

            BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);

            // the parameter is the row height
            Row<PDPage> headerRow = table.createRow(50);
            // the first parameter is the cell width
            Cell<PDPage> cell = headerRow.createCell(100, "FASILKOM THEATRE");
            cell.setFont(fontBold);
            cell.setFontSize(20);
            // vertical alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            // border style
            cell.setBorderStyle(new LineStyle(Color.WHITE, 0));
            table.addHeaderRow(headerRow);

            Row<PDPage> row = table.createRow(12);
            createCellPlain(cell, row, fontPlain, "Movie:");
            createCellBold(cell, row, fontBold, bookingData.getMovieSession().getMovie().getName());

            Row<PDPage> row2 = table.createRow(12);
            createCellPlain(cell, row2, fontPlain, "Showtime:");
            createCellBold(cell, row2, fontBold, bookingData.getMovieSession().getStartTime().toString());

            Row<PDPage> row3 = table.createRow(12);
            createCellPlain(cell, row3, fontPlain, "Seat/Row:");
            createCellBold(cell, row3, fontBold, x.getSeatId());

            Row<PDPage> row4 = table.createRow(12);
            createCellPlain(cell, row4, fontPlain, "Auditorium:");
            createCellBold(cell, row4, fontBold, bookingData.getMovieSession().getTheatre().getId().toString());
            cell.setFontSize(50);

            table.draw();

            cos.endText();
            cos.close();
        }

        doc.save(baos);
        doc.close();

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        byte[] bytes = IOUtils.toByteArray(inputStream);
        return bytes;
    }

    public static void createCellBold(Cell<PDPage> cell, Row<PDPage> row, PDFont font, String val) {
        cell = row.createCell(50, val);
        cell.setAlign(HorizontalAlignment.CENTER);
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setFontSize(15);
        cell.setFont(fontBold);
        cell.setBorderStyle(new LineStyle(Color.WHITE, 0));
    }

    public static void createCellPlain(Cell<PDPage> cell, Row<PDPage> row, PDFont font, String val) {
        cell = row.createCell(50, val);
        cell.setFont(fontPlain);
        cell.setFontSize(15);
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setBorderStyle(new LineStyle(Color.WHITE, 0));
    }

}