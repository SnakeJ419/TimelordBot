package Commands.Seasonal;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Mistletoe extends ServerCommand {
	private final static ArrayList<Long> channelIDs = new ArrayList<>();
	private final static ArrayList<Boolean> channelStatus = new ArrayList<>();

	public Mistletoe() {
		super("$mistletoe", false, false);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {

		if(!channelIDs.contains(event.getChannel().getIdLong())){
			channelIDs.add(event.getChannel().getIdLong());
			channelStatus.add(false);
		}

		int i = channelIDs.indexOf(event.getChannel().getIdLong());
		if(!channelStatus.get(i)) {
			channelStatus.set(i, true);
			event.getJDA().addEventListener(new MistletoeListener(event.getChannel(), event.getMember(), i));
			event.getChannel().deleteMessageById(event.getMessageId()).queue();
		} else {
			event.getChannel().sendMessage("Channel can only be trapped once! oh no, it looks like you've made a mistake").queue();
		}
	}

	private static class MistletoeListener extends ListenerAdapter{
		private final MessageChannel trappedChannel;
		private final Member placer;
		private final int index;
		private Member memberOne;
		private User userOne;

		protected MistletoeListener(MessageChannel trappedChannel, Member placer, int index){
			this.trappedChannel = trappedChannel;
			this.placer = placer;
			this.index = index;
		}

		@Override
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			if(event.getChannel() == trappedChannel && event.getAuthor().getIdLong() != 733407709130391582L){
				if(userOne == null){
					memberOne = event.getMember();
					userOne = event.getAuthor();
				} else if(userOne != event.getAuthor()){
					channelStatus.set(index, false);
					event.getJDA().removeEventListener(this);

					trappedChannel.sendMessage(memberOne.getEffectiveName() + " was kissed by " + event.getMember().getEffectiveName() + " under the mistletoe placed by " + placer.getEffectiveName() + '!').queue();
				}
			}
		}
	}
}
