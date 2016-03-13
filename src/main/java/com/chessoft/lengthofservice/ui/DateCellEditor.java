package com.chessoft.lengthofservice.ui;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateCellEditor extends DefaultCellEditor {
	JFormattedTextField ftf;
	SimpleDateFormat dateFormat;
	public DateCellEditor() {
		super(new JFormattedTextField());
		ftf = (JFormattedTextField)getComponent();
		dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		DateFormatter dateFormatter = new DateFormatter(dateFormat);
		dateFormatter.setFormat(dateFormat);
		dateFormatter.setAllowsInvalid(false);
		dateFormatter.setOverwriteMode(true);

		ftf.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
		ftf.setHorizontalAlignment(JTextField.TRAILING);
		ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);
		ftf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
		ftf.getActionMap().put("check", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!ftf.isEditValid()) {
					if (userSaysRevert()) {
						ftf.postActionEvent();
					}
				} else try {
					ftf.commitEdit();
					ftf.postActionEvent();
				} catch (ParseException pe) {}
			}
		});
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		JFormattedTextField ftf = (JFormattedTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		ftf.setValue(value);
		return ftf;
	}

	@Override
	public Object getCellEditorValue() {
		JFormattedTextField ftf = (JFormattedTextField)getComponent();
		Object o = ftf.getValue();
		if (o instanceof Date) {
			return o;
		} else try {
			return o!= null ? dateFormat.parseObject(o.toString()) : null;
		} catch (ParseException pe) {
			System.out.println("Cannot parse o: "+o);
			return null;
		}
	}

	@Override
	public boolean stopCellEditing() {
		JFormattedTextField ftf = (JFormattedTextField)getComponent();
		if (ftf.isEditValid()) {
			try {
				ftf.commitEdit();
			} catch (ParseException pe) {}

		} else {
			if (!userSaysRevert()) {
				return false;
			}
		}
		return super.stopCellEditing();
	}

	protected boolean userSaysRevert() {
		Toolkit.getDefaultToolkit().beep();
		ftf.selectAll();
		Object[] options = {"Продолжить", "Отменить"};
		int answer = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(ftf), "Формат ввода: ДД.ММ.ГГГГ\nПродолжить редактирование?",
				"Неверный ввод даты", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);
		if (answer == 1) { //cancel editing and revert
			ftf.setValue(ftf.getValue());
			return true;
		}
		return false;
	}

}
