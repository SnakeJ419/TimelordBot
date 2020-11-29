package Commands.Scheduling;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class NewEvent extends ServerCommand {
	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
	private final static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:a", Locale.ENGLISH);

	public NewEvent() {
		super("$newevent", false, true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String message = event.getMessage().getContentRaw().toLowerCase();
		String[] messagePieces = message.split(" ");

		if(messagePieces.length != 4 && messagePieces.length != 5){
			formatError(event);
			return;
		}

		Date time = parseTime(messagePieces, event);
		Date date = parseDate(messagePieces);
		if(time == null || date == null){
			formatError(event);
			return;
		}
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		date.setSeconds(time.getSeconds());

		String name = messagePieces[1];
		int participants = Integer.parseInt(messagePieces[2]);

		logger.log(Level.INFO, "New event: " + name + " at: " + date.toString() + " for: " + participants);
	}

	private Date parseTime(String[] messagePieces, MessageReceivedEvent event){
		String timeString = messagePieces[3];
		char[] timeChar = timeString.toCharArray();
		char amPM = timeChar[timeChar.length-2];
		String[] timeAry = timeString.split(":");
		int timeNumb = Integer.parseInt(timeAry[0]);
		int mins = Integer.parseInt(timeAry[1].substring(0, 2));

		List<String[]> timezones = null;
		List<String[]> users = null;
		try {
			CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\timezones.csv"));
			CSVReader userReader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\users.csv"));
			timezones = reader.readAll();
			users = userReader.readAll();
			reader.close();
			userReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String startZone = null;
		for(String[] user : users){
			if(user[0].equals(event.getMember().getId())){
				startZone = user[1];
			}
		}
		if(startZone == null){
			event.getChannel().sendMessage("Please register yourself to a timezone").queue();
			return null;
		}
		for(String[] zone : timezones){
			if(zone[0].equals(startZone)){
				timeNumb -= Integer.parseInt(zone[1]);
				if(timeNumb > 12){
					timeNumb -= 12;
					amPM = oppisite(amPM);
				} else if(timeNumb < 1){
					timeNumb += 12;
					amPM = oppisite(amPM);
				}
				break;
			}
		}

		try {
			return timeFormatter.parse(timeNumb + ":" + mins + ":" + amPM + 'm');
		} catch (ParseException e) {
			return null;
		}
	}

	private char oppisite(char amPM) {
		if (amPM == 'p'){
			return 'a';
		} else {
			return 'p';
		}
	}

	private Date parseDate(String[] messagePieces){
		Date date;
		if(messagePieces.length == 5) {
			try {
				date = dateFormatter.parse(messagePieces[4] + "-" + Calendar.getInstance().get(Calendar.YEAR));
			} catch (ParseException e) {
				return null;
			}
		} else {
			date = new Date();
		}
		return date;
	}

	private void formatError(MessageReceivedEvent event){
		logger.log(Level.FINE, "Formatting error in received message, canceling request");
		event.getChannel().sendMessage("Error with the formatting of your message, Message should be in the format of:" +
				"\n`$newevent <event_name> <max_participants> <hh:mma>`" +
				"\nor" +
				"\n`$newevent <event_name> <max_participants> <hh:mma> <MMM-dd>`").queue();
	}
}
