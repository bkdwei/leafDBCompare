package com.eventListener;

//输入框获得焦点时的处理动作
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UI;

@Service
public class CleanDefaulText implements FocusListener {

	@Autowired
	private UI ui;

	@Override
	public void focusGained(FocusEvent arg0) {
		TextField search = (TextField) arg0.getSource();
		String inputValue = search.getText();
		if (inputValue.trim().equals("请输入您要搜索的表名")) {
			search.setText("");
		} else if (inputValue.trim().equals("请输入您要执行的命令") && ui.awtList.getSelectedItem() != null) {
			ui.tfSqlCommand.setText("select * from " + ui.awtList.getSelectedItem() + " where   like  \"%%\"");
		} else if (!(search.getText().indexOf("select") >= 0)) {
			search.select(0, search.getText().length());
		}

	}

	@Override
	public void focusLost(FocusEvent arg0) {

	}

}
