package fun.rubicon.commands.settings;

import fun.rubicon.RubiconBot;
import fun.rubicon.command.CommandCategory;
import fun.rubicon.command.CommandHandler;
import fun.rubicon.command.CommandManager;
import fun.rubicon.data.PermissionLevel;
import fun.rubicon.data.PermissionRequirements;
import fun.rubicon.data.UserPermissions;
import fun.rubicon.util.EmbedUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandLeaveMessage extends CommandHandler {
    public CommandLeaveMessage() {
        super(new String[] {"leavemsg", "leavemessage"}, CommandCategory.SETTINGS, new PermissionRequirements(PermissionLevel.WITH_PERMISSION, "command.leavemessage"), "Set the server's leave message!", " <disable/Message(%user% for username, %guild% for guildname)>", false);
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0)
            return createHelpMessage();
        String content = parsedCommandInvocation.getMessage().getContentDisplay().replace(parsedCommandInvocation.getPrefix() + parsedCommandInvocation.getCommandInvocation(), "");
        if(content.equalsIgnoreCase("disable") || content.equalsIgnoreCase("false") || content.equalsIgnoreCase("0")){
            RubiconBot.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "leavemsg", "0");
            return new MessageBuilder().setEmbed(EmbedUtil.success("Disabled", "Succesfully disabled leavemessages").build()).build();
        }
        RubiconBot.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "leavemsg", content);
        return new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(":white_check_mark:  Successfully set leavemessage to `" + content + "`!").build()).build();
    }
}
