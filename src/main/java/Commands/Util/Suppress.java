package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Suppress extends ServerCommand {

	public Suppress() {
		super("$suppress", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		String memberText = event.getMessage().getContentRaw().toLowerCase().split("suppress ")[1];
		memberText = memberText.replace("<@!", "").replace(">", "");
		event.getGuild().retrieveMemberById(memberText).queue(member -> {
			event.getJDA().addEventListener(new SilentSuppressor(member, event.getGuild()));
			event.getGuild().moveVoiceMember(member, null).queue();
		});
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
	}

	private class SilentSuppressor extends ListenerAdapter {
		private final Member suppressed;
		private final Guild guild;

		public SilentSuppressor(Member suppressed, Guild guild){
			this.suppressed = suppressed;
			this.guild = guild;
		}

		@Override
		public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
			if(event.getGuild() == guild && event.getMember().getId().equals(suppressed.getId())){
				event.getGuild().moveVoiceMember(event.getMember(), null).queue();
			}
		}

		@Override
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR) && event.getMessage().getContentRaw().toLowerCase().equals("$stopsuppression")){
				event.getJDA().removeEventListener(this);
				event.getChannel().deleteMessageById(event.getMessageId()).queue();
			}
		}
	}

}
