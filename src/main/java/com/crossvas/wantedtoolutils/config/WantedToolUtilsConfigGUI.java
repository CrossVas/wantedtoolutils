package com.crossvas.wantedtoolutils.config;

import static com.crossvas.wantedtoolutils.config.WantedToolUtilsConfig.config;

import java.util.ArrayList;
import java.util.List;

import com.crossvas.wantedtoolutils.References;
import com.crossvas.wantedtoolutils.config.WantedToolUtilsConfig.WantedToolUtilsConfigCategory;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

public class WantedToolUtilsConfigGUI extends GuiConfig {

	public WantedToolUtilsConfigGUI(GuiScreen guiScreen) {
		super(guiScreen, getConfigElements(guiScreen), References.MOD_ID, false, false, References.MOD_NAME + " - " + "Config");
	}

	private static List<IConfigElement> getConfigElements(GuiScreen guiScreen) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		for (WantedToolUtilsConfigCategory category : WantedToolUtilsConfig.configCategories) {
			list.add(new ConfigElement<ConfigCategory>(config.getCategory(category.name.toLowerCase())));
		}
		return list;
	}
}
