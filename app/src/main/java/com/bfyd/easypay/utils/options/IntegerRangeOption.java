package com.bfyd.easypay.utils.options;

public final class IntegerRangeOption extends Option {
	public final int MinValue;
	public final int MaxValue;

	private final int myDefaultValue;
	private int myValue;

	public IntegerRangeOption(String group, String optionName, int minValue, int maxValue, int defaultValue) {
		super(group, optionName);
		MinValue = minValue;
		MaxValue = maxValue;
		if (defaultValue < MinValue) {
			defaultValue = MinValue;
		} else if (defaultValue > MaxValue) {
			defaultValue = MaxValue;
		}
		myDefaultValue = defaultValue;
		myValue = defaultValue;
	}

	public int getValue() {
		if (!myIsSynchronized) {
			String value = getConfigValue(null);
			if (value != null) {
				try {
					int intValue = Integer.parseInt(value);
					if (intValue < MinValue) {
						intValue = MinValue;
					} else if (intValue > MaxValue) {
						intValue = MaxValue;
					}
					myValue = intValue;
				} catch (NumberFormatException e) {
				}
			}
			myIsSynchronized = true;
		}
		return myValue;
	}

	public void setValue(int value) {
		if (value < MinValue) {
			value = MinValue;
		} else if (value > MaxValue) {
			value = MaxValue;
		}
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
