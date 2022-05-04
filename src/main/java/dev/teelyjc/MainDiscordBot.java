package dev.teelyjc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class MainDiscordBot {
    public MainDiscordBot(JDA client) throws InterruptedException {
        client.addEventListener(new DiscordBot(client));

        client.setAutoReconnect(true);
        client.awaitReady();
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        final String token = "BOT TOKEN";
        JDABuilder clientBuilt = JDABuilder.createDefault(token);

        JDA client = clientBuilt.build();
        new MainDiscordBot(client);
    }
}
