import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andryxx.gitworker.client.GitHubClient;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.configuration.ApplicationConfig;
import ru.andryxx.gitworker.configuration.ClientConfig;
import ru.andryxx.gitworker.configuration.RetryConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
@EnableConfigurationProperties(value = ApplicationConfig.class)
@AutoConfigureWebClient
@ContextConfiguration(classes = {ClientConfig.class, RetryConfig.class},
                      initializers = ConfigDataApplicationContextInitializer.class)
@Slf4j
public class ClientTest {
    static WireMockServer wireMockServer;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ClientConfig clientsConfig;

    @Autowired
    RetryConfig retryTemplatesConfig;

    @Autowired
    GitHubClient githubClient;

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
            get(urlEqualTo("/repos/testUser/testRep/events?page=1"))
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
         List<GitHubEventResponse> gitHubResponse = githubClient.fetchResponse("testUser", "testRep", 1);

        //then
        Assertions.assertEquals(2, gitHubResponse.size());
        Assertions.assertEquals(44098989393L, gitHubResponse.get(0).id());
        Assertions.assertEquals(44098871806L, gitHubResponse.get(1).id());
        Assertions.assertEquals("PullRequestEvent", gitHubResponse.get(0).type());
        Assertions.assertEquals("CreateEvent", gitHubResponse.get(1).type());
    }
}
