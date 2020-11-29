package Commands.Scheduling;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Date;

public class Event {
	private final Date date;
	private final String name;
	private final MessageReceivedEvent parentEvent;
	private final int maxParticipants;

	public Event(Date date, String name, MessageReceivedEvent parentEvent, int maxParticipants) {
		this.date = date;
		this.name = name;
		this.parentEvent = parentEvent;
		this.maxParticipants = maxParticipants;
	}

	@Override
	public String toString() {
		return "Event [" + name + "] at [" + date + "] for up to [" + maxParticipants + "] participants";
	}
}
