package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildBoostListener extends ListenerAdapter {

    private final Gauge booster_count;

    public GuildBoostListener() {
        booster_count = Gauge.build().name("booster_count").help("Count of boosters").labelNames("guild").register();
    }

    @Override
    public void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event) {
        booster_count.labels(event.getGuild().getId()).set(event.getNewBoostCount());
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        booster_count.labels(event.getGuild().getId()).set(event.getGuild().getBoostCount());
    }
}
