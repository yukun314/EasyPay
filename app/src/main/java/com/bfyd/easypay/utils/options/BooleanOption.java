package com.bfyd.easypay.utils.options;

public final class BooleanOption extends Option {
	private final boolean myDefaultValue;
	private boolean myValue;

	public BooleanOption(String group, String optionName, boolean defaultValue) {
		super(group, optionName);
		myDefaultValue = defaultValue;
		myValue = defaultValue;
	}

	public boolean getValue() {
		if (!myIsSynchronized) {
			String value = getConfigValue(null);
			if (value != null) {
				if ("true".equals(value)) {
					myValue = true;
				} else if ("false".equals(value)) {
					myValue = false;
				}
			}
			myIsSynchronized = true;
		}
		return myValue;
	}

	public void setValue(boolean value) {
		if (myIsSynchronized && (myValue == value)) {
			return;
		}
		myValue = value;
		myIsSynchronized = true;
		if (value == myDefaultValue) {
			unsetConfigValue();
		} else {
			setConfigValue(value ? "true" : "false");
		}
	}
}
