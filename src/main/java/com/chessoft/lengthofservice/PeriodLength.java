package com.chessoft.lengthofservice;

import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.enums.PeriodType;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class PeriodLength implements Comparable<PeriodLength> {
	private int years;
	private int months;
	private int days;

	private PeriodLength(int years, int months, int days) {
		this.years = years;
		this.months = months;
		this.days = days;
	}

	public static PeriodLength create(Period period, boolean useMultiplier) {
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		int years;
		int months;
		int days;

		beginCalendar.setTime(period.getBegin());
		endCalendar.setTime(period.getEnd());

		int beginMonths = beginCalendar.get(Calendar.YEAR) * 12 + beginCalendar.get(Calendar.MONTH);
		int endMonths = endCalendar.get(Calendar.YEAR) * 12 + endCalendar.get(Calendar.MONTH);

		PeriodType periodType = period.getType();
		if (periodType == null) {
			periodType = PeriodType.NORMAL;
		}

		int diffMonths = endMonths - beginMonths;

		int diffDays = endCalendar.get(Calendar.DAY_OF_MONTH) - beginCalendar.get(Calendar.DAY_OF_MONTH);
		if (diffDays < 0) {
			diffMonths--;
			endCalendar.add(Calendar.MONTH, -1);
			diffDays += endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		if (useMultiplier) {
			double multiplier = getMultiplier(periodType);
			diffMonths *= multiplier;
			diffDays *= multiplier;
		}

		years = diffMonths / 12;
		months = diffMonths % 12;
		days = diffDays;

		return new PeriodLength(years, months, days);
	}

	public static PeriodLength create() {
		return new PeriodLength(0, 0, 0);
	}

	public void append(PeriodLength periodLength) {
		years += periodLength.getYears();
		months += periodLength.getMonths();
		days += periodLength.getDays();
		months += days / 30;
		days %= 30;
		years += months / 12;
		months %= 12;
	}

	private static double getMultiplier(PeriodType periodType) {
		switch (periodType) {
			case NORMAL: return 1.0;
			case ONE_MONTH_FOR_HALF: return 0.5;
			case ONE_MONTH_FOR_ONE_AND_HALF: return 1.5;
			case ONE_MONTH_FOR_TWO: return 2.0;
			case ONE_MONTH_FOR_THREE: return 3.0;
		}
		return 1.0;
	}

	@Override
	public int compareTo(PeriodLength periodLength) {
		int myTotalMonths = years * 12 + months;
		int hisTotalMonths = periodLength.getYears() * 12 + periodLength.getMonths();
		if (myTotalMonths != hisTotalMonths) {
			return myTotalMonths - hisTotalMonths;
		}
		return days - periodLength.getDays();
	}
}
