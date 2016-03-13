package com.chessoft.lengthofservice.enums;

public enum PeriodType {
	NORMAL,
	ONE_MONTH_FOR_HALF,
	ONE_MONTH_FOR_THREE,
	ONE_MONTH_FOR_TWO,
	ONE_MONTH_FOR_ONE_AND_HALF;

	@Override
	public String toString() {
		switch (this) {
			case NORMAL: return "Месяц за месяц";
			case ONE_MONTH_FOR_HALF: return "Месяц за 0,5 месяца";
			case ONE_MONTH_FOR_THREE: return "Месяц за 3 месяца";
			case ONE_MONTH_FOR_TWO: return "Месяц за 2 месяца";
			case ONE_MONTH_FOR_ONE_AND_HALF: return "Месяц за 1,5 месяца";
		}
		return "unknown";
	}
}
