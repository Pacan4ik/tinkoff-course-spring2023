package edu.java.bot.commands;

public abstract class AbstractCommand implements Command {
    @Override
    public String toString() {
        return command() + "\t - \t" + description();
    }
}
