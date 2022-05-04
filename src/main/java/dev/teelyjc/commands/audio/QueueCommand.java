package dev.teelyjc.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.teelyjc.DiscordBot;
import dev.teelyjc.audioPlayer.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends AudioCommand {
    public QueueCommand(DiscordBot discordBot) {
        super(discordBot);
    }

    @Override
    public String getCommandName() {
        return "queue";
    }

    @Override
    public void registerCommand(JDA client) {
        client.upsertCommand("queue", "get queue").queue();
    }

    @Override
    protected void executeAudioCommand(SlashCommandInteractionEvent event, Member executor) {
        BlockingQueue<AudioTrack> guildQueue = this.playerManager.getQueue(this.musicManager);
        event.replyEmbeds(this.queueEmbed(guildQueue)).queue();
    }

    private MessageEmbed queueEmbed(BlockingQueue<AudioTrack> queue) {
        return new EmbedBuilder()
                .setTitle("Queue List")
                .setColor(new Color(63, 126, 209))
                .addField("Upcoming Track", "TODO: QUEUE LIST", true)
                .setTimestamp(Instant.now())
                .build();
    }
}
