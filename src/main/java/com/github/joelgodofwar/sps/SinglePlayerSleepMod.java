package com.github.joelgodofwar.sps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.joelgodofwar.sps.common.config.Config;
import com.github.joelgodofwar.sps.server.events.InteractEventHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("singleplayersleepmod")
public class SinglePlayerSleepMod {
	
	// Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "singleplayersleepmod";
    InteractEventHandler IEventHandler = new InteractEventHandler();

    public SinglePlayerSleepMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC, "singleplayersleepmod-common.toml");
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    

    private void setup(final FMLCommonSetupEvent event){ // some preinit code
        LOGGER.info("SinglePlayerSleep Loading...");
        
        //Config config = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + "additional/path/to/your/config.cfg"));
    }

    private void enqueueIMC(final InterModEnqueueEvent event){ // Send InterModComms
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event){ // Receive InterModComms
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().
        //        map(m->m.getMessageSupplier().get()).
        //        collect(Collectors.toList()));
    }

    @SuppressWarnings("resource")
	private void doClientStuff(final FMLClientSetupEvent event) { // Client side stuff
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public void doServerStuff(final FMLServerStartedEvent  event) {
    	LOGGER.info("Registering Events...");
    	MinecraftForge.EVENT_BUS.register(new InteractEventHandler());
    	//IEventHandler.loadConfig();
    	LOGGER.info("Loading Complete.");
    }
    @SubscribeEvent
    public void doServerStuff(final FMLServerStoppingEvent  event) {
    	LOGGER.info("Saving configs...");
    	Config.COMMON_SPEC.save();
    }
}
