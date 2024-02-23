package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.Client;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class GitHubClientTest {
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
    void shouldGetResponse() throws IOException, URISyntaxException {
        //given
        stubFor(
            get(urlEqualTo("/repos/testUser/testRep"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(GitHubClientTest.class
                                    .getResource("/GitHubResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );
        Client<GitHubResponse> client = new GitHubClient("http://localhost:8080");


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
}
