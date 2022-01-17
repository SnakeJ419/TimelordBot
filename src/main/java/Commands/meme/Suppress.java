package Commands.meme;

import Commands.ServerCommand;
import SlashTesting.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class Suppress extends ServerCommand implements SlashCommand {

	public Suppress() {
		super("$suppress", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		String memberText = event.getMessage().getContentRaw().toLowerCase().split("suppress ")[1];
		memberText = memberText.replace("<@!", "").replace(">", "");
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		event.getGuild().retrieveMemberById(memberText).queue(member -> {
			event.getJDA().addEventListener(new SilentSuppressor(member, event.getGuild()));
//			member.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(
//					"You have been suppressed in the server '" + event.getGuild().getName() + "'\n"
//					+ "You will not be able to join voice channels or send messages until the suppression has ended\n"
//					+ "Contact support at 425-647-3034 to remove your suppression")).queue();
			event.getGuild().moveVoiceMember(member, null).queue();
		});
	}

	@Override
	public void run(SlashCommandEvent event) {
		User user = event.getOption("user").getAsUser();
		event.getJDA().addEventListener(new SilentSuppressor(user, event.getGuild()));
		event.reply(user.getName() + " has been suppressed").queue();
	}

	private class SilentSuppressor extends ListenerAdapter {
		private Member suppressed;
		private Guild guild;

		public SilentSuppressor(Member suppressed, Guild guild){
			this.suppressed = suppressed;
			this.guild = guild;
			logger.log(Level.INFO, "Suppressed [" + suppressed.getEffectiveName() + "] in [" + guild.getName() + "]");
		}

		public SilentSuppressor(User suppressed, Guild guild){
			guild.loadMembers().onSuccess(users -> {
				for(Member member : users){
					if(member.getUser().getId().equals(suppressed.getId())){
						this.suppressed = member;
						break;
					}
				}
				this.guild = guild;
				guild.moveVoiceMember(this.suppressed, null).queue();
			});
		}

		@Override
		public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
			if(event.getGuild() == guild && event.getMember().getId().equals(suppressed.getId())){
				event.getGuild().moveVoiceMember(event.getMember(), null).queue();
			}
		}

		@Override
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			if(event.getGuild() == guild && !event.getMember().getId().equals(suppressed.getId()) && event.getMember().hasPermission(Permission.ADMINISTRATOR) && event.getMessage().getContentRaw().toLowerCase().equals("$stopsuppression")){
				event.getJDA().removeEventListener(this);
				event.getChannel().deleteMessageById(event.getMessageId()).queue();
				logger.log(Level.INFO, "Ended suppression on [" + suppressed.getEffectiveName() + "] in [" + guild.getName() + "]");
			} else if(event.getGuild() == guild && event.getMember().getId().equals(suppressed.getId())){
				event.getMessage().delete().queue();
			}
		}
	}

}
