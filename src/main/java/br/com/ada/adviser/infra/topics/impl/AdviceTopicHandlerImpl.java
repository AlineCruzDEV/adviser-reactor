package br.com.ada.adviser.infra.topics.impl;

import br.com.ada.adviser.infra.topics.AdviceTopicHandler;
import br.com.ada.adviser.infra.topics.AdviceTopicPublisher;
import br.com.ada.adviser.infra.topics.email.SendEmail;
import br.com.ada.adviser.web.dto.response.AdviceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AdviceTopicHandlerImpl implements AdviceTopicHandler {

    @Autowired
    private AdviceTopicPublisher adviceTopicPublisher;

    @Autowired
    private SendEmail sendEmail;

    public AdviceTopicHandlerImpl(AdviceTopicPublisher newsletterPublisher) {
        newsletterPublisher.getNewsFlux().subscribeOn(Schedulers.newSingle("New thread")).subscribe(
            advice -> {
                try {
                    final List<String> words = splitAdvicesInWords(advice);
                    sendEmail.send(advice);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            },
            error -> log.error("Error " + error)
        );
    }

    @Override
    public void sendTopic(AdviceResponse adviceResponse) {
        adviceTopicPublisher.publish(adviceResponse);
    }

    private List<String> splitAdvicesInWords(final AdviceResponse adviceResponse) {
        final List<String> words = Arrays.asList(adviceResponse.getAdvice().split(" "));
        words.forEach(w -> log.info(w));
        return words;
    }

}