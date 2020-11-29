//package Commands.Util;
//
//import Commands.ServerCommand;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//
//public class DiceTest extends ServerCommand {
//
//	private static final String diceParserID = "279722369260453888";
//
//	public DiceTest(){
//		super("$dicetest");
//	}
//
//	@Override
//	public void runCommand(MessageReceivedEvent event) {
//		String[] pieces = event.getMessage().getContentRaw().split(" ");
//		if(pieces.length != 2){
//			event.getChannel().sendMessage("Command not set up in valid manner").queue();
//			return;
//		}
//
//		event.getChannel().sendMessage(pieces[1]).queue();
//		event.getJDA().addEventListener(new AverageClac(event));
//	}
//
//	private class AverageClac extends ListenerAdapter{
//		private final MessageReceivedEvent parent;
//
//		public AverageClac(MessageReceivedEvent parent){
//			this.parent = parent;
//		}
//
//
//		@Override
//		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
//			if(event.getChannel().getId().equals(parent.getChannel().getId()) && event.getAuthor().getId().equals(diceParserID)){
//				File file = new File()
//				if(event.getMessage().getContentRaw().equals("")){
//					event.getMessage().getAttachments().get(0).downloadToFile()
//				} else {
//
//				}
//				parent.getJDA().removeEventListener(this);
//			}
//		}
//	}
//}
