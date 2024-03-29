package de.biosphere.wtp;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.biosphere.wtp.listener.GuildListener;
import de.biosphere.wtp.listener.MessageListener;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WheresTheParty {

    private static final Logger logger = LoggerFactory.getLogger(WheresTheParty.class);

    private final JDA jda;
    private final Javalin javalin;
    private final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public WheresTheParty() throws Exception{
        final long startTime = System.currentTimeMillis();
        logger.info("Starting WheresTheParty");

        jda = initializeJDA();
        logger.info("JDA set up!");

        javalin = Javalin.create().start(8080);

        javalin.ws("/socket", wsHandler -> {
            wsHandler.onConnect(wsConnectContext -> {
                sessions.add(wsConnectContext.session);
                sendToAllSessions(new JSONObject().put("stats", new JSONObject().put("sessions", sessions.size()).put("servers", jda.getGuilds().size())).toString());
            });
            wsHandler.onClose(wsCloseContext -> {
                sessions.remove(wsCloseContext.session);
                sendToAllSessions(new JSONObject().put("stats", new JSONObject().put("sessions", sessions.size()).put("servers", jda.getGuilds().size())).toString());
            });
        });
        javalin.config.addStaticFiles("src/main/resources/static", Location.EXTERNAL);
        javalin.config.addSinglePageRoot("/", "src/main/resources/static/index.html", Location.EXTERNAL);

        logger.info(String.format("Startup finished in %dms!", System.currentTimeMillis() - startTime));
    }

    protected JDA initializeJDA() throws Exception {
        try {
            final JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
            jdaBuilder.setToken(System.getenv("DISCORD_TOKEN"));
            jdaBuilder.addEventListeners(new MessageListener(this), new GuildListener(this));
            jdaBuilder.setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY));
            if(System.getenv("DISCORD_GAME") != null){
                jdaBuilder.setActivity(Activity.playing(System.getenv("DISCORD_GAME")));
            }
            return jdaBuilder.build().awaitReady();
        } catch (Exception exception) {
            logger.error("Encountered exception while initializing ShardManager!");
            throw exception;
        }
    }

    public void sendToAllSessions(final String message) {
        sessions.forEach(s -> {
            try {
                s.getRemote().sendString(message);
            } catch(IOException exception) {
                logger.error(exception.toString(), exception);
            }
        });
    }

    public Queue<Session> getSessions() {
        return sessions;
    }
}
