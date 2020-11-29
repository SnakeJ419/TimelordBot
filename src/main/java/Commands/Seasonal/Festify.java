package Commands.Seasonal;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.logging.Level;

public class Festify extends ServerCommand {
	private final ArrayList<Timer> spookyTimers = new ArrayList<>();

	private final static String[] festiveEmojis = new String[]{"\uD83C\uDF85", "\uD83E\uDD8C", "\uD83C\uDF1F", "☃", "\uD83C\uDF84", "\uD83C\uDF81", "\uD83D\uDD14", "\uD83D\uDD6F"};
	private final static Random rand = new Random();

	public Festify() {
		super("$festify", false, false);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		event.getGuild().loadMembers(member -> {
			int emoji = rand.nextInt(festiveEmojis.length);
			try {
				String newNick = festiveEmojis[emoji] + "Festive " + member.getEffectiveName() + festiveEmojis[emoji];
				logger.log(Level.FINE, "Changed: [" + member.getEffectiveName() + "] to: [" + newNick + ']');
				member.modifyNickname(newNick).queue();

			} catch (Exception e){
				logger.log(Level.WARNING, "Unable to modify admin: [" + member.getEffectiveName() + "], proceeding");
			}
		});

		event.getChannel().sendMessage(event.getMember().getEffectiveName() + " made the server festive!").queue();
	}
}
