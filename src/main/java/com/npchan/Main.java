package com.npchan;



import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.javacord.api.DiscordApiBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main (String[] args){

        new DiscordApiBuilder().setToken(System.getenv("BOT_TOKEN")).login().thenAccept(api -> {

            // Add a listener which answers with "Pong!" if someone writes "!ping"
            api.addMessageCreateListener(event -> {
                if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                    try {

                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpGet getRequest = new HttpGet(
                                "https://osu.ppy.sh/api/get_beatmaps?b=889322&k="+ System.getenv("OSU_KEY"));
                        getRequest.addHeader("accept", "application/json");
                        getRequest.setHeader("User-Agent", "MySuperUserAgent");

                        HttpResponse response = httpClient.execute(getRequest);

                        if (response.getStatusLine().getStatusCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + response.getStatusLine().getStatusCode());
                        }

                        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                        String output;
                        while ((output = br.readLine()) != null) {
                            System.out.println(output);
                            event.getChannel().sendMessage(output);
                        }

                        httpClient.getConnectionManager().shutdown();

                    } catch (ClientProtocolException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

            });

            // Print the invite url of your bot
            System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());

        });
    }

}
