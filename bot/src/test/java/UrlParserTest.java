import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.SimpleUrlParser;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UrlParserTest {

    static Arguments[] links() {
        return new Arguments[] {
            Arguments.of(
                "https://google.com/1/2/3",
                new ParsedUrl("https", "google.com", null, "/1/2/3", null)
            ),
            Arguments.of(
                "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
                new ParsedUrl("https", "stackoverflow.com", null, "/questions/1642028/what-is-the-operator-in-c", null)
            ),
            Arguments.of(
                "https://www.youtube.com/",
                new ParsedUrl("https", "www.youtube.com", null, null, null)
            )
        };
    }

    @ParameterizedTest
    @MethodSource("links")
    void shouldParse(String link, ParsedUrl expected) throws URLSyntaxException {
        //given
        UrlParser urlParser = new SimpleUrlParser();

        //when
        ParsedUrl parsedUrl = urlParser.parse(link);

        //then
        Assertions.assertEquals(expected, parsedUrl);

    }

    static Arguments[] wrongLinks() {
        return new Arguments[] {
            Arguments.of("www.baeldung.com"),
            Arguments.of("https:\\\\github.com"),
            Arguments.of("stackoverflow.com/questions/")
        };
    }

    @ParameterizedTest
    @MethodSource("wrongLinks")
    void shouldThrowException(String link) {
        Assertions.assertThrows(URLSyntaxException.class, () -> new SimpleUrlParser().parse(link));
    }
}
