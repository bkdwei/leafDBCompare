package com.eventListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UI;

@Service
public class TableColSelect implements MouseListener {

	@Autowired
	private UI ui;

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int col = ui.table.getSelectedColumn();
		String columnName = ui.table.getColumnName(col);
		ui.tfSqlCommand
				.setText("select * from " + ui.awtList.getSelectedItem() + " where " + columnName + "  like  \"%%\"");

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
