package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Upgrade extends ServerCommand {
	public Upgrade() {
		super("$upgrade");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("bots", true).get(0)).queue();
	}
}
