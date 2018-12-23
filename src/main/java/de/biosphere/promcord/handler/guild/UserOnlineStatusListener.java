package de.biosphere.promcord.handler.guild;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserOnlineStatusListener extends ListenerAdapter {

    private final Gauge member_online;

    public UserOnlineStatusListener() {
        member_online = Gauge.build().name("member_online").help("Count of online members").labelNames("guild").register();
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        member_online.labels(event.getGuild().getId()).set(event.getGuild().getMembers().stream().filter(member -> member.getOnlineStatus() != OnlineStatus.OFFLINE).count());
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        member_online.labels(event.getGuild().getId()).set(event.getGuild().getMembers().stream().filter(member -> member.getOnlineStatus() != OnlineStatus.OFFLINE).count());
    }
}
