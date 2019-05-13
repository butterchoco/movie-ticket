package com.adpro.ticket;

import com.adpro.ticket.api.EmailClient;
import com.adpro.ticket.utils.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class EmailConfig {
    @Bean
    public EmailClient getEmailClient() {
        final String BASE_URL = "https://api.mailgun.net/v3/sandboxcc639084c2a0431db7271570b5b5b421.mailgun.org/";
        final String API_KEY = "51a76eda02d0eb75bb4825065d2747c8-059e099e-c62d7920";
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("api", API_KEY))
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(EmailClient.class);
    }

    @Bean
    public String getSenderAddress() {
        return "Do Not Reply <do-not-reply@sandboxcc639084c2a0431db7271570b5b5b421.mailgun.org>";
    }
}
