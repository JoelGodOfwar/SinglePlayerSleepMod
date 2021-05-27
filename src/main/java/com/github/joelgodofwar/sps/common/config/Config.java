package com.github.joelgodofwar.sps.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import org.apache.commons.lang3.tuple.Pair;

public class Config {
    //public static final ServerConfig SERVER;
    //public static final ForgeConfigSpec SERVER_SPEC;
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    
    public static String str_SleepMessage;
    public static Boolean bool_Enabled;
    public static Integer int_SleepLength;
    public static Boolean bool_CancelOnExit;
    public static Boolean bool_Debug;
    
    public static void load() {
    	str_SleepMessage = COMMON.str_SleepMessage.get();
    	bool_Enabled = COMMON.bool_Enabled.get();
    	int_SleepLength = COMMON.int_SleepLength.get();
    	bool_CancelOnExit = COMMON.bool_CancelOnExit.get();
    	bool_Debug = COMMON.bool_Debug.get();
    }
    
    public static class CommonConfig {
    	public final ForgeConfigSpec.ConfigValue<String> str_SleepMessage;
        public final BooleanValue bool_Enabled;
        public final IntValue int_SleepLength;
        public final BooleanValue bool_CancelOnExit;
        public final BooleanValue bool_Debug;
    		

    		
        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("Single Player Sleep Mod settings")
            .push("settings");

    		str_SleepMessage = builder
    		        .comment(" Defines the sleep message to be displayed when a player sleeps.",
    		                 " %1$s is a placeholder for the sleeping player's name.")
    		        .translation("singleplayersleep.config.sleepmsg")
    		        .define("str_SleepMessage", "%1$s went to bed. Sweet dreams!");
    		
    		bool_Enabled = builder
    		        .comment(" Enable or Disable this mod")
    		        .translation("singleplayersleep.config.enabled")
    		        .define("bool_Enabled", true);
    		
    		int_SleepLength = builder
    		        .comment(" Define the length of time to delay in ticks before changing the time.",
    		        		" 300(15 seconds) is default, as it is just over the vanilla length for single player.")
    		        .translation("singleplayersleep.config.delay")
    		        .defineInRange("int_SleepLength", 300, 20, Integer.MAX_VALUE);
    		
    		bool_CancelOnExit =  builder
    		        .comment(" Should SPS cancel sleep if the player exits the bed?")
    		        .translation("singleplayersleep.config.cancel_on_exit")
    		        .define("bool_CancelOnExit", true);
    		
    		bool_Debug =  builder
    		        .comment(" Should SPS log debug information in the console/log?")
    		        .translation("singleplayersleep.config.debug")
    		        .define("bool_Debug", false);
    		
    		builder.pop();
    		
    	}
    }
}