package com.chessoft.lengthofservice.ui;

import javax.swing.*;

public class ErrorMessage {
	private ErrorMessage() {}

	public static void periodErrorMessage() {
		JOptionPane.showMessageDialog(null, "Ошибка ввода периода службы",
				"Ошибка", JOptionPane.ERROR_MESSAGE);
	}

	public static void pdfErrorMessage() {
		JOptionPane.showMessageDialog(null, "Произошла ошибка при создании pdf-файла",
				"Ошибка", JOptionPane.ERROR_MESSAGE);
	}

	public static void ioErrorMessage() {
		JOptionPane.showMessageDialog(null, "Произошла ошибка ввода-вывода",
				"Ошибка", JOptionPane.ERROR_MESSAGE);
	}

	public static boolean askForDeleteHuman() {
		Object[] options = {"Удалить", "Отмена"};
		return JOptionPane.showOptionDialog(null, "Удалить данные выбранного человека?",
				"Удаление данных человека", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]) == 0;
	}

	public static boolean askForDeletePeriod() {
		Object[] options = {"Удалить", "Отмена"};
		return JOptionPane.showOptionDialog(null, "Удалить период службы?",
				"Удаление периода службы", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]) == 0;
	}

}
