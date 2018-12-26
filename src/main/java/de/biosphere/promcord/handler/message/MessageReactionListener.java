package de.biosphere.promcord.handler.message;

import com.vdurmont.emoji.EmojiManager;
import io.prometheus.client.Counter;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageReactionListener extends ListenerAdapter {

    private final Counter reaction_count;

    public MessageReactionListener(){
        reaction_count = Counter.build().name("reaction_count").help("Count of reactions").labelNames("guild", "channel", "user", "emote").register();
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()){
            return;
        }
        final String emote = getReaction(event.getReactionEmote());
        if(emote != null){
            reaction_count.labels(event.getGuild().getId(), event.getChannel().getId(), event.getUser().getId(), emote).inc();
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
