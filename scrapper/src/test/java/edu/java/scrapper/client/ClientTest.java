package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientsConfig;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import edu.java.scrapper.configuration.retry.RetryTemplatesConfig;
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
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    GitHubClient githubClient;

    @Autowired
    StackOverflowClient stackoverflowClient;

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
    void shouldGetResponseGitHub() throws IOException, URISyntaxException {
        //given
        stubFor(
            get(urlEqualTo("/repos/testUser/testRep"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(ClientTest.class
                                    .getResource("/GitHubResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );

        //when
        GitHubResponse gitHubResponse = githubClient.fetchResponse("testUser", "testRep");

        //then
        Assertions.assertEquals(
            new GitHubResponse(
                OffsetDateTime.parse("2024-02-20T17:37:57Z"),
                OffsetDateTime.parse("2024-02-02T10:09:10Z"),
                "https://github.com/Pacan4ik/tinkoff-course-spring2023",
                null,
                1L
            ), gitHubResponse

        );

    }

    @Test
    void shouldGetResponseStackOverflow() throws IOException, URISyntaxException {
        //given
        stubFor(
            get(urlEqualTo(
                "/2.3/questions/testUser?site=stackoverflow&filter=!nNPvSNR9ie"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(ClientTest.class
                                    .getResource("/StackOverflowResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );

        //when
        StackOverFlowResponse stackOverFlowResponse = stackoverflowClient.fetchResponse("testUser");

        //then
        assertEquals(
            new StackOverFlowResponse(
                List.of(new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2014-05-30T05:29:52Z"),
                    OffsetDateTime.parse("2014-01-19T23:06:34Z"),
                    "https://stackoverflow.com/questions/20089818/get-questions-content-from-stack-exchange-api",
                    "Get questions content from Stack Exchange API",
                    0L,
                    2L
                ))
            ), stackOverFlowResponse

        );

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
        HttpStatusCode statusCode = botClient.sendUpdates(
            1L,
            URI.create("https://example.com/"),
            "descrpiption",
            List.of(1L, 2L)
        ).getStatusCode();

        //then
        Assertions.assertTrue(statusCode.is2xxSuccessful());

    }

    @Test
    void shouldRetryOnError() throws URISyntaxException, IOException {
        //given
        stubFor(
            get(urlEqualTo("/repos/testUser/onError"))
                .inScenario("retry")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step1")
        );
        stubFor(
            get(urlEqualTo("/repos/testUser/onError"))
                .inScenario("retry")
                .whenScenarioStateIs("step1")
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step2")
        );
        stubFor(
            get(urlEqualTo("/repos/testUser/onError"))
                .inScenario("retry")
                .whenScenarioStateIs("step2")
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step3")
        );
        stubFor(
            get(urlEqualTo("/repos/testUser/onError"))
                .inScenario("retry")
                .whenScenarioStateIs("step3")
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(ClientTest.class
                                    .getResource("/GitHubResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );

        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new SimpleRetryPolicy(4);
        retryTemplatesConfig.retryOnStatusCustomizer(retryPolicy, null).accept(retryTemplateBuilder);
        retryTemplatesConfig.retryBackoffPolicyCustomizer(new ApplicationConfig.Client.BackOff(
            4,
            Duration.ofSeconds(1),
            ApplicationConfig.Client.BackOff.Policy.CONSTANT,
            null,
            null
        )).accept(retryTemplateBuilder);

        //when
        GitHubClient githubClient =
            new GitHubClient(webClientBuilder, "http://localhost:8080/", retryTemplateBuilder.build());

        //then
        Assertions.assertDoesNotThrow(() -> githubClient.fetchResponse("testUser", "onError"));
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

        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(10);
        retryTemplatesConfig.retryOnStatusCustomizer(retryPolicy, Collections.emptyList()).accept(retryTemplateBuilder);
        retryTemplatesConfig.retryBackoffPolicyCustomizer(new ApplicationConfig.Client.BackOff(
            10,
            Duration.ofSeconds(5),
            ApplicationConfig.Client.BackOff.Policy.LINEAR,
            null,
            null
        )).accept(retryTemplateBuilder);

        //when
        BotClient botClient = new BotClient(webClientBuilder, "http://localhost:8080", retryTemplateBuilder.build());

        //then
        Assertions.assertThrows(
            WebClientResponseException.class,
            () -> botClient.sendUpdates(
                1L,
                URI.create("https://example.com/"),
                "descrpiption",
                List.of(1L, 2L)
            )
        );
    }

}
