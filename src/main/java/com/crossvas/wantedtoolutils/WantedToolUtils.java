package com.crossvas.wantedtoolutils;

import com.crossvas.wantedtoolutils.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(name = References.MOD_NAME, modid = References.MOD_ID, version = References.MOD_VER, acceptedMinecraftVersions = References.MC_VER, guiFactory = References.GUI_FACTORY)
public class WantedToolUtils {

	@Mod.Instance(References.MOD_ID)
	public static WantedToolUtils instance;
	
	@SidedProxy(clientSide = References.CLIENT_PROXY, serverSide = References.SERVER_PROXY)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//use proxy
		proxy.preInit(event);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		//use proxy
		proxy.postInit(event);
	}
}
