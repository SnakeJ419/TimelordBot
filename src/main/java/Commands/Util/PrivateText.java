package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.templates.TemplateChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.checkerframework.checker.index.qual.LTEqLengthOf;

import javax.annotation.Nonnull;
import java.util.List;

public class PrivateText extends ServerCommand {
    public PrivateText() {
        super("$privatetext");
    }

    @Override
    public void runCommand(MessageReceivedEvent event) {
        String rawText = event.getMessage().getContentRaw().toLowerCase();
        String[] pieces = rawText.split(" ", 2);

        if(pieces.length != 2){
            event.getChannel().sendMessage("Error, command format must be `$privateText <newChannelName>`").queue();
            return;
        }

        event.getGuild().retrieveMember(event.getAuthor()).queue(member -> {
            if(!member.getVoiceState().inVoiceChannel()){
                event.getChannel().sendMessage("Must be in voice channel").queue();
                return;
            }

            List<Category> categoryList = event.getGuild().getCategoriesByName("Private Channels", true);
            if(categoryList.size() != 0){
                buildChannel(categoryList.get(0), member, pieces[1]);
            } else {
                event.getGuild().createCategory("Private Channels").queue(category -> {
                    buildChannel(category, member, pieces[1]);
                });
            }
        });
    }

    private void buildChannel(Category category, Member memberAuthor, String name) {
        category.getGuild().createTextChannel(name, category).queue(textChannel -> {
                textChannel.getManager().putRolePermissionOverride(
                        category.getGuild().getRolesByName("@everyone", true).get(0).getIdLong(),
                        Permission.UNKNOWN.getRawValue(),
                        Permission.VIEW_CHANNEL.getRawValue())
                        .queue(k -> {
                            for(Member member : memberAuthor.getVoiceState().getChannel().getMembers()){
                                textChannel.getManager().putMemberPermissionOverride(member.getIdLong(),
                                Permission.VIEW_CHANNEL.getRawValue(),
                                Permission.UNKNOWN.getRawValue()).queue();
                            }
                            category.getJDA().addEventListener(new PrivateTextWatcher(memberAuthor.getVoiceState().getChannel(), textChannel));
                        });
            });
    }

    private static class PrivateTextWatcher extends ListenerAdapter {
        private final VoiceChannel voiceChannel;
        private final TextChannel textChannel;

        public PrivateTextWatcher(VoiceChannel voiceChannel, TextChannel textChannel) {
            this.voiceChannel = voiceChannel;
            this.textChannel = textChannel;
        }

        @Override
        public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event){
            if(event.getChannelJoined().getIdLong() == voiceChannel.getIdLong()) {
                textChannel.getManager().putMemberPermissionOverride(event.getMember().getIdLong(),
                        Permission.VIEW_CHANNEL.getRawValue(),
                        Permission.UNKNOWN.getRawValue()).queue();
            }
        }

        @Override
        public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event){
            if(event.getChannelLeft().getIdLong() == voiceChannel.getIdLong() && event.getChannelLeft().getMembers().size() == 0){
                event.getJDA().removeEventListener(this);
            }
        }
    }
}
