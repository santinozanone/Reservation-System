package com.sz.reservation.globalConfiguration.eventRetryScheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.modulith.events.core.EventPublicationRepository;
import org.springframework.modulith.events.core.TargetEventPublication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Service
public class EventRetryScheduler {

    private final static int MAX_MINUTES = 5;
    private IncompleteEventPublications incompleteEventPublications;

    private Logger logger = LogManager.getLogger(EventRetryScheduler.class);

    @Autowired
    public EventRetryScheduler(IncompleteEventPublications incompleteEventPublications) {
        this.incompleteEventPublications = incompleteEventPublications;
    }

    @Scheduled(fixedDelay = 60000) //sixty seconds
    public void retryFailedEvents(){
        logger.info("Retrying Incomplete Events...");
        incompleteEventPublications.resubmitIncompletePublications(
                eventPublication -> ( // retry only if the time passed is <= to MAX_MINUTES
                        Duration.between(eventPublication.getPublicationDate(),Instant.now()).toMinutes() <= MAX_MINUTES));
    }

}
