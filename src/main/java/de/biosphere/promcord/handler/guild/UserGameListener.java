package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserGameListener extends ListenerAdapter {

    private final Gauge game_count;

    public UserGameListener() {
        game_count = Gauge.build().name("game_usage").help("Count of online members").labelNames("guild", "game").register();
    }

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent event) {
        if(event.getNewGame() != null && event.getNewGame().isRich()){
            final long count = event.getGuild().getMembers().stream().filter(member -> (member.getGame() != null && member.getGame().isRich() && member.getGame().getName().equals(event.getNewGame().getName()))).count();
            game_count.labels(event.getGuild().getId(), event.getNewGame().getName()).set(count);
        }
    }
}
