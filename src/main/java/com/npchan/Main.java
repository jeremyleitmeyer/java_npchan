package com.npchan;

import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main (String[] args) {

            new DiscordApiBuilder().setToken(System.getenv("BOT_TOKEN")).login().thenAccept(api -> {

                api.addMessageCreateListener(event -> {

                    try{
//                      this is causing error: "String index out of range:
//                      1" will fix
                        if(event.getMessage().getContent().substring(0,1)
                                .equalsIgnoreCase(">")){
                            String msgArgs = event.getMessage().getContent()
                                    .substring(1,10);
//                            grab word two
                            String osuUsername = event.getMessage().getContent()
                                    .substring(11);

                            switch(msgArgs){
                                case "npchanSet":
                                    String user = event.getMessage().getAuthor().getIdAsString();
                                    Bot.setUser(event, user, Bot.osuApiCall(osuUsername, "https://osu.ppy.sh/api/get_user?u="));
                                    break;
                            }
                        }
                    } catch (Throwable e){
                        e.printStackTrace();
                    }

//                    this will be an acceptance of data not a ! command
                    if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
//                    this will be dynamic
                        Bot.buildMessage(event, Bot.osuApiCall("889322", "https://osu.ppy.sh/api/get_beatmaps?b="), "tux");
                    }

                    if (event.getMessage().getContent().equalsIgnoreCase
                            ("!osu")) {
                        String userId = event.getMessage().getAuthor()
                                .getIdAsString();
                        Bot.getUser(event, userId);
                    }

                });
                System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
            });

    }
}

