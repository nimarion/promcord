package de.biosphere.promcord.core;

import de.biosphere.promcord.StatisticsHandlerCollector;
import de.biosphere.promcord.handler.guild.GuildMemberCountChangeListener;
import de.biosphere.promcord.handler.guild.UserOnlineStatusListener;
import de.biosphere.promcord.handler.message.MessageRecieverListener;
import io.prometheus.client.exporter.HTTPServer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PromcordImpl implements Promcord {

    private static final Logger logger = LoggerFactory.getLogger(Promcord.class);

    private final JDA jda;

    public PromcordImpl() throws Exception {
        final long startTime = System.currentTimeMillis();
        logger.info("Starting promcord");

        jda = initializeJDA();
        logger.info("JDA set up!");

        final HTTPServer prometheusServer = new HTTPServer(8080);

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

    protected JDA initializeJDA() throws Exception {
        try {
            final JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
            jdaBuilder.setToken(System.getenv("DISCORD_TOKEN"));
            jdaBuilder.addEventListener(
                    new MessageRecieverListener(),
                    new GuildMemberCountChangeListener(),
                    new UserOnlineStatusListener());
            return jdaBuilder.build().awaitReady();
        } catch (Exception exception) {
            logger.error("Encountered exception while initializing ShardManager!");
            throw exception;
        }
    }

    @Override
    public JDA getJDA() {
        return jda;
    }
}
