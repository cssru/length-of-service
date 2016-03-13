package com.chessoft.lengthofservice.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof Date && null != value) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
			setText(df.format((Date)value));
		} else setText("");

		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);

		return this;
	}
}
