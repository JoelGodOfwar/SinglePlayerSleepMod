package com.github.joelgodofwar.sps.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;

import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class SimpleConfig {
	public static Properties props = new Properties();
	public static final Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
	public static File configFile = new File(defaultConfigPath + File.separator + "singleplayersleepmod.xml");
	public static HashMap<String, Object> config = new HashMap<String,Object>();
	
	public static void load() {
		try {
			InputStream inputStream = new FileInputStream(configFile);
			props.loadFromXML(inputStream);
			inputStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save() {
		System.out.println("Saving file");
		try {
			OutputStream outputStream = new FileOutputStream(configFile);
			props.storeToXML(outputStream, "singleplayersleepmod settings");
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("File saved. " + configFile);
	}
	
	public String getString(String key, String def) {
		return (String) props.getOrDefault(key, def);
	}
	
	public int getInt(String key, String def) {
		return (Integer) props.getOrDefault(key, def);
	}
	
	public boolean getBoolean(String key, boolean def) {
		return (boolean) props.getOrDefault(key, def);
	}
	
	public Double getDouble(String key, Double def) {
		return (Double) props.getOrDefault(key, def);
	}
	
	public static void setValue(String key, Object value) {
		props.setProperty(key, String.valueOf(value));
	}
	
	public static void saveDefaults() {
		System.out.println("Setting values");
		setValue("enabled", true);
		setValue("sleepMessage", "%1$s went to bed. Sweet dreams!");
		setValue("lengthOfDelay", 200);
		save();
	}
}
