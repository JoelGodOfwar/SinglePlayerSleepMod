package com.github.joelgodofwar.sps.config;

public class CCommon extends ConfigBase {
	
	//public CWorldGen worldGen = nested(0, CWorldGen::new, Comments.worldGen);
	//public ConfigBool logTeErrors = b(false, "logTeErrors", Comments.logTeErrors);

	@Override
	public String getName() {
		return "common";
	}

	@SuppressWarnings("unused")
	private static class Comments {
		//static String worldGen = "Modify Create's impact on your terrain";
		//static String logTeErrors = "Forward caught TileEntityExceptions to the log at debug level.";
	}

}
