package dev.teelyjc;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.teelyjc.audioPlayer.PlayerManager;
import dev.teelyjc.commands.CommandManager;
import dev.teelyjc.commands.audio.PlayCommand;
import dev.teelyjc.commands.audio.QueueCommand;
import dev.teelyjc.commands.audio.SkipCommand;
import dev.teelyjc.commands.common.PingCommand;
import dev.teelyjc.commands.common.PongCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class DiscordBot implements EventListener {

    private PlayerManager player;
    private AudioPlayerManager playerManager;
    private final CommandManager commandManager;

    public DiscordBot(JDA client) {

        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.getConfiguration()
                .setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        AudioSourceManagers.registerLocalSource(this.playerManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager);
        this.player = new PlayerManager(this.playerManager);
        // handle commands
        this.commandManager = new CommandManager(client);
        this.commandManager.registerCommand(new PingCommand());
        this.commandManager.registerCommand(new PongCommand());
        this.commandManager.registerCommand(new PlayCommand(this));
        this.commandManager.registerCommand(new SkipCommand(this));
        this.commandManager.registerCommand(new QueueCommand(this));
        this.commandManager.listAllCommand();
        // execute command by eventListener
        client.addEventListener(this.commandManager);
    }

    public PlayerManager getPlayerManager() {
        return this.player;
    }

    @Override
    public void onEvent(GenericEvent event) {
        JDA client = event.getJDA();
        if (event instanceof ReadyEvent) {
            System.out.printf("Logged in as %s!%n", client.getSelfUser().getAsTag());
        }
    }
}
