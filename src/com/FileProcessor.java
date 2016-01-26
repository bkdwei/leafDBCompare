package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.model.Table;
import com.model.TableList;

@Service
public class FileProcessor {
	private String fileName;
	private String fileContent;
	private String filePath;

	public String readFile(String fileName) {

		File file = new File(fileName);
		BufferedReader reader = null;
		String fileContent = "";
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				fileContent += tempString;
				//System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return fileContent;
		
	}

	public void json2file(TableList tl) {
		String tableStructureInfo = JSON.toJSONString(tl, true);
//		System.out.println(tableStructureInfo);
//		System.out.println(fileContent);
		TableList tl1 = JSON.parseObject(tableStructureInfo, TableList.class);
		List ll =tl1.getTables();
		Object tt =(Object) ll.get(2);
		System.out.println(tt.toString());
		
		File fTableStructureInfo = new File("/tmp/123");
		if (!fTableStructureInfo.exists()) {
			try {
				fTableStructureInfo.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter(fTableStructureInfo);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(tableStructureInfo);
			System.out.println("写入数据库信息成功！");
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
