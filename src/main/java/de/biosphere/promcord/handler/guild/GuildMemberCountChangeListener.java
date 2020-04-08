package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberCountChangeListener extends ListenerAdapter {

    private final Gauge member_count;

    public GuildMemberCountChangeListener() {
        member_count = Gauge.build().name("member_count").help("Count of members").labelNames("guild").register();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMembers().size());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMembers().size());
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        member_count.labels(event.getGuild().getId()).set(event.getGuild().getMembers().size());
    }

}
