package com.chessoft.lengthofservice.ui;

import com.chessoft.lengthofservice.ui.model.Rank;
import com.chessoft.lengthofservice.ui.model.RankList;

import javax.swing.*;
import java.awt.*;

public class RankCellEditor extends DefaultCellEditor {
	JComboBox<Rank> cb;

	public RankCellEditor() {
		super(new JComboBox<Rank>());
		cb = (JComboBox<Rank>)getComponent();
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
		JComboBox<Rank> cb = (JComboBox<Rank>)getComponent();
		return cb.getSelectedItem();
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		JComboBox<Rank> cb = (JComboBox<Rank>)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		cb.removeAllItems();
		for (Rank rank : RankList.list()) {
			cb.addItem(rank);
		}
		cb.setSelectedItem(value);
		return cb;
	}
}
