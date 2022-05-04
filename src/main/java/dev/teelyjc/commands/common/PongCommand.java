package dev.teelyjc.commands.common;

import dev.teelyjc.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Objects;

public class PongCommand implements Command {
    @Override
    public String getCommandName() {
        return "pong";
    }

    @Override
    public void registerCommand(JDA client) {
        client.upsertCommand("pong", "reply with ping")
                .addOption(OptionType.STRING, "value", "value", false)
                .queue();
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String value = Objects.requireNonNull(event.getOption("value")).getAsString();
        event.reply("Value is " + value).queue();
    }
}
