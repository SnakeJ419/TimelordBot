package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.logging.Level;

public class ForceReset extends ServerCommand {
	public ForceReset() {
		super("$forcereset", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		event.getGuild().loadMembers(member -> {
			try {
				logger.log(Level.FINE, "Reverted: [" + member.getEffectiveName() + "] to: [" + member.getUser().getName() + ']');
				member.modifyNickname(member.getUser().getName()).queue();
			} catch (Exception e){
				logger.log(Level.WARNING, "Unable to modify admin: [" + member.getEffectiveName() + "], proceeding");
			}
		});

		event.getChannel().sendMessage(event.getMember().getEffectiveName() + " forced a reset of all nicknames").queue();
	}
}
