package io.radston12.reddefense.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.radston12.reddefense.commands.map.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class ModCommands {

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        ModCommands.register(dispatcher);
    }

    public static void register(CommandDispatcher dispatcher) {
        dispatcher.register(
                Commands.literal("map")
                        .then(
                                Commands.argument("size", IntegerArgumentType.integer(1, 6))
                                    .then(
                                            Commands.argument("path", StringArgumentType.string())
                                                    .executes(Map::execute)
                                    )
                        )
        );


    }
}
