package com.github.joelgodofwar.sps.config;

public class CClient extends ConfigBase {
	
	public ConfigGroup client = group(0, "client",
		"Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!");


	@Override
	public String getName() {
		return "client";
	}

	public enum PlacementIndicatorSetting {
		TEXTURE, TRIANGLE, NONE
	}
}