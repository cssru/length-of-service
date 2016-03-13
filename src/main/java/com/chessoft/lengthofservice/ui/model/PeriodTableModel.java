package com.chessoft.lengthofservice.ui.model;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.enums.PeriodType;
import com.chessoft.lengthofservice.factory.ServiceFactory;
import com.chessoft.lengthofservice.service.HumanService;
import com.chessoft.lengthofservice.ui.ErrorMessage;
import com.chessoft.lengthofservice.utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.util.Date;

public class PeriodTableModel extends AbstractTableModel {
	private static final String[] columnNames = {"Тип периода", "Начало", "Окончание", "Выслуга в календарях", "Льготная выслуга", "Примечание"};
	private static final int TYPE_COLUMN = 0;
	private static final int BEGIN_COLUMN = 1;
	private static final int END_COLUMN = 2;
	private static final int CALENDAR_SERVICE_COLUMN = 3;
	private static final int TOTAL_SERVICE_COLUMN = 4;
	private static final int NOTE_COLUMN = 5;

	private Human human;
	private HumanService humanService;

	public PeriodTableModel(Human human) {
		super();
		this.human = human;
		humanService = ServiceFactory.getHumanService();
	}

	@Override
	public Class<?> getColumnClass(int index) {
		switch (index) {
			case TYPE_COLUMN: return PeriodType.class;
			case BEGIN_COLUMN:
			case END_COLUMN:
				return Date.class;
			case CALENDAR_SERVICE_COLUMN:
			case TOTAL_SERVICE_COLUMN: return PeriodLength.class;
			case NOTE_COLUMN: return String.class;
			default: return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int index) {
		return columnNames[index];
	}

	@Override
	public int getRowCount() {
		return human.getPeriods().size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Period period = human.getPeriods().get(row);
		switch (column) {
			case 0: return period.getType();
			case 1: return period.getBegin();
			case 2: return period.getEnd();
			case 3: return PeriodLength.create(period, false);
			case 4: return PeriodLength.create(period, true);
			case 5: return period.getComment();
		}
		return "TEST";
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == CALENDAR_SERVICE_COLUMN || column == TOTAL_SERVICE_COLUMN) return false;
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Period period = human.getPeriods().get(row);
		Period testPeriod = new Period();
		testPeriod.setId(period.getId());
		testPeriod.setBegin(period.getBegin());
		testPeriod.setEnd(period.getEnd());

		try {
			switch (column) {
				case TYPE_COLUMN:
					period.setType((PeriodType) value);
					break;
				case BEGIN_COLUMN:
					testPeriod.setBegin((Date) value);
					break;
				case END_COLUMN:
					testPeriod.setEnd((Date) value);
					break;
				case NOTE_COLUMN:
					period.setComment(value.toString());
					break;
			}
			Utils.checkPeriod(testPeriod);
			Utils.checkPeriodIntersection(testPeriod, human.getPeriods());

			period.setBegin(testPeriod.getBegin());
			period.setEnd(testPeriod.getEnd());
			humanService.update(human);
			fireTableRowsUpdated(row, row);
		} catch (IllegalArgumentException exception) {
			System.out.println(exception);
			ErrorMessage.periodErrorMessage();
		}
	}

	public void deletePeriodAt(int index) {
		Period period = human.getPeriods().get(index);
		humanService.removePeriod(human.getId(), period);
		human = humanService.read(human.getId());
		fireTableDataChanged();
	}

	public void setHuman(Human human) {
		this.human = human;
		fireTableDataChanged();
	}
}
