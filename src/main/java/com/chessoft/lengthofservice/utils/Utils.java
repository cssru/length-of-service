package com.chessoft.lengthofservice.utils;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.enums.PeriodType;
import com.chessoft.lengthofservice.ui.model.RankList;
import com.chessoft.lengthofservice.ui.model.Age;

import java.text.SimpleDateFormat;
import java.util.*;

public final class Utils {
	private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
	private Utils() {}

	public static Human emptyHuman() {
		Human result = new Human();
		result.setRank(0);
		result.setSurname("");
		result.setName("");
		result.setLastname("");
		result.setBirthday(new Date());
		return result;
	}

	public static PeriodLength servicePeriodLength(Human human, boolean multiplier) {
		PeriodLength totalServiceLength = PeriodLength.create();
		for (Period period : human.getPeriods()) {
			totalServiceLength.append(PeriodLength.create(period, multiplier));
		}

		return totalServiceLength;
	}

	public static PeriodLength servicePeriodLength(Human human, PeriodType type, boolean multiplier) {
		PeriodLength totalServiceLength = PeriodLength.create();
		for (Period period : human.getPeriods()) {
			if (period.getType().equals(type)) {
				totalServiceLength.append(PeriodLength.create(period, multiplier));
			}
		}

		return totalServiceLength;
	}

	public static String getFullName(Human human) {
		return new StringBuilder()
				.append(RankList.get(human.getRank()).getRankName())
				.append(" ")
				.append(human.getSurname())
				.append(" ")
				.append(human.getName())
				.append(" ")
				.append(human.getLastname()).toString();
	}

	public static Period findLast(Human human) {
		Period result = null;
		for (Period nextPeriod : human.getPeriods()) {
			if (isFirstBeforeSecond(result, nextPeriod)) {
				result = nextPeriod;
			}
		}

		return result;
	}

	public static boolean isFirstBeforeSecond(Period first, Period second) {
		if (first == null) return true;
		if (second == null) {
			throw new IllegalArgumentException("second Period cannot be null");
		}
		return first.getEnd().getTime() <= second.getBegin().getTime();
	}

	public static Date nextDay(Date day) {
		long millis = day.getTime();
		return new Date(millis + ONE_DAY_MILLIS);
	}

	public static Date clear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	public static void checkPeriod(Period period) {
		long begin = period.getBegin().getTime();
		long end = period.getEnd().getTime();
		if (begin >= end) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
			throw new IllegalArgumentException("Wrong Period: " + periodAsString(period));
		}
	}

	public static void checkPeriodIntersection(Period period, List<Period> periods) {
		for (Period nextPeriod : periods) {
			if (intersects(period, nextPeriod)) {
				throw new IllegalArgumentException("Period " + periodAsString(period) + " intersects with " + periodAsString(nextPeriod));
			}
		}
	}

	public static boolean intersects(Period firstPeriod, Period secondPeriod) {
		long firstBegin = firstPeriod.getBegin().getTime();
		long firstEnd = firstPeriod.getEnd().getTime();
		long secondBegin = secondPeriod.getBegin().getTime();
		long secondEnd = secondPeriod.getEnd().getTime();

		if (firstPeriod.equals(secondPeriod)) {
			return false;
		}

		if ((firstBegin < secondBegin && secondBegin < firstEnd) ||
				(firstBegin < secondEnd && secondEnd < firstEnd) ||
				(secondBegin < firstBegin && firstBegin < secondEnd) ||
				(secondBegin <  firstEnd && firstEnd < secondEnd)) {
			return true;
		}
		return false;
	}

	public static String dateAsString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		return df.format(date) + " г.";
	}

	public static String periodAsString(Period period) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		return df.format(period.getBegin()) + " - " + df.format(period.getEnd());
	}

	public static String getYearsSuffix(int years) {
		if (years >= 5 && years <= 20) return "лет";
		if (years % 10 == 0) return "лет";
		if (years % 10 == 1) return "год";
		if (years % 10 < 5) return "года";
		return "лет";
	}

	public static String periodLengthAsShortString(PeriodLength periodLength) {
		return new StringBuilder()
				.append(periodLength.getYears())
				.append(" ")
				.append(getYearsSuffix(periodLength.getYears())).toString();
	}

	public static String periodLengthAsLongString(PeriodLength periodLength) {
		return new StringBuilder()
				.append(periodLength.getYears())
				.append(" ")
				.append(getYearsSuffix(periodLength.getYears()))
				.append(" ")
				.append(periodLength.getMonths())
				.append(" мес. ")
				.append(periodLength.getDays())
				.append(" дн.")
				.toString();
	}

	public static String getAgeAsString(Age age) {
		return new StringBuilder()
				.append(age.getYears())
				.append(" ")
				.append(getYearsSuffix(age.getYears())).toString();
	}

	public static Period getPeriodByTypeAndNumber(Human human, PeriodType type, int number) {
		List<Period> result = new ArrayList<>();
		for (Period period : human.getPeriods()) {
			if (period.getType().equals(type)) {
				result.add(period);
			}
		}
		Collections.sort(result, (first, second) -> (int)(first.getEnd().getTime() - second.getEnd().getTime()));
		if (number < result.size()) {
			return result.get(number);
		} else {
			return null;
		}
	}

	public static String extractYear(Date date) {
		return date == null
				?
				" "
				:
				new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
	}

	public static String extractMonth(Date date) {
		return date == null
				?
				" "
				:
				new SimpleDateFormat("MM", Locale.getDefault()).format(date);
	}

	public static String extractDay(Date date) {
		return date == null
				?
				" "
				:
				new SimpleDateFormat("dd", Locale.getDefault()).format(date);
	}

	public static String numberAsWords(int number) {
		if (number < 10) {
			return beforeTen(number);
		}
		if (number < 20) {
			return beforeTwenty(number);
		}
		if (number < 100) {
			int x1 = number % 10;
			int x10 = number - x1;
			StringBuilder sb = new StringBuilder();
			if (x10 != 0) {
				sb.append(beforeHundered(x10));
			}
			if (x1 != 0) {
				sb.append(" ")
						.append(beforeTen(x1));
			}
			return sb.toString();
		}
		return "???";
	}

	private static String beforeTen(int number) {
		if (number < 0) {
			number = -number;
		}

		switch (number) {
			case 0: return "ноль";
			case 1: return "один";
			case 2: return "два";
			case 3: return "три";
			case 4: return "четыре";
			case 5: return "пять";
			case 6: return "шесть";
			case 7: return "семь";
			case 8: return "восемь";
			case 9: return "девять";
			default: return "???";
		}
	}

	private static String beforeTwenty(int number) {
		if (number < 0) {
			number = -number;
		}

		switch (number) {
			case 10: return "десять";
			case 11: return "одиннадцать";
			case 12: return "двенадцать";
			case 13: return "тринадцать";
			case 14: return "четырнадцать";
			case 15: return "пятнадцать";
			case 16: return "шестнадцать";
			case 17: return "семнадцать";
			case 18: return "восемнадцать";
			case 19: return "девятнадцать";
			default: return "???";
		}
	}

	private static String beforeHundered(int number) {
		if (number < 0) {
			number = -number;
		}

		switch (number) {
			case 20: return "двадцать";
			case 30: return "тридцать";
			case 40: return "сорок";
			case 50: return "пятьдесят";
			case 60: return "шестьдесят";
			case 70: return "семьдесят";
			case 80: return "восемьдесят";
			case 90: return "девяносто";
			default: return "???";
		}
	}

}
