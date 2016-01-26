package com.eventListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.UI;
import com.DAO.DBDao;

@Service
public class ButtonClickHandler implements ActionListener {
	@Autowired
	private UI ui;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DBDao dbDao;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String cmd = (String) arg0.getActionCommand();
		String tableName = ui.awtList.getSelectedItem();
		switch (cmd) {
		case "执行查询":
			String SQLCmd = ui.tfSqlCommand.getText().trim();
			ui.statusBar.setText("成功执行命令：" + SQLCmd);
			ui.table.setModel(dbDao.getSomeTableDataByCondition(tableName,SQLCmd));
			break;
		case "删除本表数据":
			dbDao.deleteTableData(tableName);
			ui.statusBar.setText("成功删除" + tableName + "的数据");
			ui.table.setModel(dbDao.getAllTableData(tableName));
			break;
		case "关于":
			JOptionPane.showMessageDialog(null, "版本：v1.2\n作者：bkd\n联系方式：bkdwei@bkdwei.com\n更新日期：2016-01-09\n", "关于	",
					1, null);
			break;
		default:
			ui.statusBar.setText("无任何操作可执行");
		}
	}
}
