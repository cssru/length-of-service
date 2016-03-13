package com.chessoft.lengthofservice.ui;

import com.chessoft.lengthofservice.enums.PeriodType;
import com.chessoft.lengthofservice.ui.model.Rank;

import javax.swing.*;
import java.awt.*;

public class PeriodTypeCellEditor extends DefaultCellEditor {
	JComboBox<PeriodType> cb;

	public PeriodTypeCellEditor() {
		super(new JComboBox<Rank>());
		cb = (JComboBox<PeriodType>)getComponent();
	}


	@Override
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	@Override
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}

	@Override
	public Object getCellEditorValue() {
		JComboBox<PeriodType> cb = (JComboBox<PeriodType>)getComponent();
		return cb.getSelectedItem();
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		JComboBox<PeriodType> cb = (JComboBox<PeriodType>)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		cb.removeAllItems();
		for (PeriodType periodType : PeriodType.values()) {
			cb.addItem(periodType);
		}
		cb.setSelectedItem(value);
		return cb;
	}
}
