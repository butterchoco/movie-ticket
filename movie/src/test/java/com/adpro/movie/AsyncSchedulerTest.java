package com.adpro.movie;

import static org.mockito.BDDMockito.then;

import com.adpro.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class AsyncSchedulerTest {

    @Mock
    private Scheduler scheduler;

    private AsyncScheduler asyncScheduler;

    @Before
    public void init() {
        asyncScheduler = new AsyncScheduler(scheduler);
    }

    @Test
    public void givenPostConstruct_thenUpdateMovieList() {
        asyncScheduler.postConstruct();
        then(scheduler)
                .should()
                .updateMovieSessionList();
    }

    @Test
    public void givenMidnightCron_thenUpdateMovieList() {
        asyncScheduler.midnightCron();
        then(scheduler)
                .should()
                .updateMovieSessionList();
    }
}
