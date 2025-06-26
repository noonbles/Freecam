package net.xolt.freecam.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.EntityArgument;

import java.util.Collection;
import static net.minecraft.commands.arguments.EntityArgument.getPlayers;

public class FreecamCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("freecam_enable")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> targets = getPlayers(context, "target");

                            for (ServerPlayer player : targets) {
                                    ItemStack eye = new ItemStack(Items.ENDER_EYE);
                                    CompoundTag tag = new CompoundTag();
                                
                                    tag.putBoolean("freecam_allowed", true);
                                    tag.putBoolean("Unbreakable", true);
                                    eye.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                                    eye.set(DataComponents.ITEM_NAME, Component.literal("§6All-Seeing Eye"));
                                    eye.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                                    player.getInventory().add(eye);
                                    context.getSource().sendSuccess(() ->
                                    Component.literal("You now have the All-Seeing Eye. Freecam is now available."), true);
                            }

                            return 1;
                        })
                    )
                );
    }
}