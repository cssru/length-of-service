package com.chessoft.lengthofservice.ui;

import com.chessoft.lengthofservice.PeriodLength;
import com.chessoft.lengthofservice.factory.ServiceFactory;
import com.chessoft.lengthofservice.utils.Utils;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.enums.PeriodType;
import com.chessoft.lengthofservice.service.HumanService;
import com.chessoft.lengthofservice.ui.model.PeriodTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PeriodsDialog extends JDialog {
	private Human human;
	private JTable periodTable;
	private HumanService humanService = ServiceFactory.getHumanService();

	public PeriodsDialog(Frame owner, Human human) {
		super(owner, "Периоды службы: " + Utils.getFullName(human), true);
		this.human = human;
		initUI();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)(screenSize.getWidth()*0.8), (int)(screenSize.getHeight()*0.8));
		setLocation(0, 0);
	}

	private void initUI() {
		Container mainCp = getContentPane();
		mainCp.setLayout(new BorderLayout());

		JPanel tablePanel = new JPanel(new BorderLayout());
		JToolBar tableToolBar = new JToolBar();

		JButton tableAddButton = new JButton(Resources.getImageIcon("ic_add.png")); //adding new Human
		tableAddButton.setToolTipText("Добавить период");

		tableAddButton.addActionListener(actionEvent -> {
			Period newPeriod = new Period();
			newPeriod.setType(PeriodType.NORMAL);
			Period lastHumansPeriod = Utils.findLast(human);
			newPeriod.setBegin(lastHumansPeriod == null
					?
					Utils.clear(new Date())
					:
					lastHumansPeriod.getEnd());
			newPeriod.setEnd(Utils.nextDay(newPeriod.getBegin()));
			humanService.addPeriod(human.getId(), newPeriod);
			human = humanService.read(human.getId());
			PeriodTableModel tableModel = (PeriodTableModel)periodTable.getModel();
			tableModel.setHuman(human);
		});

		tableToolBar.add(tableAddButton);

		JButton tableDelButton = new JButton(Resources.getImageIcon("ic_delete.png"));
		tableDelButton.setToolTipText("Удалить период");

		tableDelButton.addActionListener(actionEvent -> {
			int selectedRow = periodTable.getSelectedRow();
			if (selectedRow >= 0 && ErrorMessage.askForDeletePeriod()) {
				selectedRow = periodTable.convertRowIndexToModel(selectedRow);
				PeriodTableModel model = (PeriodTableModel)periodTable.getModel();
				model.deletePeriodAt(selectedRow);
				human = humanService.read(human.getId());
			}
		});

		tableToolBar.add(tableDelButton);
		tablePanel.add("North", tableToolBar);

		periodTable = new JTable(new PeriodTableModel(human));
		periodTable.setDefaultEditor(Date.class, new DateCellEditor());
		periodTable.setDefaultEditor(PeriodType.class, new PeriodTypeCellEditor());
		periodTable.setDefaultRenderer(Date.class, new DateCellRenderer());
		periodTable.setDefaultRenderer(PeriodLength.class, new PeriodLengthCellRenderer());
		periodTable.setAutoCreateRowSorter(true);


		tablePanel.add("Center", new JScrollPane(periodTable));
		add("Center", tablePanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton bClose = new JButton("Закрыть");
		buttonPanel.add(bClose);
		mainCp.add("South", buttonPanel);

		bClose.addActionListener(actionEvent -> setVisible(false));
	}
}
