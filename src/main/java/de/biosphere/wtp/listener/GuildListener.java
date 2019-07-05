package de.biosphere.wtp.listener;

import de.biosphere.wtp.WheresTheParty;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

public class GuildListener extends ListenerAdapter {

    private final WheresTheParty bot;

    public GuildListener(final WheresTheParty bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        bot.sendToAllSessions(new JSONObject().put("stats", new JSONObject().put("sessions", bot.getSessions().size()).put("servers", event.getJDA().getGuilds().size())).toString());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        bot.sendToAllSessions(new JSONObject().put("stats", new JSONObject().put("sessions", bot.getSessions().size()).put("servers", event.getJDA().getGuilds().size())).toString());
    }
}
