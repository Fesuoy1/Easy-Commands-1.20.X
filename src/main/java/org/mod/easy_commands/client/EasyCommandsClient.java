package org.mod.easy_commands.client;

import net.fabricmc.api.ClientModInitializer;

import static org.mod.easy_commands.EasyCommands.LOGGER;

public class EasyCommandsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LOGGER.info("Easy Commands Initialized");
    }
}
