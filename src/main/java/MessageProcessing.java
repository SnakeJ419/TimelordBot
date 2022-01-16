import Commands.*;
import Commands.Preferences.EnableTimeConversion;
import Commands.Scheduling.*;
import Commands.Seasonal.*;
import Commands.Util.*;
import Commands.dnd.*;
import Commands.meme.*;
import Commands.timezones.*;
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
	public final static ServerCommand[] commands = {new TimeConversion(), new AddTimezone(), new ListTimezones(), new RegisterTimezone(),
			new DNDNick(), new ResetNick(), new SetDNDNick(), new Spookify(), new ForceReset(), new Clean(), new Clear(), new ClearTo(), new DabCommand(), new Mistletoe(),
			new DumpLog(), new Annoy(), new Suppress(), new Festify(), new NewEvent(), new Invite(), new Proof(), new Upgrade(), new DiceRoll(), new Vibe(), new Threadify(), new PrivateText(),
			new EnableTimeConversion(), new IDMe()};


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
