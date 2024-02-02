package org.mod.easy_commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EasyCommands implements ModInitializer {
	public static final String MOD_ID = "easy_commands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		try {
			CommandRegistrationCallback.EVENT.register(this::registerCommands);
		}
		catch (Exception e) {
			LOGGER.error("Easy Commands failed to initialize: " + e.getMessage());
		}}
	/**
	 * <p>
	 * Current working commands:
	 * <p>
	 * /repair
	 * <p>
	 * /repairinventory
	 * <p>
	 * /repairall
	 * <p>
	 * /enchantsword
	 * <p>
	 * /knockback
	 * <p>
	 * /knockbackstick
	 * <p>
	 * /killall
	 * <p>
	 * /heal
	 * <p>
	 * /feed
	 * <p>
	 * /day
	 * <p>
	 * /night
	 * <p>
     * /midnight
     * <p>
	 * if any commands not listed above, they may not work and may be fixed later
	 */
	private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(CommandManager.literal("repair")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					ServerPlayerEntity player = context.getSource().getPlayer();
					if (player != null) {
						Hand hand = player.getActiveHand();
						ItemStack stack = player.getStackInHand(hand);

						if (stack.isDamageable()) {
							stack.setDamage(0);
							player.playSound(SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
						} else {
							context.getSource().sendError(Text.literal("The item you are holding cannot be repaired or damaged."));
							return Command.SINGLE_SUCCESS;
						}
					}
					return Command.SINGLE_SUCCESS;
				}));

		dispatcher.register(CommandManager.literal("enchantsword")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					ServerPlayerEntity player = context.getSource().getPlayer();
					if (player != null) {
						Hand hand = player.getActiveHand();
						ItemStack stack = player.getStackInHand(hand);
						if (stack.isOf(Items.WOODEN_SWORD) || stack.isOf(Items.STONE_SWORD) || stack.isOf(Items.GOLDEN_SWORD) || stack.isOf(Items.IRON_SWORD) || stack.isOf(Items.DIAMOND_SWORD) || stack.isOf(Items.NETHERITE_SWORD)) {
							Map<Enchantment, Integer> enchantments = Util.make(new HashMap<>(), map -> {
								map.put(Enchantments.SHARPNESS, 5);
								map.put(Enchantments.SWEEPING, 3);
								map.put(Enchantments.MENDING, 1);
								map.put(Enchantments.UNBREAKING, 3);
								map.put(Enchantments.FIRE_ASPECT, 2);
								map.put(Enchantments.KNOCKBACK, 2);
								map.put(Enchantments.LOOTING, 1);
							});
							EnchantmentHelper.set(enchantments, stack);
							player.playSound(SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
						} else {
							context.getSource().sendError(Text.literal("You must hold any sword to enchant."));
							return Command.SINGLE_SUCCESS;
						}
					}
					return Command.SINGLE_SUCCESS;
				}));
		dispatcher.register(CommandManager.literal("repairinventory")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					ServerPlayerEntity player = context.getSource().getPlayer();
					if (player != null) {
						for (int i = 0; i < player.getInventory().size(); i++) {
							ItemStack stack = player.getInventory().getStack(i);
							if (stack.isDamageable()) {
								stack.setDamage(0);
							}
						}
						player.playSound(SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					return Command.SINGLE_SUCCESS;
				}));

		dispatcher.register(CommandManager.literal("repairall")
				.requires(source -> source.hasPermissionLevel(4))
						.then(CommandManager.argument("repairInventory", BoolArgumentType.bool())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							ServerWorld world = source.getWorld();
							Vec3d pos = source.getPosition();

							if (BoolArgumentType.getBool(context, "repairInventory")) {

								world.getPlayers().forEach(player -> {
									for (int i = 0; i < player.getInventory().size(); i++) {
										ItemStack stack = player.getInventory().getStack(i);
										if (stack.isDamageable()) {
											stack.setDamage(0);
										}
									}
									world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
								});
							} else {
								world.getPlayers().forEach(player -> {
										Hand hand = player.getActiveHand();
										ItemStack stack = player.getStackInHand(hand);
										if (stack.isDamageable()) {
											stack.setDamage(0);
										}
									world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
								});
							}
							return Command.SINGLE_SUCCESS;
						})));

		// Commands separated to support suggestions
		KnockbackCommand.register(dispatcher);
		KnockbackStickCommand.register(dispatcher);

		dispatcher.register(CommandManager.literal("killall")
				.requires(source -> source.hasPermissionLevel(4))
				.then(CommandManager.argument("shouldAlsoKillPlayers", BoolArgumentType.bool())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							if (BoolArgumentType.getBool(context, "shouldAlsoKillPlayers")) {
								ParseResults<ServerCommandSource> parsed = dispatcher.parse("kill @e", source);
								dispatcher.execute(parsed);
							} else {
								ParseResults<ServerCommandSource> parsed = dispatcher.parse("kill @e[type=!player]", source);
								dispatcher.execute(parsed);
							}
							return Command.SINGLE_SUCCESS;
						})));
		dispatcher.register(CommandManager.literal("heal")
				.requires(source -> source.hasPermissionLevel(4))
				.then(CommandManager.argument("allPlayers", BoolArgumentType.bool())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							ServerWorld world = source.getWorld();
							ServerPlayerEntity player = source.getPlayer();
							Vec3d pos = source.getPosition();

							if (!BoolArgumentType.getBool(context, "allPlayers")) {
								if (player != null) {
									player.setHealth(player.getMaxHealth());
									player.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
								}
							} else if (BoolArgumentType.getBool(context, "allPlayers")) {
								world.getPlayers().forEach(p -> p.setHealth(p.getMaxHealth()));
								source.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
							}

							return Command.SINGLE_SUCCESS;
						})));

		dispatcher.register(CommandManager.literal("feed")
				.requires(source -> source.hasPermissionLevel(4))
				.then(CommandManager.argument("allPlayers", EntityArgumentType.players())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
                            Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "allPlayers");

                            if (!players.isEmpty()) {
								for (ServerPlayerEntity player : players) {
									player.getHungerManager().add(20, 20);
									player.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
								}
							} else {
								source.sendError(Text.literal("No players found."));
							}

							return Command.SINGLE_SUCCESS;
						})));


		dispatcher.register(CommandManager.literal("day")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					context.getSource().getWorld().setTimeOfDay(1000);
					return Command.SINGLE_SUCCESS;
				}));

		dispatcher.register(CommandManager.literal("noon")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					context.getSource().getWorld().setTimeOfDay(6000);
					return Command.SINGLE_SUCCESS;
				}));

		dispatcher.register(CommandManager.literal("night")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					context.getSource().getWorld().setTimeOfDay(13000);
					return Command.SINGLE_SUCCESS;
				}));

		dispatcher.register(CommandManager.literal("midnight")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					context.getSource().getWorld().setTimeOfDay(18000);
					return Command.SINGLE_SUCCESS;
				}));
	}
}