package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.util.List;
import java.util.logging.Level;

public class LoadMembers extends ServerCommand {
	public LoadMembers() {
		super("$loadmembers", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');
		Task<List<Member>> memberTask = event.getGuild().loadMembers();
	}
}
