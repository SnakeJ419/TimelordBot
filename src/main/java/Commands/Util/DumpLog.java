package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.LogFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class DumpLog extends ServerCommand {
	public DumpLog() {
		super("$dumplog", true, true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		int logs = -1;
		try {
			logs = Integer.parseInt(event.getMessage().getContentRaw().split(" ")[1]);
		} catch (Exception e){
			logs = 0;
		}

		File logFile = new File("src\\main\\resources\\TimeLordLog.log");
		if(logs == 0){
			event.getChannel().sendMessage("Uploading log file...").queue();
			event.getChannel().sendFile(logFile).queue();
			return;
		}

		Scanner logReader;
		try {
			logReader = new Scanner(logFile);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage());
			return;
		}

		ArrayList<String> logArray = new ArrayList<>();
		while(logReader.hasNext()) {
			logArray.add(logReader.nextLine());
		}

		if(logs >= logArray.size()){
			logs = logArray.size() - 1;
		}

		StringBuilder messageString = new StringBuilder("```");
		for(int i = logArray.size() - logs; i < logArray.size(); i++){
			messageString.append(logArray.get(i)).append("\n");
		}
		messageString.append("```");

		try {
			event.getChannel().sendMessage(messageString.toString()).queue();
		} catch (IllegalArgumentException e){
			event.getChannel().sendMessage("Requested log is to large, please request full log or a smaller sample").queue();
		}
	}
}
