package org.mod.easy_commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;

public class KnockbackCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            Hand hand = player.getActiveHand();
            ItemStack stack = player.getStackInHand(hand);
            Map<Enchantment, Integer> enchantments = Util.make(new HashMap<>(), (map) -> map.put(Enchantments.KNOCKBACK, level));
            EnchantmentHelper.set(enchantments, stack);
            player.playSound(SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("knockback")
                .then(CommandManager.argument("level", IntegerArgumentType.integer())
                        .suggests((context, builder) -> {
                            // Add suggestions for knockback levels
                            builder.suggest(5);
                            builder.suggest(10);
                            builder.suggest(30);
                            builder.suggest(50);
                            return builder.buildFuture();
                        })
                        .executes(new KnockbackCommand())));
    }
}
