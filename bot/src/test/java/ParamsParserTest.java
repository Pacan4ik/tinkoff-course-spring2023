import edu.java.bot.utils.commands.ParamsParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class ParamsParserTest {
    @Test
    void shouldReturnParams() {
        //given
        String userCommand = "/test param1 param2";
        ParamsParser paramsParser = new ParamsParser();

        //when
        List<String> ans = paramsParser.getParams(userCommand);

        //then
        Assertions.assertEquals(List.of("param1", "param2"), ans);
    }

    @Test
    void shouldReturnEmptyList() {
        //given
        String userCommand = "/test  ";
        ParamsParser paramsParser = new ParamsParser();

        //when
        List<String> ans = paramsParser.getParams(userCommand);

        //then
        Assertions.assertTrue(ans.isEmpty());

    }

    @Test
    void shouldReturnSingleParam() {
        //given
        String userCommand = "/test param";
        ParamsParser paramsParser = new ParamsParser();

        //when
        String ans = paramsParser.getSingleParam(userCommand);

        //then
        Assertions.assertEquals("param", ans);
    }

    @Test
    void shouldReturnNullWithMoreParams() {
        //given
        String userCommand = "/test param1 param1";
        ParamsParser paramsParser = new ParamsParser();

        //when
        String ans = paramsParser.getSingleParam(userCommand);

        //then
        Assertions.assertNull(ans);
    }
}
