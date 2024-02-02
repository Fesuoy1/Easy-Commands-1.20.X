package org.mod.easy_commands;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

import static org.mod.easy_commands.EasyCommands.MOD_ID;
public class EasyCommandsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
	}

	@Override
	public @Nullable String getEffectiveModId() {
		return MOD_ID;
	}
}
