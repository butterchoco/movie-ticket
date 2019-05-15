package com.adpro.ticket.services.email;

import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.bookings.TicketGenerator;
import com.adpro.ticket.api.notifications.MessageResponse;
import com.adpro.ticket.api.notifications.UserNotificationService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


@Component
public class EmailUserNotificationService implements UserNotificationService {
    private String senderAddress;
    private EmailClient emailClient;
    private SpringTemplateEngine templateEngine;
    private TicketGenerator ticketGenerator;

    @Autowired
    public EmailUserNotificationService(String senderAddress, EmailClient emailClient,
                                        SpringTemplateEngine templateEngine,
                                        TicketGenerator ticketGenerator) {
        this.senderAddress = senderAddress;
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
        this.ticketGenerator = ticketGenerator;
    }

    private byte[] createAttachment(BookingData bookingData) {
        try {
            return ticketGenerator.generateTicket(bookingData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<MessageResponse> sendBookingData(BookingData bookingData) {
        Context context = new Context();
        context.setVariable("booking", bookingData);
        var body = new MultipartBody.Builder()
            .addFormDataPart("from", senderAddress)
            .addFormDataPart("to", bookingData.getEmail())
            .addFormDataPart("subject",
                "E-Ticket: " + bookingData.getMovieSession().getMovie().getName())
            .addFormDataPart("text", "Payment has been verified. View E-Tickets now.")
            .addFormDataPart("html", templateEngine.process("ticket-email", context))
            .addFormDataPart("attachment", "E-Ticket.pdf",
                RequestBody.create(MediaType.parse("application/pdf"),
                    createAttachment(bookingData))
            )
            .setType(MediaType.get("multipart/form-data"))
            .build();

        return emailClient.sendEmail(body);
    }
}