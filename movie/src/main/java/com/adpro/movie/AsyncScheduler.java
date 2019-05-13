package com.adpro.movie;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncScheduler {

    private Scheduler scheduler;

    @Autowired
    public AsyncScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void midnightCron() {
        scheduler.updateMovieSessionList();
    }

    @PostConstruct
    public void postConstruct() {
        scheduler.updateMovieSessionList();
    }

}
