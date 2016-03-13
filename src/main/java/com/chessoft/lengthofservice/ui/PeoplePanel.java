package com.chessoft.lengthofservice.ui;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.factory.ServiceFactory;
import com.chessoft.lengthofservice.ui.model.PeopleTableModel;
import com.chessoft.lengthofservice.utils.PdfUtils;
import com.chessoft.lengthofservice.utils.Utils;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.service.HumanService;
import com.chessoft.lengthofservice.ui.model.Rank;
import com.itextpdf.text.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class PeoplePanel extends JPanel {
	private JTable table;
	private HumanService humanService = ServiceFactory.getHumanService();

	public PeoplePanel() {
		super(new BorderLayout());
		JPanel tablePanel = new JPanel(new GridLayout(1,1));
		table = new JTable(new PeopleTableModel());
		table.setDefaultEditor(Rank.class, new RankCellEditor());
		table.setDefaultEditor(Date.class, new DateCellEditor());
		table.setDefaultRenderer(Date.class, new DateCellRenderer());
		table.setDefaultRenderer(PeriodLength.class, new PeriodLengthCellRenderer());
		table.setAutoCreateRowSorter(true);

		JScrollPane scrl = new JScrollPane(table);

		tablePanel.add(scrl);

		add("Center", tablePanel);

		JToolBar toolBar = new JToolBar();

		JButton addButton = new JButton(Resources.getImageIcon("ic_add.png")); //adding new Human
		addButton.setToolTipText("Добавить сотрудника");

		addButton.addActionListener((actionEvent) -> {
			Human human = Utils.emptyHuman();
			humanService.create(human);
			((PeopleTableModel)table.getModel()).refresh();
		});

		toolBar.add(addButton);


		JButton editButton = new JButton(Resources.getImageIcon("ic_edit.png")); //editing Human
		editButton.setToolTipText("Изменить");

		editButton.addActionListener((actionEvent) -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				selectedRow = table.convertRowIndexToModel(selectedRow);
				PeopleTableModel model = (PeopleTableModel)table.getModel();
				Human human = model.getHumanAt(selectedRow);
				Container parentFrame = PeoplePanel.this;
				while (!(parentFrame instanceof Frame) && (parentFrame != null)) parentFrame = parentFrame.getParent();
				PeriodsDialog periodsDialog;
				if (parentFrame != null) {
					periodsDialog = new PeriodsDialog((Frame)parentFrame, human);
					periodsDialog.setVisible(true);
				}
				model.refresh();
			}

		});

		toolBar.add(editButton);


		JButton delButton = new JButton(Resources.getImageIcon("ic_delete.png"));
		delButton.setToolTipText("Удалить данные человека");

		delButton.addActionListener((actionEvent) -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0 && ErrorMessage.askForDeleteHuman()) {
				selectedRow = table.convertRowIndexToModel(selectedRow);
				PeopleTableModel model = (PeopleTableModel)table.getModel();
				Human human = model.getHumanAt(selectedRow);
				humanService.delete(human.getId());
				model.refresh();
			}
		});

		toolBar.add(delButton);

		JButton pdfButton = new JButton(Resources.getImageIcon("ic_pdf.png"));
		pdfButton.setToolTipText("Просмотр в pdf");

		pdfButton.addActionListener((actionEvent) -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				selectedRow = table.convertRowIndexToModel(selectedRow);
				PeopleTableModel model = (PeopleTableModel)table.getModel();
				Human human = model.getHumanAt(selectedRow);
				try {
					PdfUtils.createPdf(human);
				} catch (IOException e) {
					ErrorMessage.ioErrorMessage();
					e.printStackTrace();
				} catch (DocumentException e) {
					ErrorMessage.pdfErrorMessage();
					e.printStackTrace();
				}
			}
		});

		toolBar.add(pdfButton);

		add("North", toolBar);
	}

	public JTable getTable() {
		return table;
	}
}
