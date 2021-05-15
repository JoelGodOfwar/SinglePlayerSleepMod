package com.github.joelgodofwar.sps.server.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class SPSConfig {
	
	/**
     * General configuration that doesn't need to be synchronized but needs to be available before server startup
     */
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<String> str_singleplayersleep_SleepMessage;//public final ForgeConfigSpec.ConfigValue<? extends String> str_singleplayersleep_SleepMessage;
        public final BooleanValue bool_singleplayersleep_Enabled;
        public final IntValue int_singleplayersleep_SleepLength;

        Common(ForgeConfigSpec.Builder builder)
        {
            builder.comment("General configuration settings")
                    .push("general");

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
    
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    
}
