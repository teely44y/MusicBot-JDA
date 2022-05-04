package dev.teelyjc.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final JDA client;
    private final ArrayList<String> commandName = new ArrayList<String>();
    private final Map<String, Command> commandMap = new HashMap<String, Command>();

    public CommandManager(JDA client) {
        this.client = client;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // execute command that equal in commandMap.
        String CommandName = event.getName();
        Command command = this.commandMap.get(CommandName);

        try {
            // if command equal commandName is in commandMap -> executeCommand
            command.executeCommand(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void registerCommand(Command commands) {
        commands.registerCommand(client);
        this.commandName.add(commands.getCommandName());
        this.commandMap.put(commands.getCommandName(), commands);
    }

    public void listAllCommand() {
        System.out.printf("Register %s commands to DiscordAPI%n", this.commandName.size());
    }

}
