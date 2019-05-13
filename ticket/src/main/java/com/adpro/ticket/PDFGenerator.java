package com.adpro.ticket;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import be.quodlibet.boxable.*;
import be.quodlibet.boxable.line.LineStyle;
import be.quodlibet.boxable.text.WrappingFunction;

import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;

public class PDFGenerator {
    public static void main(String[] args) throws IOException {
        try {
            PDDocument doc = new PDDocument();
            PDFont fontPlain = PDType1Font.HELVETICA;
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;
            PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
            PDFont fontMono = PDType1Font.COURIER;

            PDPage page = new PDPage(PDRectangle.A4);
            //Dummy Table
            float margin = 50;
            // starting y position is whole page height subtracted by top and bottom margin
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
            // we want table across whole page width (subtracted by left and right margin of course)
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

            boolean drawContent = true;
            float yStart = yStartNewPage;
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
            cell.setTopBorderStyle(new LineStyle(Color.WHITE, 10));
            table.addHeaderRow(headerRow);

            Row<PDPage> row = table.createRow(12);
            cell = row.createCell(50, "Movie:");
            cell = row.createCell(50, booking.getMovie());

            Row<PDPage> row1 = table.createRow(12);
            cell = row1.createCell(50, "Date:");
            cell = row1.createCell(50, booking.getDate());

            Row<PDPage> row2 = table.createRow(12);
            cell = row2.createCell(50, "Showtime:");
            cell = row2.createCell(50, booking.getShowtime());

            Row<PDPage> row3 = table.createRow(12);
            cell = row3.createCell(50,"Seat/Row:");
            cell = row3.createCell(50, booking.getSeat());

            Row<PDPage> row4 = table.createRow(12);
            cell = row4.createCell(50, "Auditorium:");
            cell = row4.createCell(50, booking.getAudi());
            doc.save("ticket.pdf");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    public void generateMovieCell(){
//        PdfPCell cellMovie = new PdfPCell(new Paragraph("Movie:", fontSubHead));
//        cellMovie.setBorderColor(BaseColor.WHITE);
//        cellMovie.setPaddingRight(10);
//        cellMovie.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cellMovie.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//        PdfPCell cellMovieTitle = new PdfPCell(new Paragraph(ticket.getMovie(), fontSubHead));
//        cellMovieTitle.setBorderColor(BaseColor.WHITE);
//        cellMovieTitle.setPaddingRight(10);
//        cellMovieTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cellMovieTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    }
//
//    public void generateShowtimeCell() {
//        PdfPCell cellShowtime = new PdfPCell(new Paragraph("Showtime:", fontSubHead));
//        cellShowtime.setBorderColor(BaseColor.WHITE);
//        cellShowtime.setPaddingRight(10);
//        cellShowtime.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cellShowtime.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//        PdfPCell cellTime = new PdfPCell(new Paragraph(ticket.getShowtime(), fontSubHead));
//        cellDate.setBorderColor(BaseColor.WHITE);
//        cellDate.setPaddingRight(10);
//        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    }
//
//    public void generateMovieDateCell() {
//        PdfPCell cellShowtime = new PdfPCell(new Paragraph("Date:", fontSubHead));
//        cellShowtime.setBorderColor(BaseColor.WHITE);
//        cellShowtime.setPaddingRight(10);
//        cellShowtime.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cellShowtime.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//        PdfPCell cellTime = new PdfPCell(new Paragraph(ticket.getDate(), fontSubHead));
//        cellDate.setBorderColor(BaseColor.WHITE);
//        cellDate.setPaddingRight(10);
//        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    }
//
//    public void generateSeatInfoCell() {
//        PdfPCell cellSeat = new PdfPCell(new Paragraph("Seat/Row:", fontSubHead));
//        cellSeat.setBorderColor(BaseColor.WHITE);
//        cellSeat.setPaddingRight(10);
//        cellSeat.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cellSeat.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//        PdfPCell cellSeatInfo = new PdfPCell(new Paragraph(ticket.getSeat(), fontSubHead));
//        cellSeatInfo.setBorderColor(BaseColor.WHITE);
//        cellSeatInfo.setPaddingRight(10);
//        cellSeatInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cellSeatInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    }
//
//    public void generateAudiInfoCell() {
//        PdfPCell cellAuditorium = new PdfPCell(new Paragraph("Auditorium:", fontSubHead));
//        cellSeat.setBorderColor(BaseColor.WHITE);
//        cellSeat.setPaddingRight(10);
//        cellSeat.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cellSeat.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//        PdfPCell cellAudiInfo = new PdfPCell(new Paragraph(ticket.getAudi(), fontAudi));
//        cellSeatInfo.setBorderColor(BaseColor.WHITE);
//        cellSeatInfo.setPaddingRight(10);
//        cellSeatInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cellSeatInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    }
}