package com.npchan;

import com.mongodb.*;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.*;

import java.net.UnknownHostException;

public class Main {

    public static void main (String[] args) {

            new DiscordApiBuilder().setToken(System.getenv("BOT_TOKEN")).login().thenAccept(api -> {

                api.addMessageCreateListener(event -> {

                    try{
                        if(event.getMessage().getContent().substring(0,1)
                                .equalsIgnoreCase(">")){
                            String msgArgs = event.getMessage().getContent()
                                    .substring(1,10);
                            String osuUsername = event.getMessage().getContent()
                                    .substring(11);

                            switch(msgArgs){
                                case "npchanSet":
                                    String user = event.getMessage().getAuthor()
                                            .getIdAsString();
                                    Bot.osuUserApiCall(event, user, osuUsername);
                                    break;
                            }
                        }
                    } catch (Throwable e){
                        e.printStackTrace();
                    }

//                    this will be an acceptance of data not a ! command
                    if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
//                    this will be dynamic
                        Bot.getBeatmap(event, "tux", "889322");
                    }

                    if (event.getMessage().getContent().equalsIgnoreCase
                            ("!osu")) {
                        String userId = event.getMessage().getAuthor()
                                .getIdAsString();
                        Bot.getUser(event, userId);
                    }


//                    this will be dynamic
//                        Bot.setUser(user, "889322");

                });
                System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
            });

    }
}

