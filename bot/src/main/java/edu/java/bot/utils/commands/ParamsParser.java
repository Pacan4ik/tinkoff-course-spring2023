package edu.java.bot.utils.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ParamsParser {
    private final Pattern pattern = Pattern.compile("^/(\\S+) (.+)$");

    public ParamsParser() {
    }

    public List<String> getParams(String commandMessage) {
        Matcher matcher = pattern.matcher(commandMessage);
        if (!matcher.matches()) {
            return Collections.emptyList();
        }
        String fullParamsString = matcher.group(2);

        return Arrays.asList(fullParamsString.split(" "));
    }

    public Optional<String> getSingleParam(String commandMessage) {
        List<String> params = getParams(commandMessage);
        if (params.size() == 1) {
            return Optional.of(params.getFirst());
        }
        return Optional.empty();
    }
}
