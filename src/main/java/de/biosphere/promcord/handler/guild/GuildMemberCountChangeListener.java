package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberCountChangeListener extends ListenerAdapter {

    private final Gauge member_count;
    private final Gauge bot_count;

    public GuildMemberCountChangeListener() {
        member_count = Gauge.build().name("member_count").help("Count of members").labelNames("guild").register();
        bot_count = Gauge.build().name("bot_count").help("Count of bots").labelNames("guild").register();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMemberCount());
        bot_count.labels(event.getGuild().getId()).set(getBotCount(event.getGuild()));
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMemberCount());
        bot_count.labels(event.getGuild().getId()).set(getBotCount(event.getGuild()));
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMemberCount());
        bot_count.labels(event.getGuild().getId()).set(getBotCount(event.getGuild()));
    }

    private Long getBotCount(final Guild guild) {
        return guild.getMembers().stream().filter(member -> member.getUser().isBot()).count();
    }

}
