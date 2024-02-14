package org.mod.easy_commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EasyCommands implements ModInitializer {
    public static final String MOD_ID = "easy_commands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final GameRules.Key<GameRules.IntRule> TREE_HEIGHT =
            GameRuleRegistry.register("easy_commands.treeHeight", GameRules.Category.MISC, GameRuleFactory.createIntRule(1));

    private static final GameRules.Key<GameRules.BooleanRule> EXPLOSIVE_PROJECTILES_ENABLED =
            GameRuleRegistry.register("easy_commands.explosiveProjectiles", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static Float power = 2.5f;
    public static Iterable<ServerWorld> worlds;


    @Override
    public void onInitialize() {
        try {
			LOGGER.info("Easy Commands Initialized");
            CommandRegistrationCallback.EVENT.register(this::registerCommands);
            ServerLifecycleEvents.SERVER_STARTED.register((server -> worlds = server.getWorlds()));
            ServerLifecycleEvents.SERVER_STOPPED.register((server -> worlds = null));
        } catch (Exception e) {
            LOGGER.error("Easy Commands failed to initialize: " + e.getMessage());
        }
    }

    public static void explode(@NotNull Entity entity) {
        entity.discard();
        Vec3d pos = entity.getPos();
        World world = entity.getWorld();
        world.createExplosion(entity, pos.getX(), pos.getY(), pos.getZ(), power, World.ExplosionSourceType.TNT);
    }

    public static Boolean isExplosiveProjectilesEnabled(@NotNull World world) {
        return world.getGameRules().getBoolean(EXPLOSIVE_PROJECTILES_ENABLED);
    }

    public static Integer getTreeHeight() {
        if (worlds != null) {
            for (ServerWorld world : worlds) {
                if (world.getGameRules().getInt(TREE_HEIGHT) > 0) {
                    return world.getGameRules().getInt(TREE_HEIGHT);
                }
            }
        }
        return 1;
    }

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
     * /enableExplosiveProjectiles
     * <p>
     * /modifyTreeHeight
     * <p>
     * /explode
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
                    boolean repaired = false;
                    if (player != null) {
                        for (int i = 0; i < player.getInventory().size(); i++) {
                            ItemStack stack = player.getInventory().getStack(i);
                            if (stack.isDamageable()) {
                                stack.setDamage(0);
                                repaired = true;
                            }
                        }
                        if (!repaired) {
                            context.getSource().sendError(Text.literal("You have no items that can be repaired or damaged."));
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
                            final boolean[] repaired = {false};

                            if (BoolArgumentType.getBool(context, "repairInventory")) {

                                world.getPlayers().forEach(player -> {
                                    for (int i = 0; i < player.getInventory().size(); i++) {
                                        ItemStack stack = player.getInventory().getStack(i);
                                        if (stack.isDamageable()) {
                                            stack.setDamage(0);
                                            repaired[0] = true;
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
                                        repaired[0] = true;
                                    }
                                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                });
                            }
                            if (!repaired[0]) {
                                context.getSource().sendError(Text.literal("All players have no items that can be repaired or damaged."));
                            }
                            return Command.SINGLE_SUCCESS;
                        })));

        KnockbackCommand.register(dispatcher);
        KnockbackStickCommand.register(dispatcher);

        dispatcher.register(CommandManager.literal("killall")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("shouldAlsoKillPlayers", BoolArgumentType.bool())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            if (BoolArgumentType.getBool(context, "shouldAlsoKillPlayers")) {
                                final ParseResults<ServerCommandSource> parsed = dispatcher.parse("kill @e", source);
                                dispatcher.execute(parsed);
                            } else {
                                final ParseResults<ServerCommandSource> parsed = dispatcher.parse("kill @e[type=!player]", source);
                                dispatcher.execute(parsed);
                            }
                            return Command.SINGLE_SUCCESS;
                        })));
        dispatcher.register(CommandManager.literal("heal")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .then(CommandManager.argument("alsoFeed", BoolArgumentType.bool())
                                .executes(context -> {
                                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");

                                    if (!players.isEmpty()) {
                                            for (ServerPlayerEntity player : players) {
                                                player.setHealth(player.getMaxHealth());
                                                if (BoolArgumentType.getBool(context, "alsoFeed")) {
                                                    player.getHungerManager().add(20, 20);
                                                }
                                                player.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                            }
                                        }


                                    return Command.SINGLE_SUCCESS;
                                }))

                                .executes(context -> {
                                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");

                                    if (!players.isEmpty()) {
                                        for (ServerPlayerEntity player : players) {
                                            player.setHealth(player.getMaxHealth());
                                            player.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })));


        dispatcher.register(CommandManager.literal("feed")
                .requires(source -> source.hasPermissionLevel(3))
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
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    context.getSource().getWorld().setTimeOfDay(1000);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("noon")
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    context.getSource().getWorld().setTimeOfDay(6000);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("night")
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    context.getSource().getWorld().setTimeOfDay(13000);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("midnight")
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    context.getSource().getWorld().setTimeOfDay(18000);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("setExplosionPower")
                .requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.argument("power", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> {
                            power = FloatArgumentType.getFloat(context, "power");
                            context.getSource().sendFeedback(() -> Text.literal("Projectile explosion power set to: " + power), true);
                            context.getSource().sendFeedback(() -> Text.literal("Note that this won't work if the gamerule 'easy_commands.explosiveProjectiles' is disabled"), false);
                            return Command.SINGLE_SUCCESS;
                        })));


        dispatcher.register(CommandManager.literal("explode")
                .requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.argument("position", Vec3ArgumentType.vec3())
                        .then(CommandManager.argument("explosionPower", FloatArgumentType.floatArg(0.1f))
                                .suggests((source, builder) -> {
                                    builder.suggest(1);
                                    builder.suggest(3);
                                    builder.suggest(5);
                                    builder.suggest(10);
                                    return builder.buildFuture();
                                })
                                .then(CommandManager.argument("createFire", BoolArgumentType.bool())
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            ServerWorld world = source.getWorld();

                                            Vec3d pos = Vec3ArgumentType.getVec3(context, "position");
                                            float explosionPower = FloatArgumentType.getFloat(context, "explosionPower");
                                            boolean createFire = BoolArgumentType.getBool(context, "createFire");

                                            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), explosionPower, createFire, World.ExplosionSourceType.TNT);
                                            return Command.SINGLE_SUCCESS;
                                        })))));
    }
}