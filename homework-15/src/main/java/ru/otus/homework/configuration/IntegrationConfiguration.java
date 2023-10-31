package ru.otus.homework.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.homework.service.BreadService;
import ru.otus.homework.service.DoughService;
import ru.otus.homework.service.FlourService;

@Configuration
public class IntegrationConfiguration {

    @Bean
    public MessageChannelSpec<?, ?> grainChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> breadChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow breadProductionFlow(
            FlourService flourService,
            DoughService doughService,
            BreadService breadService
    ) {
        return IntegrationFlow.from(grainChannel())
                .split()
                .transform(flourService, "toFlour")
                .transform(doughService, "toDough")
                .transform(breadService, "toBread")
                .aggregate()
                .channel(breadChannel())
                .get();
    }

}
