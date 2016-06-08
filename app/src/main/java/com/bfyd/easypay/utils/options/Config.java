package com.bfyd.easypay.utils.options;

import java.util.List;

public abstract class Config {
	public static Config Instance() {
		return ourInstance;
	}

	private static Config ourInstance;

	protected Config() {
		ourInstance = this;
	}

	public abstract List<String> listGroups();
	public abstract List<String> listNames(String group);

	public abstract String getValue(String group, String name, String defaultValue);
	public abstract void setValue(String group, String name, String value);
	public abstract void unsetValue(String group, String name);
	public abstract void removeGroup(String name);
}
