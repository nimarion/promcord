package de.biosphere.promcord;

import de.biosphere.promcord.handler.guild.GuildBoostListener;
import de.biosphere.promcord.handler.guild.GuildMemberCountChangeListener;
import de.biosphere.promcord.handler.guild.UserGameListener;
import de.biosphere.promcord.handler.guild.UserOnlineStatusListener;
import de.biosphere.promcord.handler.message.MessageReactionListener;
import de.biosphere.promcord.handler.message.MessageRecieverListener;
import de.biosphere.promcord.handler.voice.VoiceChannelListener;
import io.prometheus.client.exporter.HTTPServer;
import io.sentry.Sentry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Promcord {

    private static final Logger logger = LoggerFactory.getLogger(Promcord.class);

    private final JDA jda;

    public Promcord() throws Exception {
        final long startTime = System.currentTimeMillis();
        logger.info("Starting promcord");

        jda = initializeJDA();
        logger.info("JDA set up!");

        final HTTPServer prometheusServer = new HTTPServer(
                Configuration.HTTP_PORT == null ? 8080 : Integer.parseInt(Configuration.HTTP_PORT));

        new StatisticsHandlerCollector(this).register();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                prometheusServer.stop();
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
            jda.shutdown();
        }));
        logger.info(String.format("Startup finished in %dms!", System.currentTimeMillis() - startTime));
    }

    private JDA initializeJDA() throws Exception {
        try {
            final JDABuilder jdaBuilder = JDABuilder.createDefault(Configuration.DISCORD_TOKEN);
            jdaBuilder.setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
            jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jdaBuilder.addEventListeners(new MessageRecieverListener(), new GuildMemberCountChangeListener(),
                    new UserOnlineStatusListener(), new UserGameListener(), new MessageReactionListener(),
                    new GuildBoostListener(), new VoiceChannelListener());
            return jdaBuilder.build().awaitReady();
        } catch (Exception exception) {
            logger.error("Encountered exception while initializing JDA!");
            throw exception;
        }
    }

    public JDA getJDA() {
        return jda;
    }

    public static void main(String... args) {
        if (System.getenv("SENTRY_DSN") != null || System.getProperty("sentry.properties") != null) {
            Sentry.init();
        }
        try {
            new Promcord();
        } catch (Exception exception) {
            logger.error("Encountered exception while initializing the bot!", exception);
        }
    }
}
