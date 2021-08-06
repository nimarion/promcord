package de.biosphere.promcord.handler.message;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class MessageRecieverListener extends ListenerAdapter {

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final Counter msg_count;
    private final Counter msg_emote_count;
    private final Gauge msg_length;
    private final Gauge msg_word_count;
    private final Gauge toxicityScore;
    private final String perspectivePayload;

    public MessageRecieverListener() {
        String[] tags = { "guild", "channel", "channelName", "user", "name" };
        msg_count = Counter.build().name("msg_count").help("Count of messages").labelNames(tags).register();
        msg_emote_count = Counter.build().name("msg_emote_count").help("Count of emotes in messages")
                .labelNames("guild", "channel", "channelName", "user", "name", "emote").register();
        msg_length = Gauge.build().name("msg_length").help("Length of messages").labelNames(tags).register();
        msg_word_count = Gauge.build().name("msg_word_count").help("Count of words in messages").labelNames(tags)
                .register();
        toxicityScore = Gauge.build().name("toxicity_score").help("ToxicityScore of a message").labelNames(tags)
                .register();
        perspectivePayload = "{\n" + "  \"comment\": {\n" + "    \"text\": \"message\"\n" + "  },\n"
                + "  \"languages\": [\n" + "    \"de\"\n" + "  ],\n" + "  \"requestedAttributes\": {\n"
                + "    \"TOXICITY\": {}\n" + "  }\n" + "}";
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                && (System.getenv("IGNORE_BOTS") == null || System.getenv("IGNORE_BOTS").equalsIgnoreCase("true"))) {
            return;
        }

        final TextChannel channel = event.getChannel();
        final User user = event.getAuthor();

        recordMessageCount(channel, user);
        recordMessageLength(channel, user, event.getMessage().getContentDisplay().length());
        recordMessageWordCount(channel, user, event.getMessage().getContentDisplay().split(" ").length);

        event.getMessage().getEmotesBag().forEach(emote -> {
            msg_emote_count.labels(channel.getGuild().getId(), channel.getId(), channel.getName(), user.getId(), user.getName(), emote.getName()).inc();
        });

        if (System.getenv("PERSPECTIVE_KEY") != null) {
            recordToxicityScore(event.getMessage());
        }
    }

    private void recordMessageWordCount(final TextChannel channel, final User user, final int length) {
        msg_word_count.labels(channel.getGuild().getId(), channel.getId(), channel.getName(),user.getId(), user.getName()).set(length);
    }

    private void recordMessageLength(final TextChannel channel, final User user, final int length) {
        msg_length.labels(channel.getGuild().getId(), channel.getId(), channel.getName(), user.getId(), user.getName()).set(length);
    }

    private void recordMessageCount(final TextChannel channel, final User user) {
        msg_count.labels(channel.getGuild().getId(), channel.getId(), channel.getName(), user.getId(), user.getName()).inc();
    }

    private void recordToxicityScore(final Message message) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                perspectivePayload.replace("message", message.getContentDisplay()));
        final Request request = new Request.Builder()
                .url("https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key="
                        + System.getenv("PERSPECTIVE_KEY"))
                .post(requestBody).build();
        try {
            final Response response = okHttpClient.newCall(request).execute();
            final JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            final double value = jsonObject.get("attributeScores").getAsJsonObject().get("TOXICITY").getAsJsonObject()
                    .get("summaryScore").getAsJsonObject().get("value").getAsDouble();
            toxicityScore.labels(message.getGuild().getId(), message.getChannel().getId(), message.getAuthor().getId())
                    .set(value);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
