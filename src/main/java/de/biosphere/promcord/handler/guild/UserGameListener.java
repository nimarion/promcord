package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class UserGameListener extends ListenerAdapter {

    private final Gauge game_count;

    public UserGameListener() {
        game_count = Gauge.build().name("game_usage").help("Count of played games").labelNames("guild", "game").register();
    }

    /**@Override public void onUserUpdateActivityOrder(@Nonnull UserUpdateActivityOrderEvent event) {
    if (event.getNewValue() != null && event.getNewGame().isRich() && event.getNewGame().getType().equals(Game.GameType.DEFAULT)) {
            if (event.getOldGame() != null && event.getNewGame().getName().equals(event.getOldGame().getName())) {
                return;
            }
            final long count = event.getGuild().getMembers().stream().filter(member -> (member.getGame() != null && member.getGame().isRich() && member.getGame().getName().equals(event.getNewGame().getName()))).count();
            game_count.labels(event.getGuild().getId(), event.getNewGame().getName()).set(count);
        }
    }**/

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        final HashMap<String, Integer> gameMap = new HashMap<>();
        event.getGuild().getMembers().stream().filter(member -> !member.getOnlineStatus().equals(OnlineStatus.OFFLINE) && !member.getActivities().isEmpty() && member.getActivities().get(0).isRich() && member.getActivities().get(0).getType().equals(Activity.ActivityType.DEFAULT)).forEach(member -> {
            gameMap.compute(member.getActivities().get(0).getName(), (key, val) -> val == null ? 1 : val + 1);
        });
        gameMap.forEach((key, value) -> game_count.labels(event.getGuild().getId(), key).set(value));
    }

}
