package com.crossvas.wantedtoolutils.proxy;

import com.crossvas.wantedtoolutils.config.WantedToolUtilsConfig;
import com.crossvas.wantedtoolutils.events.BlockBreakEvent;
import com.crossvas.wantedtoolutils.events.ItemUseEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		// do config here
		WantedToolUtilsConfig.init(event.getModConfigurationDirectory().toString());
		FMLCommonHandler.instance().bus().register(new WantedToolUtilsConfig());
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		// do events here
		MinecraftForge.EVENT_BUS.register(new BlockBreakEvent());
		MinecraftForge.EVENT_BUS.register(new ItemUseEvent());
	}
	
}
