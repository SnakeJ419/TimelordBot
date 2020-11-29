package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.logging.Level;

public class ClearTo extends ServerCommand {
	public ClearTo() {
		super("$clearto", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		long messageID;
		try {
			messageID = Long.parseLong(event.getMessage().getContentRaw().split(" ")[1]);
		} catch (Exception e){
			logger.log(Level.WARNING, "Failed to parse long from: [" + event.getMessage().getContentRaw() + ']');
			event.getChannel().sendMessage("Failed to parse command, must contain message id").queue();
			return;
		}

		event.getChannel().getHistory().retrievePast(100).queue(messages -> {
			if(messages.isEmpty()){
				event.getChannel().sendMessage("No messages to delete").queue();
				return;
			}
			boolean foundMsg = false;
			System.out.println("message id: " + messageID);
			for(Message msg : messages){
				if(msg.getIdLong() == messageID){
					foundMsg = true;
					break;
				}
			}
			if(!foundMsg){
				event.getChannel().sendMessage("Message ID not found in last 100 messages, aborting clear").queue();
				logger.log(Level.WARNING, "Unable to find message ID: [" + messageID + "] aborting clear");
				return;
			}

			for(Message msg : messages){
				if(!msg.isPinned()) {
					logger.log(Level.FINE, "Removed Message: [" + msg.getId() + "] Content: [" + msg.getContentRaw() + "] From: [" + (msg.getMember() == null ? "unknown" : msg.getMember().getEffectiveName()) + ']');
					event.getChannel().deleteMessageById(msg.getId()).queue();

					if(msg.getIdLong() == messageID){
						break;
					}
				}
			}
		});

	}
}
