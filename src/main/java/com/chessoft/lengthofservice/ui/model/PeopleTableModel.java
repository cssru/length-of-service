package com.chessoft.lengthofservice.ui.model;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.factory.ServiceFactory;
import com.chessoft.lengthofservice.utils.Utils;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.service.HumanService;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class PeopleTableModel extends AbstractTableModel {
	private static final String[] columnNames = {"Воинское звание", "Фамилия", "Имя", "Отчество", "Дата рождения", "Возраст", "Выслуга в календарях", "Льготная выслуга", "Личный номер", "Примечание"};
	private static final int RANK_COLUMN = 0;
	private static final int SURNAME_COLUMN = 1;
	private static final int NAME_COLUMN = 2;
	private static final int LASTNAME_COLUMN = 3;
	private static final int BIRTHDAY_COLUMN = 4;
	private static final int AGE_COLUMN = 5;
	private static final int CALENDAR_SERVICE_COLUMN = 6;
	private static final int TOTAL_SERVICE_COLUMN = 7;
	private static final int PERSONAL_NUMBER_COLUMN = 8;
	private static final int NOTE_COLUMN = 9;

	private List<Human> people;
	private HumanService humanService;

	public PeopleTableModel() {
		super();
		humanService = ServiceFactory.getHumanService();
		people = humanService.list();
	}

	public void refresh() {
		people = humanService.list();
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int index) {
		switch (index) {
			case RANK_COLUMN: return Rank.class;
			case BIRTHDAY_COLUMN: return Date.class;
			case AGE_COLUMN: return Age.class;
			case CALENDAR_SERVICE_COLUMN:
			case TOTAL_SERVICE_COLUMN: return PeriodLength.class;
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
		return people.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Human human = people.get(row);
		switch (column) {
			case RANK_COLUMN: return RankList.get(human.getRank());
			case SURNAME_COLUMN: return human.getSurname();
			case NAME_COLUMN: return human.getName();
			case LASTNAME_COLUMN: return human.getLastname();
			case BIRTHDAY_COLUMN: return human.getBirthday();
			case AGE_COLUMN: return Utils.getAgeAsString(new Age(human.getBirthday()));
			case CALENDAR_SERVICE_COLUMN: return Utils.servicePeriodLength(human, false);
			case TOTAL_SERVICE_COLUMN: return Utils.servicePeriodLength(human, true);
			case PERSONAL_NUMBER_COLUMN : return human.getPersonalNumber();
			case NOTE_COLUMN: return human.getNote();
		}
		return "TEST";
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
			case AGE_COLUMN:
			case CALENDAR_SERVICE_COLUMN:
			case TOTAL_SERVICE_COLUMN: return false;
			default: return true;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Human human = people.get(row);
		switch (column) {
			case RANK_COLUMN: human.setRank(((Rank) value).getRankId()); break;
			case SURNAME_COLUMN: human.setSurname(value.toString()); break;
			case NAME_COLUMN: human.setName(value.toString()); break;
			case LASTNAME_COLUMN: human.setLastname(value.toString());
				break;
			case BIRTHDAY_COLUMN: human.setBirthday((Date)value); break;
			case PERSONAL_NUMBER_COLUMN : human.setPersonalNumber(value.toString()); break;
			case NOTE_COLUMN: human.setNote(value.toString()); break;
		}
		humanService.update(human);
		fireTableRowsUpdated(row,row);
	}

	public Human getHumanAt(int index) {
		return people.get(index);
	}
}
