package de.biosphere.promcord.handler.message;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageRecieverListener extends ListenerAdapter {

    private final String[] tags = {"guild", "channel", "user"};
    private final Pattern emote_pattern = Pattern.compile("<[a]?:[a-zA-Z0-9]+:[0-9]+>");
    private final Counter msg_count;
    private final Counter msg_emote_count;
    private final Gauge msg_length;
    private final Gauge msg_word_count;

    public MessageRecieverListener() {
        msg_count = Counter.build().name("msg_count").help("Count of messages").labelNames(tags).register();
        msg_emote_count = Counter.build().name("msg_emote_count").help("Count of emotes in messages").labelNames("guild", "channel", "user", "emote").register();
        msg_length = Gauge.build().name("msg_length").help("Length of messages").labelNames(tags).register();
        msg_word_count = Gauge.build().name("msg_word_count").help("Count of words in messages").labelNames(tags).register();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getChannelType().isGuild()) {
            return;
        }
        if (event.getAuthor().isBot()) {
            return;
        }

        final TextChannel channel = event.getTextChannel();
        final User user = event.getAuthor();

        recordMessageCount(channel, user);
        recordMessageLength(channel, user, event.getMessage().getContentDisplay().length());
        recordMessageWordCount(channel, user, event.getMessage().getContentDisplay().split(" ").length);

        final Matcher matcher = emote_pattern.matcher(event.getMessage().getContentRaw());
        while (matcher.find()) {
            final String emote = matcher.group();
            msg_emote_count.labels(channel.getGuild().getId(), channel.getId(), user.getId(), emote.split(":")[1]).inc();
        }
    }

    private void recordMessageWordCount(final TextChannel channel, final User user, final int length) {
        msg_word_count.labels(channel.getGuild().getId(), channel.getId(), user.getId()).set(length);
    }

    private void recordMessageLength(final TextChannel channel, final User user, final int length) {
        msg_length.labels(channel.getGuild().getId(), channel.getId(), user.getId()).set(length);
    }

    private void recordMessageCount(final TextChannel channel, final User user) {
        msg_count.labels(channel.getGuild().getId(), channel.getId(), user.getId()).inc();
    }
}
