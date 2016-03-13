package com.chessoft.lengthofservice.ui.model;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.domain.Period;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Age implements Comparable<Age> {
	private int years;
	private int months;
	private int days;

	public Age(Date birthday) {
		Period lifePeriod = new Period();
		lifePeriod.setBegin(birthday);
		lifePeriod.setEnd(new Date());
		PeriodLength lifeLength = PeriodLength.create(lifePeriod, false);
		years = lifeLength.getYears();
		months = lifeLength.getMonths();
		days = lifeLength.getYears();
	}

	@Override
	public int compareTo(Age age) {
		int myTotalMonths = years * 12 + months;
		int hisTotalMonths = age.getYears() * 12 + age.getMonths();
		if (myTotalMonths != hisTotalMonths) {
			return myTotalMonths - hisTotalMonths;
		}
		return days - age.getDays();
	}
}
