import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.scrapperClient.model.LinkResponse;
import edu.java.bot.scrapperClient.model.ListLinksResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ScrapperClientTest {
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
    void shouldRegisterChat() {
        //given
        stubFor(
            post(urlEqualTo(
                "/tg-chat/1"))
                .willReturn(aResponse()
                    .withStatus(200)
                )
        );

        //when
        ScrapperClient client = new ScrapperClient(WebClient.builder(), "http://localhost:8080");

        //then
        Assertions.assertDoesNotThrow(() -> client.registerChat(1L));

    }

    @Test
    void shouldDeleteChat() {
        //given
        stubFor(
            delete(urlEqualTo(
                "/tg-chat/1"))
                .willReturn(aResponse()
                    .withStatus(200)
                )
        );

        //when
        ScrapperClient client = new ScrapperClient(WebClient.builder(), "http://localhost:8080");

        //then
        Assertions.assertDoesNotThrow(() -> client.deleteChat(1L));

    }

    @Test
    void shouldGetLinks() {
        //given
        stubFor(
            get(urlEqualTo(
                "/links")).withHeader("Tg-Chat-Id", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "links": [
                            {
                              "id": 0,
                              "url": "https://example.com/"
                            },
                            {
                              "id": 1,
                              "url": "https://example2.com/"
                            }
                          ],
                          "size": 2
                        }""")
                )
        );

        //when
        ScrapperClient client = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        ListLinksResponse response = client.getTrackingLinks(1L).getBody();

        //then
        ListLinksResponse expected = new ListLinksResponse(List.of(
            new LinkResponse(0L, URI.create("https://example.com/")),
            new LinkResponse(1L, URI.create("https://example2.com/"))
        ), 2
        );
        Assertions.assertEquals(expected, response);
    }

    @Test
    void shouldPostLink() {
        //given
        stubFor(
            post(urlEqualTo(
                "/links")).withHeader("Tg-Chat-Id", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                         {
                           "id": 0,
                           "url": "https://example.com/"
                         }
                        """)
                )
        );

        //when
        ScrapperClient client = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        LinkResponse response = client.addTrackingLink(1L, "https://example.com/").getBody();

        //then
        LinkResponse expected = new LinkResponse(0L, URI.create("https://example.com/"));
        Assertions.assertEquals(expected, response);
    }

    @Test
    void shouldDeleteLink() {
        //given
        stubFor(
            delete(urlEqualTo(
                "/links")).withHeader("Tg-Chat-Id", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                         {
                           "id": 0,
                           "url": "https://example.com/"
                         }
                        """)
                )
        );

        //when
        ScrapperClient client = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        LinkResponse response = client.deleteLink(1L, "https://example.com/").getBody();

        //then
        LinkResponse expected = new LinkResponse(0L, URI.create("https://example.com/"));
        Assertions.assertEquals(expected, response);
    }
}
