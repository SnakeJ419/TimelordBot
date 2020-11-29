import Commands.*;
import Commands.Scheduling.NewEvent;
import Commands.Seasonal.Festify;
import Commands.Seasonal.Mistletoe;
import Commands.Seasonal.Spookify;
import Commands.Util.*;
import Commands.dnd.DNDNick;
import Commands.dnd.ResetNick;
import Commands.dnd.SetDNDNick;
import Commands.timezones.AddTimezone;
import Commands.timezones.ListTimezones;
import Commands.timezones.RegisterTimezone;
import Commands.timezones.TimeConversion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class MessageProcessing extends ListenerAdapter {
	private final static String lfgRoleID = "lfg";
	private final static String lfgChannelID = "lfg";
	public final static long botID = 733407709130391582L;
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private final ServerCommand[] commands = new ServerCommand[]{new TimeConversion(), new AddTimezone(), new ListTimezones(), new RegisterTimezone(),
			new DNDNick(), new ResetNick(), new SetDNDNick(), new Spookify(), new ForceReset(), new LoadMembers(), new Clean(), new Clear(), new ClearTo(), new DabCommand(), new Mistletoe(),
			new DumpLog(), new iSearch(), new Annoy(), new Suppress(), new Festify(), new NewEvent()};


	@Override
	public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
		if(event.getChannelJoined().getName().toLowerCase().equals("lfg-join")){
			if(!event.getMember().getRoles().contains(event.getJDA().getRolesByName(lfgRoleID, true).get(0))) {
				event.getGuild().addRoleToMember(event.getMember().getId(), event.getJDA().getRolesByName(lfgRoleID, true).get(0)).queue();
			}
			event.getGuild().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelsByName(lfgChannelID, true).get(0)).queue();
		}
	}


	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if(!event.getMember().getId().equals(Long.toString(botID))) {
			for (ServerCommand command : commands) {
				if (command.check(event)) {
					command.runCommand(event);
				}
			}
		}
	}
}
