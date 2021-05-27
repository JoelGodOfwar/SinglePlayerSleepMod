package com.github.joelgodofwar.sps.server.events;


import org.apache.logging.log4j.Logger;

import com.github.joelgodofwar.sps.SinglePlayerSleepMod;
import com.github.joelgodofwar.sps.common.config.Config;
import com.mojang.datafixers.util.Either;

import net.minecraft.block.BedBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity.SleepResult;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class InteractEventHandler {
	public long daTimer = -1;
	public PlayerEntity sleepingPlayer;
	public World sleepingWorld;
	Logger log;
	private static long mobSpawningStartTime = 12541;//12600;
	private static long mobSpawningStopTime = 23600;
	public boolean canceled;
	
	public static String sleepMsg;
	public static boolean enabled;
	public static int delayLen;
	//public Either<SleepResult, Unit> sleepResult;
	
    @SuppressWarnings("resource")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerSleep(PlayerInteractEvent.RightClickBlock event){
    	log = SinglePlayerSleepMod.LOGGER;
    	
    	if(!Config.COMMON.bool_Enabled.get()) {
    		return;
    	}
    	
    	canceled = event.isCanceled();
    	BlockPos a = event.getPos();
    	World world = event.getWorld();
    	PlayerEntity player = event.getPlayer();
    	if(!event.getWorld().isRemote) {
    		if (world.getBlockState(event.getPos()).getBlock() instanceof BedBlock) {
    			 if (!IsNight(world)&&!world.isThundering()) {
                     return;
                 }
    			//** Check if player can sleep */ 
    			Either<SleepResult, Unit> sleepResult = event.getPlayer().trySleep(event.getPos());
    			 String result = sleepResult.toString();
    			 if( result.contains("NOT_POSSIBLE_HERE") || result.contains("NOT_POSSIBLE_NOW") || result.contains("NOT_SAFE") || 
    					 result.contains("OBSTRUCTED") || result.contains("OTHER_PROBLEM") || result.contains("TOO_FAR_AWAY") ) {
    				// Sleep not allowed NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, NOT_SAFE, OBSTRUCTED, OTHER_PROBLEM, TOO_FAR_AWAY
    				 return;
    			 }else {
    				// Sleep allowed
    				 if(sleepingPlayer == null) {
 	                	sleepingPlayer = player;
 	                	if(Config.COMMON.bool_Debug.get()){log.info("sleepingPlayer SET");}
 	                }
 	                if(sleepingWorld == null) {
 	                	sleepingWorld = world;
 	                	if(Config.COMMON.bool_Debug.get()){log.info("sleepingWorld SET");}
 	                }
 	                daTimer = Config.COMMON.int_SleepLength.get(); // 200 = 10 seconds
    			 }
    			 
    		}
    	}
    	
    }
    
    @SubscribeEvent
	public void servertick(ServerTickEvent event){
    	if(daTimer == 0){
    		if(Config.COMMON.bool_Debug.get()){log.info("STE - timer finished");}
			daTimer = -1;
			if(Config.COMMON.bool_Debug.get()){log.info("STE - canceled=" + canceled);}
			if(!canceled) {
				if(sleepingPlayer.isSleeping()) {
					broadcast(sleepingWorld, Config.COMMON.str_SleepMessage.get());//"singleplayersleepmod.sleepmsg");
					if(Config.COMMON.bool_Debug.get()){log.info("STE - broadcast sent");}
					setDatime(sleepingPlayer, sleepingWorld);
					if(Config.COMMON.bool_Debug.get()){log.info("STE - setTime run");}
					sleepingPlayer = null;
					sleepingWorld = null;
					if(Config.COMMON.bool_Debug.get()){log.info("STE - variables reset");}
					canceled = false;
				}
			}
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
	}
	
	public void broadcast(World world, String string){
		//MinecraftServer mcserver = world.getServer();
		StringTextComponent msg = new StringTextComponent(string);
    	msg.getStyle().applyFormatting(TextFormatting.GOLD);
    	
    	TranslationTextComponent component = new TranslationTextComponent(string, sleepingPlayer.getDisplayName());
		component.mergeStyle(TextFormatting.GOLD);
		if(Config.COMMON.bool_Debug.get()){log.info("B - String parsed");}
		
		                              /** func_232641_a_ is probably broadcastMessage */
        world.getServer().getPlayerList().func_232641_a_(component, ChatType.SYSTEM, sleepingPlayer.getUniqueID());
        //mcserver.getPlayerList().broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
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
	public void playerJoin(PlayerLoggedInEvent event) {
		
	}
	
	public void loadConfig() {
		sleepMsg = Config.str_SleepMessage;
		enabled = Config.bool_Enabled;
		delayLen = Config.int_SleepLength;
	}
	
	public void saveConfig() {
		Config.COMMON.str_SleepMessage.set(sleepMsg);
		Config.COMMON.int_SleepLength.set(delayLen);
		Config.COMMON.bool_Enabled.set(enabled);
		Config.COMMON.str_SleepMessage.save();
		Config.COMMON.int_SleepLength.save();
		Config.COMMON.bool_Enabled.save();
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void wakeBed(PlayerWakeUpEvent event) {
		if(Config.COMMON.bool_CancelOnExit.get()) {
			canceled = true;
		}
	}
	
}
