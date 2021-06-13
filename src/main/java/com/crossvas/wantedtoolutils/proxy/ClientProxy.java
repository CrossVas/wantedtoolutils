package com.crossvas.wantedtoolutils.proxy;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		//MinecraftForge.EVENT_BUS.register(new ToolTipEvent());
	}
}
