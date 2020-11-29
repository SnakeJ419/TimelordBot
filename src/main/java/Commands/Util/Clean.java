package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;

public class Clean extends ServerCommand {
	public Clean() {
		super("$clean", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		MessageHistory channelHistory = event.getChannel().getHistory();
		List<Message> messages = new ArrayList<>();

		channelHistory.retrievePast(100).queue(historyMessages -> {
			if(historyMessages.isEmpty()){
				event.getChannel().sendMessage("No messages to delete").queue();
				return;
			}

			boolean botCommandMsg = false;
			for(Message msg : historyMessages){
				if(msg.getAuthor().getId().equals(Long.toString(733407709130391582L))){
					logger.log(Level.FINE, "Removed Message: [" + msg.getId() + "] Content: [" + msg.getContentRaw() + "] From: [" + msg.getAuthor().getName() + ']');
					event.getChannel().deleteMessageById(msg.getId()).queue();
					botCommandMsg = true;
				} else if(botCommandMsg && msg.getContentRaw().charAt(0) == '$'){
					logger.log(Level.FINE, "Removed Message: [" + msg.getId() + "] Content: [" + msg.getContentRaw() + "] From: [" + msg.getAuthor().getName() + ']');
					event.getChannel().deleteMessageById(msg.getId()).queue();
					botCommandMsg = false;
				}
			}
		});
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
	}
}
