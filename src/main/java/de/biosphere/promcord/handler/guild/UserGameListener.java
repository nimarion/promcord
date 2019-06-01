package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;

public class UserGameListener extends ListenerAdapter {

    private final Gauge game_count;

    public UserGameListener() {
        game_count = Gauge.build().name("game_usage").help("Count of played games").labelNames("guild", "game").register();
    }

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent event) {
        if (event.getNewGame() != null && event.getNewGame().isRich() && event.getNewGame().getType().equals(Game.GameType.DEFAULT)) {
            if (event.getOldGame() != null && event.getNewGame().getName().equals(event.getOldGame().getName())) {
                return;
            }
            final long count = event.getGuild().getMembers().stream().filter(member -> (member.getGame() != null && member.getGame().isRich() && member.getGame().getName().equals(event.getNewGame().getName()))).count();
            game_count.labels(event.getGuild().getId(), event.getNewGame().getName()).set(count);
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        final HashMap<String, Integer> gameMap = new HashMap<>();
        event.getGuild().getMembers().stream().filter(member -> !member.getOnlineStatus().equals(OnlineStatus.OFFLINE) && member.getGame() != null && member.getGame().isRich() && member.getGame().getType().equals(Game.GameType.DEFAULT)).forEach(member -> {
            gameMap.compute(member.getGame().getName(), (key, val) -> val == null ? 1 : val + 1);
        });
        gameMap.forEach((key, value) -> game_count.labels(event.getGuild().getId(), key).set(value));
    }
}
