package com.chessoft.lengthofservice.ui;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PeriodLengthCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof PeriodLength && null != value) {
			setText(Utils.periodLengthAsLongString((PeriodLength) value));
		} else setText("");

		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);

		return this;
	}
}
