package org.mod.easy_commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class KnockbackStickCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            PlayerInventory inventory = player.getInventory();
            ItemStack stack = enchantStick(level);
            int slot = inventory.getEmptySlot();
            // if there are no available slots, drop the item
            if (slot == -1) {
                ItemStack droppedStack = enchantStick(level);
                player.dropItem(droppedStack, false);
                return Command.SINGLE_SUCCESS;
            }
            inventory.insertStack(slot, stack);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("knockbackstick")
                .then(CommandManager.argument("level", IntegerArgumentType.integer())
                        .suggests((context, builder) -> {
                            // Add suggestions for knockback levels
                            builder.suggest(5);
                            builder.suggest(10);
                            builder.suggest(30);
                            builder.suggest(50);
                            return builder.buildFuture();
                        })
                        .executes(new KnockbackStickCommand())));

    }
    private ItemStack enchantStick(int level) {
        ItemStack stack = new ItemStack(Items.STICK);
        stack.addEnchantment(Enchantments.KNOCKBACK, level);
        stack.setCustomName(Text.literal("Knockback Stick"));
        return stack;
    }
}
