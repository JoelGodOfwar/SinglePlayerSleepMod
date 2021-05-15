package com.github.joelgodofwar.sps.server.events;


import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.github.joelgodofwar.sps.SinglePlayerSleepMod;
import com.github.joelgodofwar.sps.common.config.Config;
import com.github.joelgodofwar.sps.common.config.ConfigHelper;
import com.github.joelgodofwar.sps.common.config.ConfigHolder;
import com.github.joelgodofwar.sps.common.config.SinglePlayerSleepModConfig;
import com.github.joelgodofwar.sps.server.config.SPSConfig;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.BedBlock;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@SuppressWarnings("unused")
public class InteractEventHandler {
	public long daTimer = -1;
	public PlayerEntity sleepingPlayer;
	public World sleepingWorld;
	Logger log;
	private static long mobSpawningStartTime = 12541;//12600;
	private static long mobSpawningStopTime = 23600;
	
	public static String sleepMsg;
	public static boolean enabled;
	public static int delayLen;
	
    @SuppressWarnings("resource")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerSleep(PlayerInteractEvent.RightClickBlock event){
    	log = SinglePlayerSleepMod.LOGGER;
    	Result result = event.getResult();
    	boolean canceled = event.isCanceled();
    	
        if(!event.getWorld().isRemote) {
        	log.info("event.getWorld().isRemote=" + event.getWorld().isRemote);
        	final String modId = "singleplayersleepmod";
            final ModConfig.Type type = ModConfig.Type.SERVER;
            final String configFileName = ConfigTracker.INSTANCE.getConfigFileName(modId, type);
            if (configFileName != null) {
            	File f = new File(configFileName);
            	log.info("config file=" + f.getAbsolutePath());
            }else {
            	log.warn("Config file not found! - Warning");
            }
            final Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
            log.info("defaultConfigPath=" + defaultConfigPath);
            log.info("configFileName=" + configFileName);
            log.info("sleepMsg=" + Config.SERVER.str_singleplayersleep_SleepMessage.get());
            log.info("sleepLength=" + Config.SERVER.int_singleplayersleep_SleepLength.get());
            log.info("enabled=" + Config.SERVER.bool_singleplayersleep_Enabled.get());
            Config.SERVER.str_singleplayersleep_SleepMessage.save();
            //ConfigHelper.setValueAndSave(ConfigHelper.commonConfig, ConfigHelper.SleepLengthPath, 100);
        	//log.info("event.getWorld().isRemote=" + event.getWorld().isRemote);
        	
        	PlayerEntity player = event.getPlayer();
			World world = event.getWorld();
        	
            if (world.getBlockState(event.getPos()).getBlock() instanceof BedBlock) {
            	log.info("IsNight(world)=" + IsNight(world));
            	log.info("world.isThundering()=" + world.isThundering());
                if (!IsNight(world)&&!world.isThundering()) {
                    return;
                }
                if( result != Result.DENY && !canceled ) {
	                if(sleepingPlayer == null) {
	                	sleepingPlayer = player;
	                	log.info("sleepingPlayer SET");
	                }
	                if(sleepingWorld == null) {
	                	sleepingWorld = world;
	                	log.info("sleepingWorld SET");
	                }
	                daTimer = 200; // 200 = 10 seconds
                }
                
                //log.info("world.getDayTime=" + world.getDayTime());
                //log.info("world.getGameTime=" + world.getGameTime());
                //log.info("world.getTimeOfDay=" + world.getTimeOfDay(0));
            }
        }
    }
    
    @SubscribeEvent
	public void servertick(ServerTickEvent event){
    	if(daTimer == 0){
    		log.info("STE - timer finished");
			daTimer = -1;
			broadcast(sleepingWorld, "%1$s went to bed. Sweet dreams!");//"singleplayersleepmod.sleepmsg");
			log.info("STE - broadcast sent");
			setDatime(sleepingPlayer, sleepingWorld);
			log.info("STE - setTime run");
			sleepingPlayer = null;
			sleepingWorld = null;
			log.info("STE - variables reset");
    	}else if(daTimer > 0){
			daTimer--;
		}
    }
    
    public void setDatime(PlayerEntity player, World world){
        //int i = (300 + (new Random()).nextInt(600)) * 20;
        ServerWorld serverworld = (ServerWorld) world;
		if( world.isRaining() || world.isThundering()){
			serverworld.setWeather(60000, 0, false, false);
		}
		long Relative_Time = 24000 - world.getDayTime();
        serverworld.setDayTime(world.getDayTime() + Relative_Time);
		// TODO Add debug log here
	}
	
	public void broadcast(World world, String string){
		//MinecraftServer mcserver = world.getServer();
		StringTextComponent msg = new StringTextComponent(string);
    	msg.getStyle().applyFormatting(TextFormatting.GOLD);
    	
    	TranslationTextComponent component = new TranslationTextComponent(string, sleepingPlayer.getDisplayName());
		component.mergeStyle(TextFormatting.GOLD);
		log.info("B - String parsed");
		//MinecraftServer server = world.getServer().getPlayerList().sendMessageToTeamOrAllPlayers(sleepingPlayer, component);;
        /**for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
    		player.sendMessage(component, Util.DUMMY_UUID);
    		log.info("B - Player " + player.getName() + " messaged");
    		//
        }//*/
        world.getServer().getPlayerList().func_232641_a_(component, ChatType.SYSTEM, sleepingPlayer.getUniqueID());//.sendMessageToTeamOrAllPlayers(sleepingPlayer, component);
        //mcserver.getPlayerList().broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
        log.info("B - sleepMsg=" + sleepMsg);
        log.info("B - sleepLen=" + delayLen);
        log.info("B - enabled=" + enabled);
	}
	
	public static void writeChatMessage(PlayerEntity player, String translationKey, TextFormatting color) {
		TranslationTextComponent component = new TranslationTextComponent(translationKey);
		component.getStyle().applyFormatting(color);
		player.sendMessage(component, Util.DUMMY_UUID);
	}
	
	public static boolean IsNight(World world){
		long time = (world.getDayTime()) % 24000;
		return time >= mobSpawningStartTime && time < mobSpawningStopTime;
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		final ModConfig config = event.getConfig();
		// Rebake the configs when they change
		if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
			ConfigHelper.bakeClient(config);
		}else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
			ConfigHelper.bakeServer(config);
		}else if (config.getSpec() == ConfigHolder.COMMON_SPEC) {
			ConfigHelper.bakeCommon(config);
		}
		
	}
	
	public void loadConfig() {
		sleepMsg = SinglePlayerSleepModConfig.str_singleplayersleep_SleepMessage;
		enabled = SinglePlayerSleepModConfig.bool_singleplayersleep_Enabled;
		delayLen = SinglePlayerSleepModConfig.int_singleplayersleep_SleepLength;
	}
	
	public void saveConfig() {
		Config.SERVER.str_singleplayersleep_SleepMessage.set(sleepMsg);
		Config.SERVER.int_singleplayersleep_SleepLength.set(delayLen);
		Config.SERVER.bool_singleplayersleep_Enabled.set(enabled);
		Config.SERVER.str_singleplayersleep_SleepMessage.save();
		Config.SERVER.int_singleplayersleep_SleepLength.save();
		Config.SERVER.bool_singleplayersleep_Enabled.save();
	}
}
