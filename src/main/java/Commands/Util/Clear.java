package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;
import java.util.logging.Level;

public class Clear extends ServerCommand {
	public Clear() {
		super("$clear", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		int ammount;
		try {
			ammount = Integer.parseInt(event.getMessage().getContentRaw().split(" ")[1]) + 1;
		} catch (Exception e){
			logger.log(Level.WARNING, "Failed to parse integer from: [" + event.getMessage().getContentRaw() + ']');
			event.getChannel().sendMessage("Failed to parse command, messages to clear must be between 1 and 99").queue();
			return;
		}

		if(ammount > 100 || ammount < 1){
			event.getChannel().sendMessage("Messages to clear must be between 1 and 99");
			return;
		}

		event.getChannel().getHistory().retrievePast(ammount).queue(messages -> {
			if(messages.isEmpty()){
				event.getChannel().sendMessage("No messages to delete").queue();
				return;
			}

			for(Message msg : messages){
				if(!msg.isPinned()) {
					logger.log(Level.FINE, "Removed Message: [" + msg.getId() + "] Content: [" + msg.getContentRaw() + "] From: [" + (msg.getMember() == null ? "unknown" : msg.getMember().getEffectiveName()) + ']');
					event.getChannel().deleteMessageById(msg.getId()).queue();
				}
			}
		});

	}
}
