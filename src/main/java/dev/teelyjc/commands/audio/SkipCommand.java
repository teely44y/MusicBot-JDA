package dev.teelyjc.commands.audio;

import dev.teelyjc.DiscordBot;
import dev.teelyjc.audioPlayer.GuildMusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SkipCommand extends AudioCommand {
    public SkipCommand(DiscordBot discordBot) {
        super(discordBot);
    }

    @Override
    public String getCommandName() {
        return "skip";
    }

    @Override
    public void registerCommand(JDA client) {
        client.upsertCommand("skip", "skip song")
                .addOption(OptionType.INTEGER, "count", "value of song to skip")
                .queue();
    }

    @Override
    protected void executeAudioCommand(
            SlashCommandInteractionEvent event,
            Member executor
    ) {
        int valueToSkip = event.getOption("count").getAsInt() | 1;

        for (int i = 0; i < valueToSkip; i += 1) {
            this.playerManager.skipTrack(this.musicManager);
        }

        event.reply("Skipped " + valueToSkip + " songs.").queue();
    }
}
