package fun.rubicon.commands.music;

import fun.rubicon.command.CommandCategory;
import fun.rubicon.command.CommandHandler;
import fun.rubicon.command.CommandManager;
import fun.rubicon.core.music.MusicManager;
import fun.rubicon.data.PermissionLevel;
import fun.rubicon.data.PermissionRequirements;
import fun.rubicon.data.UserPermissions;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Yannick Seeger / ForYaSee
 */
public class CommandQueue extends CommandHandler {

    public CommandQueue() {
        super(new String[]{"queue"}, CommandCategory.MUSIC, new PermissionRequirements(PermissionLevel.EVERYONE, "command.queue"), "Shows the songs that are in the queue", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        MusicManager musicManager = new MusicManager(parsedCommandInvocation);
        return musicManager.sendQueue();
    }
}
