package Commands.Seasonal;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Spookify extends ServerCommand {
	private final ArrayList<Timer> spookyTimers = new ArrayList<>();

	private final static String[] spookyEmojis = new String[]{"\uD83D\uDE08", "\uD83D\uDC80", "\uD83D\uDC79", "\uD83D\uDC7B", "\uD83E\uDDDB", "\uD83E\uDD87", "\uD83E\uDD89", "\uD83D\uDD77", "\uD83D\uDD78", "\uD83C\uDF6B", "\uD83C\uDF83", "\uD83D\uDDE1", "\uD83E\uDE78", "⚰", "\uD83E\uDEA6"};
	private final static Random rand = new Random();

	public Spookify() {
		super("$spookify", false, true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		event.getGuild().loadMembers(member -> {
			int emoji = rand.nextInt(spookyEmojis.length);
			try {
				String newNick = spookyEmojis[emoji] + "Spooky " + member.getEffectiveName() + spookyEmojis[emoji];
				logger.log(Level.FINE, "Changed: [" + member.getEffectiveName() + "] to: [" + newNick + ']');
				member.modifyNickname(newNick).queue();

			} catch (Exception e){
				logger.log(Level.WARNING, "Unable to modify admin: [" + member.getEffectiveName() + "], proceeding");
			}
		});

		event.getChannel().sendMessage(event.getMember().getEffectiveName() + " Spooked the Server!").queue();
	}
}
