package io.radston12.reddefense.commands.map;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Map {
    public static int execute(CommandContext<CommandSourceStack> command) {

        if (!(command.getSource().getEntity() instanceof Player)) {
            command.getSource().sendSystemMessage(Component.literal("This command should only be used by players!"));
            return 0;
        }
        Player p = (Player) command.getSource().getEntity();

        if (p.level().isClientSide) return 0; // should not be possible!

        ServerLevel sLevel = (ServerLevel) p.level();

        //p.getInventory().add(ModItems.getItemAsItemStack("manner"));

        int size = command.getArgument("size", Integer.class);
        String path = command.getArgument("path", String.class);


        try { // "https://image.spreadshirtmedia.net/image-server/v1/products/T1459A839PA4459PT28D163573998W10000H8860/views/1,width=550,height=550,appearanceId=839,backgroundColor=F2F2F2/tom-und-jerry-freunde-sticker.jpg"
            int[] a = MapUtils.createMap(path, size, size, sLevel);
            if (a.length == 1) {
                if (a[0] == -1) {
                    p.sendSystemMessage(Component.literal("Error: Only square images!"));
                    return 0;
                } else if (a[0] == -2) {
                    p.sendSystemMessage(Component.literal("Error: Failed to load image!"));
                    return 0;
                }
            }

            for (int b : a) {
                ItemStack stack = new ItemStack(Items.FILLED_MAP);

                stack.getOrCreateTag().putInt("map", b);

                p.getInventory().add(stack);
                p.sendSystemMessage(Component.literal("MapId: " + b));
            }
        } catch (Exception e) {
            e.printStackTrace();
            p.sendSystemMessage(Component.literal("Error: Unknown Error!"));
        }


        return Command.SINGLE_SUCCESS;


    }

}
