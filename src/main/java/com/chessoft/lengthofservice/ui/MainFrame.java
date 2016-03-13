package com.chessoft.lengthofservice.ui;


import com.chessoft.lengthofservice.ui.model.PeopleTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
	public MainFrame() {
		super("Расчет выслуги лет");

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		initMenu();
		initUI();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(), (int)screenSize.getHeight()-20);
		setLocation(0, 0);
		setVisible(true);
	}

	private void initMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu mFile = new JMenu("Файл");
		menubar.add(mFile);

		JMenuItem miExit = new JMenuItem("Выход");
		miExit.addActionListener((actionEvent) -> System.exit(0));
		mFile.add(miExit);
		setJMenuBar(menubar);
	}

	private void initUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JTabbedPane tabp = new JTabbedPane();

		PeoplePanel peoplePanel = new PeoplePanel();

		tabp.addTab("Список личного состава", peoplePanel);

		tabp.addChangeListener((changeEvent) -> {
			int index = tabp.getSelectedIndex();
			switch (index) {
				case 0:
					JTable peopleTable = peoplePanel.getTable();
					if (peopleTable != null) {
						PeopleTableModel ptm = (PeopleTableModel)peopleTable.getModel();
						if (ptm != null) ptm.refresh();
					}
					break;
			}
		});
		cp.add("Center", tabp);
		cp.doLayout();
	}
}
