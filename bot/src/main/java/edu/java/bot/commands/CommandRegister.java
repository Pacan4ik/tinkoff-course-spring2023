package edu.java.bot.commands;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public record CommandRegister(List<? extends Command> commandList) {
    public CommandRegister {
    }

}
