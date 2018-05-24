package com.npchan;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot {
    protected static void getBeatmap(MessageCreateEvent event, String username, String beatmap) {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    "https://osu.ppy.sh/api/get_beatmaps?b=" + beatmap + "&k=" + System.getenv("OSU_KEY"));
            getRequest.addHeader("accept", "application/json");
            getRequest.setHeader("User-Agent", "Anonymous");
            System.out.println(getRequest);
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                System.out.println(username);
                Bot.buildMessage(event, output);
                event.getChannel().sendMessage(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }
    public static void buildMessage(MessageCreateEvent event, String output) {
        System.out.println(output);
//        EmbedBuilder embed  = new EmbedBuilder().setTitle(output.beatmap_id);
    }
}