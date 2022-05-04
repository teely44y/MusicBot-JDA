package dev.teelyjc.commands.audio;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import dev.teelyjc.DiscordBot;
import dev.teelyjc.audioPlayer.GuildMusicManager;
import dev.teelyjc.audioPlayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class PlayCommand extends AudioCommand {

    public PlayCommand(DiscordBot discordBot) {
        super(discordBot);
    }

    @Override
    public String getCommandName() {
        return "play";
    }

    @Override
    public void registerCommand(JDA client) {
        client.upsertCommand("play", "play songs")
                .addOption(OptionType.STRING, "song", "song name/link", true)
                .queue();
    }

    @Override
    protected void executeAudioCommand(
            SlashCommandInteractionEvent event,
            Member executor
    ) {
        String keyword = Objects.requireNonNull(event.getOption("song")).getAsString();

        if (!isURL(keyword)) {
            keyword = "ytsearch:" + keyword;
        }

        VoiceChannel voiceChannel = (VoiceChannel)
                Objects.requireNonNull(executor.getVoiceState()).getChannel();

        this.audioManager.openAudioConnection(voiceChannel);
        this.playerManager.loadAndPlay(keyword, this.musicManager, event);
    }

    private boolean isURL(String keyword) {
        try {
            new URI(keyword);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
