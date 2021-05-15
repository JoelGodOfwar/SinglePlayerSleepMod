package com.github.joelgodofwar.sps.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }
    
    public static String str_singleplayersleep_SleepMessage;//public final ForgeConfigSpec.ConfigValue<? extends String> str_singleplayersleep_SleepMessage;
    public static Boolean bool_singleplayersleep_Enabled;
    public static Integer int_singleplayersleep_SleepLength;
    
    public static void load() {
    	str_singleplayersleep_SleepMessage = SERVER.str_singleplayersleep_SleepMessage.get();
    	bool_singleplayersleep_Enabled = SERVER.bool_singleplayersleep_Enabled.get();
    	int_singleplayersleep_SleepLength = SERVER.int_singleplayersleep_SleepLength.get();
    }
    
    public static class ServerConfig {
    	public final ForgeConfigSpec.ConfigValue<String> str_singleplayersleep_SleepMessage;//public final ForgeConfigSpec.ConfigValue<? extends String> str_singleplayersleep_SleepMessage;
        public final BooleanValue bool_singleplayersleep_Enabled;
        public final IntValue int_singleplayersleep_SleepLength;
    		

    		
    	ServerConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("Single Player Sleep Mod settings")
            .push("settings");

    		str_singleplayersleep_SleepMessage = builder
    		        .comment("Defines the sleep message to be displayed when a player sleeps.",
    		                 "%1$s is a placeholder for the sleeping player's name.")
    		        .translation("singleplayersleep.config.sleepmsg")
    		        .define("str_singleplayersleep_SleepMessage", "%1$s went to bed. Sweet dreams!");
    		
    		bool_singleplayersleep_Enabled = builder
    		        .comment("Enable or Disable this mod")
    		        .translation("singleplayersleep.config.enabled")
    		        .define("bool_singleplayersleep_Enabled", true);
    		
    		int_singleplayersleep_SleepLength = builder
    		        .comment("Define the length of time to delay in ticks before changing the time.",
    		        		"200(10 seconds) is default, as it is just over the vanilla length for single player.")
    		        .translation("singleplayersleep.config.delay")
    		        .defineInRange("int_singleplayersleep_SleepLength", 200, 0, Integer.MAX_VALUE);
    		
    		builder.pop();
    		
    	}
    }
}