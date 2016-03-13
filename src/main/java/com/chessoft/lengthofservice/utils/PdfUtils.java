package com.chessoft.lengthofservice.utils;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.enums.PeriodType;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.itextpdf.text.pdf.BaseFont.createFont;

public class PdfUtils {
	private static BaseFont baseFont;
	private static Font titleFont;
	private static Font tableFont;
	private static Font smallFont;

	static {
		BaseFont baseFont = null;
		try {
			baseFont = createFont("fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		titleFont = new Font(baseFont, 14);
		tableFont = new Font(baseFont, 9);
		smallFont = new Font(baseFont, 7);
	}

	private PdfUtils() {

	}

	public static void createPdf(Human human) throws IOException, DocumentException {
		final String DEST = "pdf/" + Utils.getFullName(human) + ".pdf";
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		Document document = new Document(PageSize.A4, 70, 20, 20, 20);
		PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();


		// Title
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_CENTER);
		title.setFont(titleFont);
		title.add("РАСЧЕТ");
		document.add(title);

		Paragraph subtitle = new Paragraph();
		subtitle.setAlignment(Element.ALIGN_CENTER);
		subtitle.setFont(titleFont);
		subtitle.add("выслуги лет военнослужащего на пенсию");
		document.add(subtitle);

		// Empty string
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(14);
		table.setWidthPercentage(100);
		PdfPCell cell;
		Phrase phrase;
		PeriodLength length;

		// Name of soldier
		Paragraph name = new Paragraph();
		name.setFont(titleFont);
		name.add(Utils.getFullName(human));
		phrase = new Phrase(name);
		cell = new PdfPCell(phrase);
		cell.setColspan(14);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);

		// Empty line
		cell = new PdfPCell(new Phrase(" "));
		cell.setColspan(14);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		// Birthday
		Paragraph birthday = new Paragraph();
		birthday.setFont(tableFont);
		birthday.add(Utils.dateAsString(human.getBirthday()));

		Paragraph birthdayLabel = new Paragraph();
		birthdayLabel.setFont(smallFont);
		birthdayLabel.add("\nЧисло, месяц и год рождения");

		phrase = new Phrase();
		phrase.add(birthday);
		phrase.add(birthdayLabel);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
		table.addCell(cell);

		// Table header years/months/days - left side
		Paragraph years = new Paragraph();
		years.setFont(tableFont);
		years.setAlignment(Element.ALIGN_CENTER);
		years.add("Годы");
		cell = new PdfPCell(new Phrase(years));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		Paragraph months = new Paragraph();
		months.setFont(tableFont);
		months.setAlignment(Element.ALIGN_CENTER);
		months.add("Месяцы");
		cell = new PdfPCell(new Phrase(months));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		Paragraph days = new Paragraph();
		days.setFont(tableFont);
		days.setAlignment(Element.ALIGN_CENTER);
		days.add("Дни");
		cell = new PdfPCell(new Phrase(days));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		// Big empty cell
		phrase = new Phrase(" ");
		cell = new PdfPCell(phrase);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.LEFT);
		table.addCell(cell);

		// Table header years/months/days - right side
		cell = new PdfPCell(new Phrase(years));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(months));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(days));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		// Begin table body
		Paragraph p = new Paragraph();
		p.setFont(tableFont);
		p.add("1. Календарный срок службы");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setColspan(3);
		cell.setRowspan(16);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		table.addCell(totalCell());

		// BEGIN OF ENTIRE LINE 1

		// Item 1 / Line 1.1 (begin period)
		Period period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 0);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		p = new Paragraph();
		p.setFont(tableFont);
		p.add("в) один месяц службы за два месяца");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setColspan(3);
		cell.setRowspan(16);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		table.addCell(totalCell());

		// Item b) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 0);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item 1 / Line 1.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 0);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 0);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 0);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 0);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 1

		// BEGIN OF ENTIRE LINE 2

		table.addCell(totalCell());

		// Item 1 / Line 2.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 1);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side
		table.addCell(totalCell());

		// Item b) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 1);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item 1 / Line 1.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 1);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 1);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 1);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 1);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 2

		// BEGIN OF ENTIRE LINE 3

		table.addCell(totalCell());

		// Item 1 / Line 2.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 2);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side
		table.addCell(totalCell());

		// Item b) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 2);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item 1 / Line 1.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 2);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 2);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 2);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 2);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 3

		// BEGIN OF ENTIRE LINE 4

		table.addCell(totalCell());

		// Item 1 / Line 2.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 3);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(totalCell());

		// Item b) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 3);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item 1 / Line 1.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 3);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 3);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 3);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_TWO, 3);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 4

		// BEGIN OF ENTIRE LINE 5

		table.addCell(totalCell());

		// Item 1 / Line 2.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 4);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(dataCell("Всего"));

		// Item v) (total for all periods ONE_MONTH_FOR TWO)
		p.setFont(tableFont);
		PeriodLength totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_TWO, false);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// Item 1 / Line 1.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 4);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.2 (total for all periods with multiplier)
		p = new Paragraph();
		p.setFont(smallFont);
		p.add("Срок, зачисля-емый в выслугу");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);


		p = new Paragraph();
		p.setFont(tableFont);
		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_TWO, true);
		p.add(Integer.toString(totalLength.getYears()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		p = new Paragraph();
		p.setFont(tableFont);
		p.add(Integer.toString(totalLength.getMonths()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		p = new Paragraph();
		p.setFont(tableFont);
		p.add(Integer.toString(totalLength.getDays()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.NORMAL, 3);
		addPeriodCells(table, period, false);

		// Item b) / Line b.2 (total for all NORMAL periods)
		table.addCell(dataCell("Всего"));

		totalLength = Utils.servicePeriodLength(human, PeriodType.NORMAL, false);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));
		// END OF ENTIRE LINE 5
		// END OF ITEMS 1 and b)

		/***************************************************************************************/

		// START FOR ITEMS 2 AND g)

		p = new Paragraph();
		p.setFont(tableFont);
		p.add("2. Периоды службы, зачисляемые в выслугу лет на льготных условиях:");
		p.add("\n\n");
		p.add("а) один месяц службы за 0,5 месяца");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setColspan(3);
		cell.setRowspan(6);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		table.addCell(totalCell());

		// BEGIN OF ENTIRE LINE 1 PART 2

		// Item 1 / Line 1.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_HALF, 0);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		p = new Paragraph();
		p.setFont(tableFont);
		p.add("г) один месяц службы за полтора месяца");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setColspan(3);
		cell.setRowspan(20);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 0);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item a) (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_HALF, 0);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 0);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_HALF, 0);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 0);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 1 PART 2

		// BEGIN OF ENTIRE LINE 2 PART 2
		// Item a) (total for all periods FOR 0,5 with multiplier)
		p = new Paragraph();
		p.setFont(smallFont);
		p.add("Срок, зачисля-емый в выслугу");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);


		p = new Paragraph();
		p.setFont(tableFont);
		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_HALF, true);
		p.add(Integer.toString(totalLength.getYears()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		p = new Paragraph();
		p.setFont(tableFont);
		p.add(Integer.toString(totalLength.getMonths()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		p = new Paragraph();
		p.setFont(tableFont);
		p.add(Integer.toString(totalLength.getDays()));
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 1);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 1);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item b) / Line b.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 1);
		addPeriodCells(table, period, false);
		// END OF ENTIRE LINE 2 PART 2

		/****************************************************************************/
		// BEGIN ITEM b) LINE 1 / ITEM g) LINE 3
		p = new Paragraph();
		p.setFont(tableFont);
		p.add("б) один месяц службы за 3 месяца");
		phrase = new Phrase(p);
		cell = new PdfPCell(phrase);
		cell.setColspan(3);
		cell.setRowspan(15);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		// BEGIN OF ENTIRE b) LINE 1 / g) LINE 3
		table.addCell(totalCell());

		// Item 1 / Line 1.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 0);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 2);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item a) (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 0);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 2);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 0);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 2);
		addPeriodCells(table, period, false);
		// END OF ENTIRE b) LINE 1 / g) LINE 3

		// BEGIN OF ENTIRE b) LINE 2 / g) LINE 4
		table.addCell(totalCell());

		// Item 1 / Line 1.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 1);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 3);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item a) (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 1);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 3);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 1);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 3);
		addPeriodCells(table, period, false);
		// END OF ENTIRE b) LINE 2 / g) LINE 4

		// BEGIN OF ENTIRE b) LINE 3 / g) LINE 5
		table.addCell(totalCell());

		// Item 1 / Line 1.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 2);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 4);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item a) (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 2);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 4);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 2);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 4);
		addPeriodCells(table, period, false);
		// END OF ENTIRE b) LINE 3 / g) LINE 5

		// BEGIN OF ENTIRE b) LINE 4 / g) LINE 6
		table.addCell(totalCell());

		// Item 1 / Line 1.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 3);
		addDateCells(table, period == null ? null : period.getBegin());

		// Right side

		table.addCell(totalCell());

		// Item g) / Line b.1 (begin period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 5);
		addDateCells(table, period == null ? null : period.getBegin());

		// Item a) (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 3);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item g) / Line b.2 (end period)
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 5);
		addDateCells(table, period == null ? null : period.getEnd());

		// Item 1 / Line 1.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_THREE, 3);
		addPeriodCells(table, period, false);

		// Item b) / Line b.3 (total of period)
		p = new Paragraph();
		p.setFont(tableFont);
		period = Utils.getPeriodByTypeAndNumber(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, 5);
		addPeriodCells(table, period, false);
		// END OF ENTIRE b) LINE 4 / g) LINE 6

		// Item b) (total for all 3-MONTH periods)
		table.addCell(dataCell("Всего"));

		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_THREE, false);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// Item g) (total for all 1,5-MONTH periods)
		table.addCell(dataCell("Всего"));

		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, false);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// Item b) (total for all 3-MONTH periods using multiplier)
		table.addCell(dataCell("Срок, зачисля-емый в выслугу", smallFont));

		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_THREE, true);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// Item g) (total for all 1,5-MONTH periods using multiplier)
		table.addCell(dataCell("Срок, зачисля-емый в выслугу", smallFont));

		totalLength = Utils.servicePeriodLength(human, PeriodType.ONE_MONTH_FOR_ONE_AND_HALF, true);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// TOTAL SERVICE LINE
		cell = dataCell("3. Общая выслуга", 8);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		totalLength = Utils.servicePeriodLength(human, true);
		table.addCell(dataCell(Integer.toString(totalLength.getYears())));
		table.addCell(dataCell(Integer.toString(totalLength.getMonths())));
		table.addCell(dataCell(Integer.toString(totalLength.getDays())));

		// ITEM 4
		cell = dataCell("4. Полные годы выслуги", 3);
		cell.setBorder(Rectangle.TOP);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);

		PeriodLength calendarLength = Utils.servicePeriodLength(human, false);

		cell = dataCell((Utils.numberAsWords(calendarLength.getYears()) + " " + Utils.getYearsSuffix(calendarLength.getYears())).toUpperCase(), 11);
		cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);

		cell = dataCell(" ", 3);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);

		cell = dataCell(" ", 11);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);

		document.add(table);

		p = new Paragraph();
		p.setFont(tableFont);
		p.setAlignment(Element.ALIGN_LEFT);
		p.add("\"___\" ______________ 20___ г.");
		document.add(p);

		document.close();
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}
	}

	private static PdfPCell dataCell(String data, Font font, int colspan) {
		Paragraph p = new Paragraph();
		p.setFont(font != null ? font : tableFont);
		p.add(data);
		Phrase phrase = new Phrase(p);
		PdfPCell cell = new PdfPCell(phrase);
		if (colspan > 0) {
			cell.setColspan(colspan);
		}
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell dataCell(String data) {
		return dataCell(data, null, 0);
	}

	private static PdfPCell dataCell(String data, Font font) {
		return dataCell(data, font, 0);
	}

	private static PdfPCell dataCell(String data, int colspan) {
		return dataCell(data, null, colspan);
	}

	private static PdfPCell totalCell() {
		Paragraph p = new Paragraph();
		p.setFont(tableFont);
		p.add("Итого");
		Phrase phrase = new Phrase(p);
		phrase.setFont(tableFont);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setRowspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		return cell;
	}

	private static void addDateCells(PdfPTable table, Date date) {
		table.addCell(dataCell(Utils.extractYear(date)));
		table.addCell(dataCell(Utils.extractMonth(date)));
		table.addCell(dataCell(Utils.extractDay(date)));
	}

	private static void addPeriodCells(PdfPTable table, Period period, boolean useMultiplier) {
		if(period!=null) {
			PeriodLength length = PeriodLength.create(period, useMultiplier);
			table.addCell(dataCell(Integer.toString(length.getYears())));
			table.addCell(dataCell(Integer.toString(length.getMonths())));
			table.addCell(dataCell(Integer.toString(length.getDays())));
		} else {
			table.addCell(dataCell(" "));
			table.addCell(dataCell(" "));
			table.addCell(dataCell(" "));
		}
	}
}
