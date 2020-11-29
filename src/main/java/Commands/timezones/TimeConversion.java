package Commands.timezones;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class TimeConversion extends ServerCommand {
	public TimeConversion() {
		super("I refuse");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String message = event.getMessage().getContentRaw().toLowerCase();
		char[] chars = message.toCharArray();
		int numbEnd = -1;
		char amPM = 'f';
		for(int i = 0; i < chars.length; i++){
			if(chars[i] == 'a' && i+1 < chars.length && chars[i+1] == 'm'){
				if(i-1 >= 0 && Character.isDigit(chars[i-1])){
					amPM = 'a';
					numbEnd = i-1;
				} else if(i-2 >=0 && chars[i-1] == ' ' && Character.isDigit(chars[i-2])){
					amPM = 'a';
					numbEnd = i-2;
				}
			} else if(chars[i] == 'p' && i+1 < chars.length && chars[i+1] == 'm'){
				if(i-1 >= 0 && Character.isDigit(chars[i-1])){
					amPM = 'p';
					numbEnd = i-1;
				} else if(i-2 >=0 && chars[i-1] == ' ' && Character.isDigit(chars[i-2])){
					amPM = 'p';
					numbEnd = i-2;
				}
			}
		}
		int numbStart = findNumbStart(chars, numbEnd);
		int timeNumb = makeNumber(chars, numbStart, numbEnd);

		List<String[]> timezones = null;
		List<String[]> users = null;
		try {
			CSVReader reader = new CSVReader(new FileReader("src/main/resources/timezones.csv"));
			CSVReader userReader = new CSVReader(new FileReader("src/main/resources/users.csv"));
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
			return;
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

		String timeMessage = "Converted Times:";
		char lastChar = ';';
		String[] zone;
		for(int i = 0; i < timezones.size(); i++){
			if(i+1 == timezones.size()){
				lastChar = ' ';
			}
			zone = timezones.get(i);
			int zoneTime = timeNumb + Integer.parseInt(zone[1]);
			timeMessage += " " + parseTime(zoneTime, amPM) + " " + zone[0] + lastChar;
		}

		event.getChannel().sendMessage(timeMessage).queue();
	}

	public String parseTime(int zoneTime, char amPM) {
		if(zoneTime > 12){
			zoneTime -= 12;
			amPM = oppisite(amPM);
		} else if(zoneTime < 1){
			zoneTime += 12;
			amPM = oppisite(amPM);
		}

		if(zoneTime > 12 || zoneTime < 1){
			return parseTime(zoneTime, amPM);
		} else {
			return Integer.toString(zoneTime) + amPM + 'm';
		}
	}

	private char oppisite(char amPM) {
		if (amPM == 'p'){
			return 'a';
		} else {
			return 'p';
		}
	}

	@Override
	public boolean check(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();
		char[] chars = message.toCharArray();
		for(int i = 0; i < chars.length; i++){
			if((chars[i] == 'a' || chars[i] == 'p') && i+1 < chars.length && chars[i+1] == 'm'){
				if(i-1 >= 0 && Character.isDigit(chars[i-1])){
					return true;
				} else if(i-2 >=0 && chars[i-1] == ' ' && Character.isDigit(chars[i-2])){
					return true;
				}
			}
		}
		return false;
	}

	private int findNumbStart(char[] chars, int numbEnd){
		if(numbEnd-1 >= 0 && Character.isDigit(chars[numbEnd-1])){
			return findNumbStart(chars, numbEnd-1);
		}
		return numbEnd;
	}

	private int makeNumber(char[] chars, int numbStart, int numbEnd) {
		StringBuilder numbString = new StringBuilder();
		int i = numbStart;
		while(i <= numbEnd){
			numbString.append(chars[i]);
			i++;
		}
		return Integer.parseInt(numbString.toString());
	}
}
