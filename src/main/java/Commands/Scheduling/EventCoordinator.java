package Commands.Scheduling;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventCoordinator extends ListenerAdapter {
	private final static ArrayList<Event> events = new ArrayList<>();
	public static final String yesEmoji = "\uD83D\uDC4D";
	public static final String maybeEmoji = "\uD83E\uDDEA";

	public final static long botID = 733407709130391582L;

	protected static void addEvent(Event event){
		events.add(event);
	}

	protected static void removeEvent(Event event){
		events.remove(event);
	}

	@Override
	public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent reactionEvent) {
		if(reactionEvent.getMember().getIdLong() != botID) {
			for (Event event : events) {
				if (event.getCriticalMessage().getIdLong() == reactionEvent.getMessageIdLong() && (reactionEvent.getReactionEmote().getEmoji().equals(yesEmoji) || reactionEvent.getReactionEmote().getEmoji().equals(maybeEmoji))){
					event.handleReactionAdd(reactionEvent);
					break;
				}
			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent reactionEvent) {
		for (Event event : events) {
			if (event.getCriticalMessage().getIdLong() == reactionEvent.getMessageIdLong() && (reactionEvent.getReactionEmote().getEmoji().equals(yesEmoji) || reactionEvent.getReactionEmote().getEmoji().equals(maybeEmoji))){
				event.handleReactionRemove(reactionEvent);
				break;
			}
		}
	}
}
