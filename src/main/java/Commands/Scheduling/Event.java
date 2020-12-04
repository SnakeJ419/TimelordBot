package Commands.Scheduling;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Event {
	public final static int MINS_TO_MILLIS = 60000;

	private Date date;
	private String name;
	private MessageReceivedEvent parentEvent;
	private int maxParticipants;
	private Message criticalMessage;
	private final ArrayList<Member> participants = new ArrayList<>();
	private final ArrayList<Member> maybes = new ArrayList<>();

	private Timer eventIn15;
	private Timer eventHappening;

	private Timer maybe1hr;
	private Timer maybe30min;

	public Event(Date date, String name, MessageReceivedEvent parentEvent, int maxParticipants) {
		this.date = date;
		this.name = name;
		this.parentEvent = parentEvent;
		this.maxParticipants = maxParticipants;

		parentEvent.getChannel().sendMessage(buildEmbed()).queue(message -> {
			message.addReaction(EventCoordinator.yesEmoji).queue();
			message.addReaction(EventCoordinator.maybeEmoji).queue();
			this.criticalMessage = message;
		});

		scheduleTimers();
	}

	public void handleReactionAdd(GuildMessageReactionAddEvent reactionEvent){
		if(reactionEvent.getReactionEmote().getEmoji().equals(EventCoordinator.yesEmoji)) {
			if (participants.size() < maxParticipants) {
				participants.add(reactionEvent.getMember());
				maybes.remove(reactionEvent.getMember());
				criticalMessage.editMessage(buildEmbed()).queue();
			} else {
				parentEvent.getChannel().sendMessage("Sorry <@" + reactionEvent.getMember().getId() + "> but " + name + " is already full!").queue();
			}
		} else if(reactionEvent.getReactionEmote().getEmoji().equals(EventCoordinator.maybeEmoji)){
			if(!participants.contains(reactionEvent.getMember())) {
				maybes.add(reactionEvent.getMember());
				criticalMessage.editMessage(buildEmbed()).queue();
			} else {
				reactionEvent.getChannel().sendMessage("Sorry <@" + reactionEvent.getMember().getId() +"> But you're currently committed to this event!").queue();
			}
		}
	}

	public void handleReactionRemove(GuildMessageReactionRemoveEvent reactionEvent) {
		reactionEvent.getGuild().retrieveMemberById(reactionEvent.getUserId()).queue(member -> {
			if(reactionEvent.getReactionEmote().getEmoji().equals(EventCoordinator.yesEmoji)) {
				participants.remove(member);
			} else {
				maybes.remove(member);
			}
			criticalMessage.editMessage(buildEmbed()).queue();
		});
	}

	private MessageEmbed buildEmbed(){
		EmbedBuilder messageEmbed = new EmbedBuilder();
		messageEmbed.setTitle(name);
		messageEmbed.setColor(Color.cyan);

		messageEmbed.addField("Max Participants:", Integer.toString(maxParticipants), true);
		messageEmbed.addBlankField(true);
		messageEmbed.addField("Available Slots:", Integer.toString(maxParticipants - participants.size()), true);

		String participantString = "";
		for(Member member : participants){
			participantString += member.getEffectiveName() + "\n";
		}
		messageEmbed.addField("Committed:", participantString, false);

		String maybeString = "";
		for(Member member : maybes){
			maybeString += member.getEffectiveName() + "\n";
		}
		messageEmbed.addField("Maybes:", maybeString, false);

		messageEmbed.addField("Time/Date", date.toString().substring(0, 10) + " at " + extractTime(date.toString()), false);

		messageEmbed.setFooter("React with " + EventCoordinator.yesEmoji + " for yes and " + EventCoordinator.maybeEmoji + " for maybe");

		return messageEmbed.build();
	}

	private String extractTime(String dateString){
		char amPM = 'a';
		int hours = Integer.parseInt(dateString.substring(11, 13));
		String mins = dateString.substring(14, 16);
		if(hours > 12){
			amPM = 'p';
			hours -= 12;
		}
		return hours + ":" + mins + amPM + 'm';
	}

	private void scheduleTimers(){
		long timeToEvent = date.getTime() - System.currentTimeMillis();
		eventIn15 = new Timer();
		eventHappening = new Timer();
		maybe30min = new Timer();
		maybe1hr  = new Timer();

		if(timeToEvent > 60*MINS_TO_MILLIS) {
			maybe1hr.schedule(new TimerTask() {
				@Override
				public void run() {
					criticalMessage.getChannel().sendMessage(generateMaybeString(60)).queue();
				}
			}, timeToEvent - 60 * MINS_TO_MILLIS);
		}

		if(timeToEvent > 30*MINS_TO_MILLIS) {
			maybe30min.schedule(new TimerTask() {
				@Override
				public void run() {
					criticalMessage.getChannel().sendMessage(generateMaybeString(30)).queue();
				}
			}, timeToEvent - 30 * MINS_TO_MILLIS);
		}

		if(timeToEvent > 15*MINS_TO_MILLIS) {
			eventIn15.schedule(new TimerTask() {
				@Override
				public void run() {
					String participantString = "";
					for (Member member : participants) {
						participantString += "<@" + member.getId() + ">";
					}
					criticalMessage.getChannel().sendMessage("15 minutes until " + name
							+ "\n" + participantString).queue();
					criticalMessage.getChannel().sendMessage(generateMaybeString(15)).queue();
				}
			}, timeToEvent - 15 * MINS_TO_MILLIS);
		}

		if(timeToEvent > 0) {
			eventHappening.schedule(new TimerTask() {
				@Override
				public void run() {
					String participantString = "";
					for (Member member : participants) {
						participantString += "<@" + member.getId() + ">";
					}
					criticalMessage.getChannel().sendMessage(name + " is happening now!"
							+ "\n" + participantString).queue();
					criticalMessage.getChannel().sendMessage(generateMaybeString(0)).queue();
				}
			}, timeToEvent);
		}
	}

	private String generateMaybeString(int mins){
		if(maxParticipants - participants.size() > 0){
			String maybeMembers = "";
			String messageString;
			for(Member member : maybes){
				maybeMembers += "<@" + member.getId() + ">";
			}
			messageString = mins + " minutes until " + name
					+ "\nThere are " + (maxParticipants - participants.size()) + " slots left for "
					+ "\n" + maybeMembers;
			return messageString;
		} else {
			return "";
		}
	}

	@Override
	public String toString() {
		return "Event [" + name + "] at [" + date + "] for up to [" + maxParticipants + "] participants";
	}

	public Date getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public MessageReceivedEvent getParentEvent() {
		return parentEvent;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public Message getCriticalMessage() {
		return criticalMessage;
	}
}
