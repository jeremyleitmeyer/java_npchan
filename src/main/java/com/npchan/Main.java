package com.npchan;


import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main (String[] args) {
        new DiscordApiBuilder().setToken(System.getenv("BOT_TOKEN")).login().thenAccept(api -> {

            api.addMessageCreateListener(event -> {

                if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                    Bot.getBeatmap(event, "tux", "889322");
                }
            });
            System.out.println("You can invite the bot by using the following url: "+api.createBotInvite());
        });
    }
}

