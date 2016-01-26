package com.eventListener;

import java.awt.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UI;
import com.DAO.DBDao;

/*
 * 选中列表中的某种表时，变更右边的数据和状态栏
 */
@Service
public class ItemStatusChange implements ItemListener {

	@Autowired
	private UI ui;
	@Autowired
	private DBDao dbDao;

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		List l = (List) arg0.getItemSelectable();
		String tableName = l.getSelectedItem();
		ui.statusBar.setText(tableName);
		DefaultTableModel tm = dbDao.getAllTableData(tableName);

		if (tm != null) {
			ui.table.setModel(tm);
			ui.statusBar.setText(tableName + "：共" + (tm.getRowCount() - 1) + "条数据");
		}
		ui.tfSqlCommand.setText("select * from " + ui.awtList.getSelectedItem() + " where   like  \"%%\"");
	}
}
