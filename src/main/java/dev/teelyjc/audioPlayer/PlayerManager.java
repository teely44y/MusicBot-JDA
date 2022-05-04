package dev.teelyjc.audioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerManager {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager(AudioPlayerManager playerManager) {
        this.musicManagers = new HashMap<>();
        this.playerManager = playerManager;
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = this.musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(this.playerManager);
            this.musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(
            String keyword,
            GuildMusicManager musicManager,
            SlashCommandInteractionEvent event
    ) {
        this.playerManager.loadItemOrdered(musicManager, keyword, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                event.replyEmbeds(trackEmbed(track, event)).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (playlist.isSearchResult()) {
                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                        musicManager.scheduler.queue(firstTrack);
                        event.replyEmbeds(trackEmbed(firstTrack, event)).queue();
                        return;
                    }
                }

                for (AudioTrack track:playlist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }
                event.replyEmbeds(playlistEmded(playlist, event)).queue();
            }

            @Override
            public void noMatches() {
                event.reply("Track not found. !").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.reply("Cannot load track: " + exception).queue();
            }
        });
    }

    public void skipTrack(GuildMusicManager musicManager) {
        musicManager.scheduler.nextTrack();
    }

    public BlockingQueue<AudioTrack> getQueue(GuildMusicManager musicManager) {
        return musicManager.scheduler.getQueue();
    }

    private MessageEmbed trackEmbed(AudioTrack track, SlashCommandInteractionEvent event) {
        Number time = track.getDuration();
        String LocaleDuration = new SimpleDateFormat("mm:ss").format(time);
        return new EmbedBuilder()
                .setColor(new Color(63, 126, 209))
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .addField("Requester", Objects.requireNonNull(event.getMember()).getUser().getAsTag(), true)
                .addField("Duration", LocaleDuration, true)
                .setTimestamp(Instant.now())
                .setFooter(track.getInfo().author)
                .build();
    }

    private MessageEmbed playlistEmded(AudioPlaylist playlist, SlashCommandInteractionEvent event) {
        return new EmbedBuilder()
                .setTitle(playlist.getName(), playlist.getSelectedTrack().getInfo().uri)
                .setColor(new Color(63,126,209))
                .addField("Requester", Objects.requireNonNull(event.getMember()).getUser().getAsTag(), true)
                .addField("Length", (playlist.getTracks().size() + " tracks"), true)
                .setTimestamp(Instant.now())
                .setFooter(playlist.getTracks().get(0).getInfo().author)
                .build();
    }


}
