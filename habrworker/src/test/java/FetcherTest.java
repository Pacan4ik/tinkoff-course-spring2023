import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andryxx.habrworker.client.RawHtmlClient;
import ru.andryxx.habrworker.configuration.ApplicationConfig;
import ru.andryxx.habrworker.configuration.ClientConfig;
import ru.andryxx.habrworker.configuration.ParserConfig;
import ru.andryxx.habrworker.configuration.RetryConfig;
import ru.andryxx.habrworker.services.fetchers.DefaultHabrFetcher;
import ru.andryxx.habrworker.services.fetchers.HabrHtmlParser;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@EnableConfigurationProperties(value = ApplicationConfig.class)
@AutoConfigureWebClient
@ContextConfiguration(classes = {ClientConfig.class, RetryConfig.class, ParserConfig.class}, initializers = {
    ConfigDataApplicationContextInitializer.class})
public class FetcherTest {
    static WireMockServer wireMockServer;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ClientConfig clientsConfig;

    @Autowired
    RetryConfig retryTemplatesConfig;

    @Autowired
    HabrHtmlParser parser;

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
    void shouldFetchHabr() throws URISyntaxException, IOException {
        //given
        RawHtmlClient client = new RawHtmlClient(webClientBuilder);
        DefaultHabrFetcher fetcher = new DefaultHabrFetcher(client, parser);

        stubFor(
            get(urlEqualTo(
                "/ru/articles/769384/"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(FetcherTest.class
                                    .getResource("/HabrResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );

        //when
        HabrDTO fetch = fetcher.fetch(URI.create("http://localhost:8080/ru/articles/769384/"));

        //then
        assertEquals(3, fetch.commentCount());
        assertNotNull(fetch.sha1Hash());
    }
}
