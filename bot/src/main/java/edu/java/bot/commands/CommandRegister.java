package edu.java.bot.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public record CommandRegister(List<? extends Command> commandList) {
    @Autowired
    public CommandRegister {
    }

}
