package com.github.joelgodofwar.sps.common.config;

import net.minecraftforge.fml.config.ModConfig;

@SuppressWarnings("unused")
public final class ConfigHelper {
	private static ModConfig clientConfig;
	private static ModConfig serverConfig;
	
	public static ModConfig commonConfig;
	public static String SleepLengthPath;
	
	public static void bakeClient(final ModConfig config) {		
		clientConfig = config;

		//ExampleModConfig.clientBoolean = ConfigHolder.CLIENT.clientBoolean.get();
		//ExampleModConfig.clientStringList = ConfigHolder.CLIENT.clientStringList.get();
		//ExampleModConfig.clientEnumDyeColor = ConfigHolder.CLIENT.clientEnumDyeColor.get();
		
	}
	
	public static void bakeServer(final ModConfig config) {
		serverConfig = config;
		SinglePlayerSleepModConfig.str_singleplayersleep_SleepMessage = ConfigHolder.SERVER.str_singleplayersleep_SleepMessage.get();
		SinglePlayerSleepModConfig.int_singleplayersleep_SleepLength = ConfigHolder.SERVER.int_singleplayersleep_SleepLength.get();
		SinglePlayerSleepModConfig.bool_singleplayersleep_Enabled = ConfigHolder.SERVER.bool_singleplayersleep_Enabled.get();
		
	}
	
	public static void bakeCommon(final ModConfig config) {
		commonConfig = config;
		SinglePlayerSleepModConfig.str_singleplayersleep_SleepMessage = ConfigHolder.COMMON.str_singleplayersleep_SleepMessage.get();
		SinglePlayerSleepModConfig.int_singleplayersleep_SleepLength = ConfigHolder.COMMON.int_singleplayersleep_SleepLength.get();
		SinglePlayerSleepModConfig.bool_singleplayersleep_Enabled = ConfigHolder.COMMON.bool_singleplayersleep_Enabled.get();
		SleepLengthPath = ConfigHolder.COMMON.int_singleplayersleep_SleepLength.getPath().toString();
	}
		
	public static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
		
	}
}
