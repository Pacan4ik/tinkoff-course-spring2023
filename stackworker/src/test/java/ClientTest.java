import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import edu.tinkoff.retry.backoff.BackOff;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.client.StackOverflowClient;
import ru.andryxx.stackworker.configuration.ApplicationConfig;
import ru.andryxx.stackworker.configuration.ClientConfig;
import ru.andryxx.stackworker.configuration.RetryConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties(value = ApplicationConfig.class)
@AutoConfigureWebClient
@ContextConfiguration(classes = {ClientConfig.class, RetryConfig.class}, initializers = {
    ConfigDataApplicationContextInitializer.class})
public class ClientTest {
    static WireMockServer wireMockServer;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ClientConfig clientsConfig;

    @Autowired
    RetryConfig retryTemplatesConfig;

    @Autowired
    StackOverflowClient stackoverflowClient;

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
                    2L,
                    20089818L
                ))
            ), stackOverFlowResponse

        );

    }

    @Test
    void shouldRetryOnError() throws URISyntaxException, IOException {
        //given
        String testUrl = "/2.3/questions/testUser?site=stackoverflow&filter=!nNPvSNR9ie";
        stubFor(
            get(urlEqualTo(testUrl))
                .inScenario("retry")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step1")
        );
        stubFor(
            get(urlEqualTo(testUrl))
                .inScenario("retry")
                .whenScenarioStateIs("step1")
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step2")
        );
        stubFor(
            get(urlEqualTo(testUrl))
                .inScenario("retry")
                .whenScenarioStateIs("step2")
                .willReturn(aResponse()
                    .withStatus(500)
                )
                .willSetStateTo("step3")
        );
        stubFor(
            get(urlEqualTo(testUrl))
                .inScenario("retry")
                .whenScenarioStateIs("step3")
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

        RetryTemplate template = retryTemplatesConfig.getRetryTemplate(
            retryTemplatesConfig.retryListener(),
            new BackOff(
                4,
                Duration.ofSeconds(1),
                BackOff.Policy.CONSTANT,
                null,
                null
            )
        );

        //when
        StackOverflowClient stackOverflowClient =
            new StackOverflowClient(webClientBuilder, "http://localhost:8080/", template);

        //then
        Assertions.assertDoesNotThrow(() -> stackOverflowClient.fetchResponse("testUser"));
    }
}
