package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.clients.Client;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
    static WireMockServer wireMockServer;

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
        Client<GitHubResponse> client = new GitHubClient(WebClient.builder(), "http://localhost:8080");

        //when
        GitHubResponse gitHubResponse = client.fetchResponse("testUser", "testRep");

        //then
        Assertions.assertEquals(
            new GitHubResponse(
                OffsetDateTime.parse("2024-02-20T17:37:57Z"),
                OffsetDateTime.parse("2024-02-02T10:09:10Z"),
                "https://github.com/Pacan4ik/tinkoff-course-spring2023",
                null
            ), gitHubResponse

        );

    }

    @Test
    void shouldGetResponseStackOverflow() throws IOException, URISyntaxException {
        //given
        stubFor(
            get(urlEqualTo(
                "/2.3/questions/testUser"))
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

        Client<StackOverFlowResponse> client = new StackOverflowClient(WebClient.builder(), "http://localhost:8080");

        //when
        StackOverFlowResponse stackOverFlowResponse = client.fetchResponse("testUser");

        //then
        assertEquals(
            new StackOverFlowResponse(
                List.of(new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2014-05-30T05:29:52Z"),
                    OffsetDateTime.parse("2014-01-19T23:06:34Z"),
                    "https://stackoverflow.com/questions/20089818/get-questions-content-from-stack-exchange-api",
                    "Get questions content from Stack Exchange API"
                ))
            ), stackOverFlowResponse

        );

    }
}
