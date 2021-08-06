package de.biosphere.promcord.handler.message;

import com.vdurmont.emoji.EmojiManager;
import io.prometheus.client.Counter;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionListener extends ListenerAdapter {

    private final Counter reaction_count;

    public MessageReactionListener() {
        reaction_count = Counter.build().name("reaction_count").help("Count of reactions")
                .labelNames("guild", "channel", "channelName", "user", "name", "emote").register();
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()
                && (System.getenv("IGNORE_BOTS") == null || System.getenv("IGNORE_BOTS").equalsIgnoreCase("true"))) {
            return;
        }
        final String emote = getReaction(event.getReactionEmote());
        if (emote != null) {
            reaction_count.labels(event.getGuild().getId(), event.getChannel().getId(), event.getChannel().getName(), event.getUser().getId(), event.getUser().getName(), emote)
                    .inc();
        }
    }

    private String getReaction(final MessageReaction.ReactionEmote emote) {
        try {
            return EmojiManager.getByUnicode(emote.getName()).getAliases().get(0);
        } catch (Exception e) {
            return emote.getName();
        }
    }

}
