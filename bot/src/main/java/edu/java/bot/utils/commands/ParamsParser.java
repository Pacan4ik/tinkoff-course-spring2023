package edu.java.bot.utils.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ParamsParser {
    private final Pattern pattern = Pattern.compile("^/(\\S+) (.+)$");

    public ParamsParser() {
    }

    public List<String> getParams(String commandMessage) {
        List<String> params = Collections.emptyList();
        Matcher matcher = pattern.matcher(commandMessage);
        if (!matcher.matches()) {
            return params;
        }
        String fullParamsString = matcher.group(2);
        params = Arrays.asList(fullParamsString.split(" "));
        return params;

    }

    public String getSingleParam(String commandMessage) {
        List<String> params = getParams(commandMessage);
        if (params.size() == 1) {
            return params.getFirst();
        }
        return null;
    }
}
