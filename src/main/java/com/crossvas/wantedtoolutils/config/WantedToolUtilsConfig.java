package com.crossvas.wantedtoolutils.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.crossvas.wantedtoolutils.References;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class WantedToolUtilsConfig {

	public static Configuration config;
	
	/**
	 * General
	 * 
	 * */
	
	public static boolean enableVeinMode = true;
//	public static boolean enableTorchPlacement = true;
	
	/**
	 * Compat
	 * 
	 * */
	
	public static boolean enableRSACompat 			= true;
	public static boolean enableIC2Compat 			= true;
	public static boolean enableEIOCompat 			= true;
	
	public static void init(String configLocation) {
		if (config == null) {
			File path = new File(configLocation + "/" + References.MOD_ID + ".cfg");
			config = new Configuration(path);
			loadConfig();
		}
	}
	
	public static void loadConfig() {
		enableVeinMode 			= config.getBoolean("Vein Miner", general.name, enableVeinMode, "Enable Vein Miner");
//		enableTorchPlacement 	= config.getBoolean("Torch Placement", general.name, enableTorchPlacement, "Enable Torch Placement");
		enableRSACompat 		= config.getBoolean("Redstone Arsenal Compat", compat.name, enableRSACompat, "Enable Redstone Arsenal Compat");
		enableIC2Compat 		= config.getBoolean("IC2 Compat", compat.name, enableIC2Compat, "Enable IC2 Compat");
		enableEIOCompat 		= config.getBoolean("EnderIO Compat", compat.name, enableEIOCompat, "Enable EnderIO Compat");
		if (config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(References.MOD_ID)) {
			loadConfig();
		}
	}
	
	public static Configuration getConfig() {
		return config;
	}
	
	public static final List<WantedToolUtilsConfigCategory> configCategories;
	
	static {
		configCategories = new ArrayList<WantedToolUtilsConfig.WantedToolUtilsConfigCategory>();
	}
	
	public static class WantedToolUtilsConfigCategory {
		String name;
		
		public WantedToolUtilsConfigCategory(String name) {
			this.name = name;
			registerCategory();
		}
		
		public void registerCategory() {
			configCategories.add(this);
		}
	}
	
	public static final WantedToolUtilsConfigCategory general = new WantedToolUtilsConfigCategory("General");
	public static final WantedToolUtilsConfigCategory compat = new WantedToolUtilsConfigCategory("Compat");
}
