/*
 * Copyright (c) 2017 Rubicon Bot Development Team
 *
 * Licensed under the MIT license. The full license text is available in the LICENSE file provided with this project.
 */

package fun.rubicon.commands.general;

import fun.rubicon.RubiconBot;
import fun.rubicon.command.CommandCategory;
import fun.rubicon.command2.CommandHandler;
import fun.rubicon.command2.CommandManager;
import fun.rubicon.data.PermissionRequirements;
import fun.rubicon.data.UserPermissions;
import fun.rubicon.util.Colors;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * Handles the 'ping' command.
 */
public class CommandPing extends CommandHandler {
    /**
     * Constructs the CommandHandler.
     */
    public CommandPing() {
        super(new String[]{"ping"}, CommandCategory.GENERAL,
                new PermissionRequirements(0, "command.ping"),
                "Get the bot's ping", "ping");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        //Returns a Message with Embed included
        return new MessageBuilder().setEmbed(new EmbedBuilder()
                .setDescription("Ping: " + RubiconBot.getJDA().getPing() + "ms")
                .setColor(Colors.COLOR_PRIMARY)
                .build()).build();
    }
}
