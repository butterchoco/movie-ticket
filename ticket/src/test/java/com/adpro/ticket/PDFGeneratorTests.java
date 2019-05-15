package com.adpro.ticket;
import com.adpro.ticket.PDFGenerator;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.adpro.ticket.api.movies.Movie;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.movies.MovieSession;
import com.adpro.ticket.api.movies.Theater;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.validation.constraints.AssertTrue;

public class PDFGeneratorTests {

    private static String extractPdfText(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    @Test
    public void testPDFContainTemplate() throws IOException {
        Booking booking = new Booking(1L, Booking.Status.VERIFIED, Set.of(new Ticket("1A")), "ramadistra@gmail.com", 12999);
        Movie movie = Movie.builder()
                .name("Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now())
                .id(1L)
                .build();
        MovieSession movieSession = new MovieSession(movie, LocalDateTime.now());
        Theater theater = new Theater();
        theater.setId(1);
        movieSession.setTheatre(theater);
        BookingData bookingData = new BookingData(booking, movieSession);

        byte[] pdfData = PDFGenerator.generateTicket(bookingData);

        Assert.assertTrue(extractPdfText(pdfData).contains("FASILKOM THEATRE"));
    }
}
