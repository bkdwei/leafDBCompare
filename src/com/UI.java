package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.List;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.springframework.stereotype.Component;

@Component
public class UI {
	public JFrame frame;

	// 主窗口包含工具栏、数据区、状态栏

	public JPanel toolBar;
	public JLabel statusBar;

	// 工具栏元素
	public JButton btDeleteTableData;
	public TextField tfSqlCommand;
	public JButton btExecuteSql;
	public JButton btAbout;

	// 数据区左侧是搜索框和数据库的表，右侧是表的数据展示区
	public JPanel westJPanel;
	public TextField search;
	public List awtList;
	// 给table添加滚动动条
	public JScrollPane scrollPane;
	public JTable table;

	// 创建UI元素
	public void init() {
		frame = new JFrame("数据库查看器");

		toolBar = new JPanel();
		statusBar = new JLabel("状态栏");

		btDeleteTableData = new JButton("删除本表数据");
		tfSqlCommand = new TextField("请输入您要执行的命令", 50);
		btExecuteSql = new JButton("执行查询");
		
		btAbout = new JButton("关于");

		westJPanel = new JPanel();
		search = new TextField("请输入您要搜索的表名", 25);
		awtList = new List(20);
		scrollPane = new JScrollPane();
		table = new JTable(50, 30);
	}

	// 设置UI元素的属性
	public void compose() {
		frame.setSize(954, 750);
		frame.setLocation(200, 50);
		frame.setResizable(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// 工具栏
		toolBar.add(btDeleteTableData);
		toolBar.add(tfSqlCommand);
		toolBar.add(btExecuteSql);
		toolBar.add(btAbout);
		frame.add("North", toolBar);

		// 数据管理层

		westJPanel.setLayout(new BorderLayout());
		westJPanel.add("North", search);
		westJPanel.add("Center", awtList);
		frame.add("West", westJPanel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(true);
		table.setAutoscrolls(true);
		table.setDragEnabled(true);
		table.setCellSelectionEnabled(true);
		table.setSelectionBackground(new Color(233, 223, 233));
		scrollPane.setViewportView(table);

		frame.add("Center", scrollPane);

		statusBar.setAlignmentX(JLabel.RIGHT);
		frame.add("South", statusBar);

		frame.setVisible(true);

	}
}
