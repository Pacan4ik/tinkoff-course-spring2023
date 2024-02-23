package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.Client;
import edu.java.clients.stackoverflow.StackOverFlowResponse;
import edu.java.clients.stackoverflow.StackOverflowClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackOverflowClientTest {
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
            get(urlEqualTo(
                "/2.3/questions/testUser?order=desc&sort=activity&site=stackoverflow&filter=!-n0mNLma5xZn(k-WDcJ*(pSqnnWWeNJ7KAELyvIpT1vQ0).WiQ5TYS"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(Files.readAllBytes(
                            Path.of(Objects.requireNonNull(GitHubClientTest.class
                                    .getResource("/StackOverflowResponseExample")
                                ).toURI()
                            )
                        )
                    )
                )
        );

        Client<StackOverFlowResponse> client = new StackOverflowClient("http://localhost:8080");

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
