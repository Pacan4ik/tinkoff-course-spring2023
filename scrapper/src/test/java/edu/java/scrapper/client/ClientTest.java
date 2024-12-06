package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientsConfig;
import edu.java.scrapper.configuration.retry.RetryTemplatesConfig;
import edu.tinkoff.retry.backoff.BackOff;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

@SpringBootTest
@EnableConfigurationProperties(value = ApplicationConfig.class)
@AutoConfigureWebClient
@ContextConfiguration(classes = {ClientsConfig.class, RetryTemplatesConfig.class},
                      initializers = ConfigDataApplicationContextInitializer.class)
public class ClientTest {
    static WireMockServer wireMockServer;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ClientsConfig clientsConfig;

    @Autowired
    RetryTemplatesConfig retryTemplatesConfig;

    @Autowired
    BotClient botClient;

    @BeforeAll
    static void configureWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @AfterAll
    static void shutdownWireMock() {
        wireMockServer.shutdown();
    }

    @Test
    void shouldGetResponseBotClient() {
        //given
        stubFor(
            post(urlEqualTo(
                "/updates"))
                .willReturn(aResponse()
                    .withStatus(200)
                )
        );
        //when
        HttpStatusCode statusCode = botClient.sendUpdates(new BotUpdatesRequest(
                1L,
                URI.create("https://example.com/"),
                "descrpiption",
                List.of(1L, 2L)
            )
        ).getStatusCode();

        //then
        Assertions.assertTrue(statusCode.is2xxSuccessful());

    }

    @Test
    void shouldntRetryOnForbidden() {
        //given
        stubFor(
            post(urlEqualTo(
                "/updates"))
                .inScenario("retryForbidden")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                    .withStatus(403)
                )
                .willSetStateTo("success")
        );
        stubFor(
            post(urlEqualTo(
                "/updates"))
                .inScenario("retryForbidden")
                .whenScenarioStateIs("success")
                .willReturn(aResponse()
                    .withStatus(200)
                )
        );

        RetryTemplate template = retryTemplatesConfig.getRetryTemplate(
            retryTemplatesConfig.retryListener(),
            new BackOff(
                10,
                Duration.ofSeconds(5),
                BackOff.Policy.LINEAR,
                null,
                null
            )
        );

        //when
        BotClient botClient = new BotClient(webClientBuilder, "http://localhost:8080", template);

        //then
        Assertions.assertThrows(
            WebClientResponseException.class,
            () -> botClient.sendUpdates(new BotUpdatesRequest(
                    1L,
                    URI.create("https://example.com/"),
                    "descrpiption",
                    List.of(1L, 2L)
                )
            )
        );
    }

}
