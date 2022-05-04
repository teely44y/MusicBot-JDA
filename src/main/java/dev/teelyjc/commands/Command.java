package dev.teelyjc.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    String getCommandName();
    void registerCommand(JDA client);
    void executeCommand(SlashCommandInteractionEvent event);
}
