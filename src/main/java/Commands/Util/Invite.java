package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Invite extends ServerCommand {

	private final static String INVITE_LINK = "https://discord.com/api/oauth2/authorize?client_id=733407709130391582&permissions=8&scope=bot";


	public Invite() {
		super("$invite");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		event.getChannel().sendMessage(INVITE_LINK).queue();
	}
}
