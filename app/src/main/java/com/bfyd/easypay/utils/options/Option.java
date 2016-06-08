package com.bfyd.easypay.utils.options;

public abstract class Option {
	public static final String PLATFORM_GROUP = "PlatformOptions";
	
	private final String myGroup;
	private final String myOptionName;
	protected boolean myIsSynchronized;

	protected Option(String group, String optionName) {
		myGroup = group.intern();
		myOptionName = optionName.intern();
		myIsSynchronized = false;
	}

	protected final String getConfigValue(String defaultValue) {
		Config config = Config.Instance();
		return (config != null) ?
			config.getValue(myGroup, myOptionName, defaultValue) : defaultValue;
	}

	protected final void setConfigValue(String value) {
		Config config = Config.Instance();
		if (config != null) {
			config.setValue(myGroup, myOptionName, value);
		}
	}

	protected final void unsetConfigValue() {
		Config config = Config.Instance();
		if (config != null) {
			config.unsetValue(myGroup, myOptionName);
		}
	}
}
