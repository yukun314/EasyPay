package com.bfyd.easypay.utils.options;

public final class IntegerOption extends Option {
	private final int myDefaultValue;
	private int myValue;

	public IntegerOption(String group, String optionName, int defaultValue) {
		super(group, optionName);
		myDefaultValue = defaultValue;
		myValue = defaultValue;
	}

	public int getValue() {
		if (!myIsSynchronized) {
			String value = getConfigValue(null);
			if (value != null) {
				try {
					myValue = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
			}
			myIsSynchronized = true;
		}
		return myValue;
	}

	public void setValue(int value) {
		if (myIsSynchronized && (myValue == value)) {
			return;
		}
		myValue = value;
		myIsSynchronized = true;
		if (value == myDefaultValue) {
			unsetConfigValue();
		} else {
			setConfigValue("" + value);
		}
	}
}
