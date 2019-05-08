package com.adpro.ticket;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;

import com.itextpdf.text.Element;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;

import model.Booking;
import model.Ticket;

public class PDFGenerator {
    public static void main(String[] args) throws DocumentException, IOException {
        Document doc = new Document();
        try {
            response.setContentType("application/pdf");
            PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());
            doc.open();

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f, 1f};
            table.setWidths(columnWidths);

            Font fontHead = new Font(Font.HELVETICA, 30, Font.BOLDITALIC, Color.RED);
            Font fontSubHead = new Font(FONT.HELVETICA, 20, Font.BOLD, Color.BLACK);
            Font fontAudi = new Font(FONT.HELVETICA, 60, Font.BOLD, Color.BLACK);

            generateMovieCell();
            generateMovieDateCell();
            generateShowtimeCell();
            generateSeatInfoCell();
            generateAudiInfoCell();

            document.addAuthor("C-8 Advance Programming");
            document.addCreationDate();
            document.addTitle("Movie Ticket");

            doc.close();
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateMovieCell(){
        PdfPCell cellMovie = new PdfPCell(new Paragraph("Movie:", fontSubHead));
        cellMovie.setBorderColor(BaseColor.WHITE);
        cellMovie.setPaddingRight(10);
        cellMovie.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMovie.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellMovieTitle = new PdfPCell(new Paragraph(ticket.getMovie(), fontSubHead));
        cellMovieTitle.setBorderColor(BaseColor.WHITE);
        cellMovieTitle.setPaddingRight(10);
        cellMovieTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMovieTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public void generateShowtimeCell() {
        PdfPCell cellShowtime = new PdfPCell(new Paragraph("Showtime:", fontSubHead));
        cellShowtime.setBorderColor(BaseColor.WHITE);
        cellShowtime.setPaddingRight(10);
        cellShowtime.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellShowtime.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellTime = new PdfPCell(new Paragraph(ticket.getShowtime(), fontSubHead));
        cellDate.setBorderColor(BaseColor.WHITE);
        cellDate.setPaddingRight(10);
        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public void generateMovieDateCell() {
        PdfPCell cellShowtime = new PdfPCell(new Paragraph("Date:", fontSubHead));
        cellShowtime.setBorderColor(BaseColor.WHITE);
        cellShowtime.setPaddingRight(10);
        cellShowtime.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellShowtime.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellTime = new PdfPCell(new Paragraph(ticket.getDate(), fontSubHead));
        cellDate.setBorderColor(BaseColor.WHITE);
        cellDate.setPaddingRight(10);
        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public void generateSeatInfoCell() {
        PdfPCell cellSeat = new PdfPCell(new Paragraph("Seat/Row:", fontSubHead));
        cellSeat.setBorderColor(BaseColor.WHITE);
        cellSeat.setPaddingRight(10);
        cellSeat.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSeat.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellSeatInfo = new PdfPCell(new Paragraph(ticket.getSeat(), fontSubHead));
        cellSeatInfo.setBorderColor(BaseColor.WHITE);
        cellSeatInfo.setPaddingRight(10);
        cellSeatInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSeatInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public void generateAudiInfoCell() {
        PdfPCell cellAuditorium = new PdfPCell(new Paragraph("Auditorium:", fontSubHead));
        cellSeat.setBorderColor(BaseColor.WHITE);
        cellSeat.setPaddingRight(10);
        cellSeat.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSeat.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellAudiInfo = new PdfPCell(new Paragraph(ticket.getAudi(), fontAudi));
        cellSeatInfo.setBorderColor(BaseColor.WHITE);
        cellSeatInfo.setPaddingRight(10);
        cellSeatInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSeatInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }
}