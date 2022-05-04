package dev.teelyjc.commands.common;

import dev.teelyjc.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements Command {
    @Override
    public String getCommandName() {
        return "ping";
    }

    @Override
    public void registerCommand(JDA client) {
        client.upsertCommand("ping", "reply with pong").queue();
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        event.reply("pong!").queue();
    }
}
