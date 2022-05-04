package dev.teelyjc.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import dev.teelyjc.DiscordBot;
import dev.teelyjc.audioPlayer.GuildMusicManager;
import dev.teelyjc.audioPlayer.PlayerManager;
import dev.teelyjc.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public abstract class AudioCommand implements Command {
    protected DiscordBot discordBot;

    protected AudioManager audioManager;

    protected GuildMusicManager musicManager;

    protected PlayerManager playerManager;

    public AudioCommand(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public abstract String getCommandName();

    @Override
    public abstract void registerCommand(JDA client);

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        Member executor = event.getMember();
        GuildVoiceState voiceState = executor.getVoiceState();

        if (voiceState.getChannel() == null) {
            event.reply("Please join voice channel to use voice commands.").queue();
            return;
        }

        this.playerManager = this.discordBot.getPlayerManager();
        this.musicManager = playerManager.getGuildMusicManager(event.getGuild());

        this.audioManager = event.getGuild().getAudioManager();

        if (musicManager == null) {
            event.reply("Unavailable").queue();
            return;
        }

        this.executeAudioCommand(event, executor);
    }

    protected abstract void executeAudioCommand(
            SlashCommandInteractionEvent event,
            Member executor
    );
}
