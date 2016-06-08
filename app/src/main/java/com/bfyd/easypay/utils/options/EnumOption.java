package com.bfyd.easypay.utils.options;

public final class EnumOption<T extends Enum<T>> extends Option {
	private final T myDefaultValue;
	private T myValue;

	public EnumOption(String group, String optionName, T defaultValue) {
		super(group, optionName);
		myDefaultValue = defaultValue;
		myValue = defaultValue;
	}

	public T getValue() {
		if (!myIsSynchronized) {
			final String value = getConfigValue(null);
			if (value != null) {
				try {
					myValue = T.valueOf(myDefaultValue.getDeclaringClass(), value);
				} catch (Throwable t) {
				}
			}
			myIsSynchronized = true;
		}
		return myValue;
	}

	public void setValue(T value) {
		if (myIsSynchronized && (myValue == value)) {
			return;
		}
		myValue = value;
		myIsSynchronized = true;
		if (value == myDefaultValue) {
			unsetConfigValue();
		} else {
			setConfigValue("" + value.toString());
		}
	}
}
