package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class IDMe extends ServerCommand {
	public IDMe() {
		super("$idme");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		event.getChannel().sendMessage(Long.toString(event.getAuthor().getIdLong())).queue();
	}
}
