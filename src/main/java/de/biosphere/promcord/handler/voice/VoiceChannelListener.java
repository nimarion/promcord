package de.biosphere.promcord.handler.voice;

import java.util.HashMap;
import java.util.Map;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannelListener extends ListenerAdapter {

    private final Gauge voice_count;
    private final Gauge stream_count;
    private final Map<String, Long> voiceMap;
    private final Map<String, Long> streamMap;

    public VoiceChannelListener() {
        voice_count = Gauge.build().name("voice_time").help("Time spent in a voice channel")
                .labelNames("guild", "channel", "user").register();
        stream_count = Gauge.build().name("stream_time").help("Time spent streaming").labelNames("guild", "user")
                .register();
        voiceMap = new HashMap<>();
        streamMap = new HashMap<>();
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        voiceMap.put(event.getMember().getId(), System.currentTimeMillis());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (voiceMap.containsKey(event.getMember().getId())) {
            final Long time = (System.currentTimeMillis() - voiceMap.get(event.getMember().getId())) / 1000;
            voice_count.labels(event.getGuild().getId(), event.getChannelLeft().getId(), event.getMember().getId())
                    .set(time);
            voiceMap.remove(event.getMember().getId());
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (voiceMap.containsKey(event.getMember().getId())) {
            final Long time = (System.currentTimeMillis() - voiceMap.get(event.getMember().getId())) / 1000;
            voice_count.labels(event.getGuild().getId(), event.getChannelLeft().getId(), event.getMember().getId())
                    .set(time);
            voiceMap.put(event.getMember().getId(), System.currentTimeMillis());
        }
    }

    @Override
    public void onGuildVoiceStream(GuildVoiceStreamEvent event) {
        if (streamMap.containsKey(event.getMember().getId()) && !event.isStream()) {
            final Long time = (System.currentTimeMillis() - streamMap.get(event.getMember().getId())) / 1000;
            stream_count.labels(event.getGuild().getId(), event.getMember().getId()).set(time);
            streamMap.remove(event.getMember().getId());
        } else if (event.isStream()) {
            streamMap.put(event.getMember().getId(), System.currentTimeMillis());
        }
    }
}