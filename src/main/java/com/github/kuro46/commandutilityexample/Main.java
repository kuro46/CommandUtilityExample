package com.github.kuro46.commandutilityexample;

import com.github.kuro46.commandutility.StringConverter;
import com.github.kuro46.commandutility.StringConverters;
import com.github.kuro46.commandutility.handle.CastError;
import com.github.kuro46.commandutility.handle.Command;
import com.github.kuro46.commandutility.handle.CommandHandler;
import com.github.kuro46.commandutility.handle.CommandManager;
import com.github.kuro46.commandutility.handle.CommandSections;
import com.github.kuro46.commandutility.handle.CommandSenderType;
import com.github.kuro46.commandutility.handle.FallbackCommandHandler;
import com.github.kuro46.commandutility.handle.HelpCommandHandler;
import com.github.kuro46.commandutility.syntax.CommandSyntax;
import com.github.kuro46.commandutility.syntax.CommandSyntaxBuilder;
import com.github.kuro46.commandutility.syntax.CompletingArgument;
import com.github.kuro46.commandutility.syntax.CompletionData;
import com.github.kuro46.commandutility.syntax.LongArgument;
import com.github.kuro46.commandutility.syntax.OptionalArgument;
import com.github.kuro46.commandutility.syntax.ParseErrorReason;
import com.github.kuro46.commandutility.syntax.RequiredArgument;
import com.github.kuro46.commandutility.CompletionUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author shirokuro
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManagerImpl(
            new FallbackHandlerImpl(),
            initConverters()
        );
        commandManager.registerCommand(new Command(
            CommandSections.fromString("commandutilityexample name"),
            new NameHandler(),
            "Change executor's name."
        ));
        commandManager.registerCommand(new Command(
            CommandSections.fromString("commandutilityexample tp"),
            new TeleportHandler(),
            "Teleport executor to specified location."
        ));
        commandManager.registerCommand(new Command(
            CommandSections.fromString("commandutilityexample gamemode"),
            new GameModeHandler(),
            "Change executor's gamemode."
        ));
        CommandSections rootSection = CommandSections.fromString("commandutilityexample");
        commandManager.registerCommand(new Command(
            CommandSections.fromString("commandutilityexample help"),
            new HelpCommandHandler(rootSection),
            "Display help message."
        ));
    }

    private StringConverters initConverters() {
        StringConverters converters = new StringConverters();
        converters.registerDefaults();
        // Register StringConverter to convert from String to GameMode
        converters.registerConverter(
            GameMode.class,
            StringConverter.fromLambda((sender, target) -> {
                try {
                    return GameMode.valueOf(target);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(target + " is unknown gamemode.");
                    return null;
                }
            })
        );
        return converters;
    }

    private static class GameModeHandler extends CommandHandler {

        @Override
        public CommandSyntax getCommandSyntax() {
            return new CommandSyntaxBuilder()
                .addArgument(new RequiredArgument("gamemode"))
                .build();
        }

        @Override
        public CommandSenderType getSenderType() {
            // handleCommand.sender is org.bukkit.Player absolutely.
            return CommandSenderType.PLAYER;
        }

        @Override
        public void handleCommand(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            Map<String, String> args
        ) {
            Player player = (Player) sender;
            String stringGameMode = args.get("gamemode"); // Non-Null
            // Null if convertion failed. Converter is registered at Main.java:L40
            GameMode gameMode = caller.getStringConverters()
                .convert(GameMode.class, player, stringGameMode);
            if (gameMode == null) {
                return;
            }

            player.setGameMode(gameMode);
            player.sendMessage("Your gamemode has been updated to " + gameMode);
        }

        @Override
        public List<String> handleTabComplete(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            CompletionData completionData
        ) {
            CompletingArgument completing = completionData.getCompletingArgument();
            if (completing != null && completing.getName().equals("gamemode")) {
                return CompletionUtils.filterCandidates(
                    Arrays.asList(GameMode.values()),
                    completing.getValue()
                );
            } else {
                return Collections.emptyList();
            }
        }
    }

    private static class NameHandler extends CommandHandler {

        @Override
        public CommandSyntax getCommandSyntax() {
            return new CommandSyntaxBuilder()
                .addArgument(new LongArgument(
                    "name",
                    true /* Because this argument is required */
                ))
                .build();
        }

        @Override
        public CommandSenderType getSenderType() {
            // handleCommand.sender is org.bukkit.Player absolutely.
            return CommandSenderType.PLAYER;
        }

        @Override
        public void handleCommand(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            Map<String, String> args
        ) {
            Player player = (Player) sender;
            String name = args.get("name"); // Non-Null

            player.setDisplayName(name);
            player.sendMessage("Your display name has been updated to '" + name + "'");
        }

        @Override
        public List<String> handleTabComplete(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            CompletionData completionData
        ) {
            CompletingArgument completing = completionData.getCompletingArgument();
            if (completing != null && completing.getName().equals("name")) {
                return CompletionUtils.playerCandidates(completing.getValue());
            } else {
                return Collections.emptyList();
            }
        }
    }

    private static class TeleportHandler extends CommandHandler {

        @Override
        public CommandSyntax getCommandSyntax() {
            return new CommandSyntaxBuilder()
                .addArgument(new RequiredArgument("world"))
                .addArgument(new RequiredArgument("x"))
                .addArgument(new RequiredArgument("y"))
                .addArgument(new RequiredArgument("z"))
                .addArgument(new OptionalArgument("pitch"))
                .addArgument(new OptionalArgument("yaw"))
                .build();
        }

        @Override
        public CommandSenderType getSenderType() {
            // handleCommand.sender is org.bukkit.Player absolutely.
            return CommandSenderType.PLAYER;
        }

        @Override
        public void handleCommand(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            Map<String, String> args
        ) {
            Player player = (Player) sender;
            StringConverters converters = caller.getStringConverters();
            // "world", "x", "y", and "z" are non-null
            World world = converters.convert(World.class, player, args.get("world"));
            double x = converters.convert(Double.class, player, args.get("x"));
            double y = converters.convert(Double.class, player, args.get("y"));
            double z = converters.convert(Double.class, player, args.get("z"));
            // "pitch" and "yaw" are nullable
            float pitch = converters.convert(Float.class, player, args.get("pitch"), 0.0F);
            float yaw = converters.convert(Float.class, player, args.get("yaw"), 0.0F);

            player.teleport(new Location(world, x, y, z, pitch, yaw));
            player.sendMessage("Teleported!");
        }

        @Override
        public List<String> handleTabComplete(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            CompletionData completionData
        ) {
            CompletingArgument completing = completionData.getCompletingArgument();
            if (completing != null) {
                Player player = (Player) sender;
                Location looking = player.getTargetBlock(null, 100).getLocation();
                Number numberToSuggestion = null;
                switch (completing.getName()) {
                    case "world":
                        return CompletionUtils.worldCandidates(completing.getValue());
                    case "x":
                        numberToSuggestion = looking.getX();
                        break;
                    case "y":
                        numberToSuggestion = looking.getY();
                        break;
                    case "z":
                        numberToSuggestion = looking.getZ();
                        break;
                    case "pitch":
                        numberToSuggestion = looking.getPitch();
                        break;
                    case "yaw":
                        numberToSuggestion = looking.getYaw();
                        break;
                }
                Objects.nonNull(numberToSuggestion);
                return CompletionUtils.filterCandidates(
                    Collections.singletonList(numberToSuggestion.toString()),
                    completing.getValue()
                );
            } else {
                return Collections.emptyList();
            }
        }
    }

    private static class FallbackHandlerImpl extends FallbackCommandHandler {

        @Override
        public CommandSenderType getSenderType() {
            return CommandSenderType.ANY;
        }

        @Override
        public void handleFallback(
            CommandManager caller,
            CommandSender sender,
            CommandSections commandSections,
            List<String> args
        ) {
            sender.sendMessage("Unknown command.");
        }
    }

    private static class CommandManagerImpl extends CommandManager {

        public CommandManagerImpl(
            FallbackCommandHandler fallback,
            StringConverters converters
        ) {
            super(fallback, converters);
        }

        @Override
        public void handleCastError(CommandSender sender, CastError error) {
            switch (error) {
                case CANNOT_CAST_TO_PLAYER:
                    sender.sendMessage("Please perform this command from the game.");
                    break;
                case CANNOT_CAST_TO_CONSOLE:
                    sender.sendMessage("Please perform this command from the console.");
                    break;
            }
        }

        @Override
        public void handleParseError(CommandSender sender, ParseErrorReason error) {
            switch (error) {
                case ARGUMENTS_NOT_ENOUGH:
                    sender.sendMessage("Arguments not enough!");
                    break;
                case TOO_MANY_ARGUMENTS:
                    sender.sendMessage("Too many arguments!");
                    break;
            }
        }
    }
}

