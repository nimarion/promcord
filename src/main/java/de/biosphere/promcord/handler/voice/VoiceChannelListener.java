package de.biosphere.promcord.handler.voice;

import java.util.HashMap;
import java.util.Map;

import io.prometheus.client.Gauge;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannelListener extends ListenerAdapter {


    private final Gauge voice_count;
    private final Map<String, Long> voiceMap;

    public VoiceChannelListener(){
        voice_count = Gauge.build().name("voice_time").help("Time spent in a voice channel").labelNames("guild", "channel", "user").register();
        voiceMap = new HashMap<>();
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        voiceMap.put(event.getMember().getId(), System.currentTimeMillis());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if(voiceMap.containsKey(event.getMember().getId())){
            final Long time = (System.currentTimeMillis() - voiceMap.get(event.getMember().getId())) / 1000;
            voice_count.labels(event.getGuild().getId(), event.getChannelLeft().getId(), event.getMember().getId()).set(time);
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if(voiceMap.containsKey(event.getMember().getId())){
            final Long time = (System.currentTimeMillis() - voiceMap.get(event.getMember().getId())) / 1000;
            voice_count.labels(event.getGuild().getId(), event.getChannelLeft().getId(), event.getMember().getId()).set(time);
            voiceMap.put(event.getMember().getId(), System.currentTimeMillis());
        }
    }
}