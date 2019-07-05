package de.biosphere.wtp.listener;

import de.biosphere.wtp.WheresTheParty;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

public class MessageListener extends ListenerAdapter {

    private final WheresTheParty bot;

    public MessageListener(final WheresTheParty bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        if(event.getAuthor().getDiscriminator().equals("0000")){
            return;
        }

        bot.sendToAllSessions(new JSONObject()
                .put("user", new JSONObject()
                        .put("id", event.getAuthor().getId())
                        .put("name", event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator())
                        .put("avatar", event.getAuthor().getEffectiveAvatarUrl()))
                .put("channel", new JSONObject()
                        .put("id", "#"+event.getChannel().getId())
                        .put("name", event.getChannel().getName()))
                .put("guild", new JSONObject()
                        .put("id", event.getGuild().getId())
                        .put("name", event.getGuild().getName())
                        .put("icon", event.getGuild().getIconUrl()))
                .toString());
    }

}
