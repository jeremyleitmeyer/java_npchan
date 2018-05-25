package com.npchan;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONObject;
import org.json.JSONArray;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.javacord.api.entity.message.embed.*;

public class Bot {
    public static void getBeatmap(MessageCreateEvent event, String username, String beatmap) {

        try {
//            build httpclient object
            HttpClient httpClient = HttpClientBuilder.create().build();
//            api url GET for song data
            HttpGet getRequest = new HttpGet(
                    "https://osu.ppy.sh/api/get_beatmaps?b=" + beatmap + "&k=" + System.getenv("OSU_KEY"));
            getRequest.addHeader("accept", "application/json");
//            had to set headers
            getRequest.setHeader("User-Agent", "Anonymous");
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                Bot.buildMessage(event, output, username);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static void buildMessage(MessageCreateEvent event, String output, String username) {
//        convert String to JSONarray
        JSONArray resArr = new JSONArray(output);
//        then to an object
        JSONObject apiRes = resArr.getJSONObject(0);
//        set message variables
        String artist = apiRes.getString("artist");
        String beatmapId = apiRes.getString("beatmap_id");
        String title = apiRes.getString("title");
        String bpm = apiRes.getString("bpm");
        String str_length = apiRes.getString("total_length");
        String beatmapsetId = apiRes.getString("beatmapset_id");

//        str_length comes back in seconds
//        converting to min : sec
        int total_length = Integer.parseInt(str_length);
        if (total_length > 60) {
            int minutes = Math.round(total_length / 60);
            int seconds = total_length - minutes * 60;
            String zero = "0";
            if (seconds < 10) {
//                convert the time back to string for the message
                str_length = Integer.toString(minutes) + ':' + zero + Integer.toString(seconds);
            } else {
                str_length = Integer.toString(minutes) + ':' + Integer.toString(seconds);
            }
        }

        event.getChannel().sendMessage("**" + username + "**" + " is listening to:");
//        build embed message
        EmbedBuilder embed = new EmbedBuilder().setTitle(title + " - " + artist)
                .setDescription("BPM: " + bpm + "\nLength: " + str_length + "\nBeatmap: [View](" + "https://osu.ppy.sh/b/" + beatmapId + ")")
                .setThumbnail("https://b.ppy.sh/thumb/" + beatmapsetId + "l.jpg")
                .setFooter("© Riker, Flo, & Tux")
                .setColor(Color.pink);
        event.getChannel().sendMessage(embed);
    }
}