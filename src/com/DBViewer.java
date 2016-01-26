package com;

import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.DAO.DBDao;
import com.DAO.Querier;
import com.eventListener.ButtonClickHandler;
import com.eventListener.CleanDefaulText;
import com.eventListener.ItemStatusChange;
import com.eventListener.SearchTextChange;
import com.eventListener.TableColSelect;
import com.model.TableList;

@Service
public class DBViewer {

	@Autowired
	private DBDao dbDao;
	@Autowired
	private Querier querier;
	@Autowired
	private FileProcessor fileProcessor;
	@Autowired
	public UI ui;
	@Autowired
	private ItemStatusChange itemStatusChange;
	@Autowired
	private SearchTextChange searchTextChange;
	@Autowired
	private CleanDefaulText cleanDefaulText;
	@Autowired
	private ButtonClickHandler buttonClickHandler;
	@Autowired
	private TableColSelect tableColSelect;
	@Autowired
	private TableList tableList;

	public void testConnection() {
		int testResult = dbDao.testConnection();
		if (testResult == 0) {
			JOptionPane.showMessageDialog(null, "数据库连接失败，请重新配置数据库的连接信息！", "数据库连接测试", 0, null);
			ui.statusBar.setText("数据库连接失败");
		} else if (testResult == 1) {
			//JOptionPane.showMessageDialog(null, "数据库连接成功！", "数据库连接测试", 1, null);
		}
	}

	public void init() {
		// 绑定事件监听器
		ui.awtList.addItemListener(itemStatusChange);
		ui.search.addTextListener(searchTextChange);
		ui.search.addFocusListener(cleanDefaulText);
		ui.tfSqlCommand.addFocusListener(cleanDefaulText);
		ui.btDeleteTableData.addActionListener(buttonClickHandler);
		ui.btExecuteSql.addActionListener(buttonClickHandler);
		ui.btAbout.addActionListener(buttonClickHandler);
		ui.table.addMouseListener(tableColSelect);
		testConnection();
		List l = dbDao.getTables();
		for (int i = 0; i < l.size(); i++) {
			ui.awtList.add((String) l.get(i));
		}
		ui.statusBar.setText("数据库连接成功。共有" + ui.awtList.getItemCount() + "张表");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new FileSystemXmlApplicationContext("beans-config.xml");
		DBViewer dbViewer = (DBViewer) ctx.getBean("dbViewer");
		dbViewer.dbDao.testConnection();
		dbViewer.ui.init();
		dbViewer.ui.compose();

		dbViewer.init();
	/*	dbViewer.dbDao.exportDBStructure();
		dbViewer.dbDao.json2file(dbViewer.tableList);
*/		dbViewer.querier.getDBStructure();
		dbViewer.fileProcessor.readFile("/tmp/123");

	}

}
