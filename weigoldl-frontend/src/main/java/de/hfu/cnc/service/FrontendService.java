package de.hfu.cnc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FrontendService {
    private static final Logger LOG = LoggerFactory.getLogger(FrontendService.class);
    private final WebClient workerWebClient;
    private DirectExchange direct;
    private RabbitTemplate template;

    /*
     * Predefined values in the application.yaml:
     * routingKey -> weigoldl-kubecoin
     */
    @Value("${frontend.routingKey}")
    private String routingKey;

    // Constructor Injection
    public FrontendService(WebClient.Builder webClientBuilder,
                           DirectExchange direct,
                           RabbitTemplate template,
                           @Value("${frontend.workerBackend}") String workerServiceBackend) {

        this.direct = direct;
        this.template = template;

        this.workerWebClient = webClientBuilder
                .baseUrl(workerServiceBackend)
                .build();
    }



    // Sending-method with the amount of requests as parameter [1, 100 , 1000].
    public void sendRequest(int amountOfRequests) {
        LOG.info("Sending " + amountOfRequests + " Requests to queue");

        for (int i = 0; i < amountOfRequests; i++) {
            template.convertAndSend(direct.getName(), routingKey, routingKey + " 'message body'");
        }
    }


    public Long getTotalAmountOfCoins() {
        return this.workerWebClient
                .get()
                .uri("/total")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public String getLastCoin() {
        return this.workerWebClient
                .get()
                .uri("/last")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteCoins() {
        this.workerWebClient
                .delete()
                .uri("/delete")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
