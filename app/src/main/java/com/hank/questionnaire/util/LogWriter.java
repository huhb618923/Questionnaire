package com.hank.questionnaire.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({ "SimpleDateFormat" })
public class LogWriter {
	private static LogWriter mLogWriter;
	private static SimpleDateFormat datef = new SimpleDateFormat("yy-MM-dd");
	private static Writer mWriter;
	private static SimpleDateFormat df;

	public static LogWriter open() throws IOException {
		if (mLogWriter == null) {
			mLogWriter = new LogWriter();
		}
		File logFolder = new File(Environment.getExternalStorageDirectory(), "OUDING");
		// 删除之前日期的log数据
		if(!logFolder.exists()) {
			logFolder.mkdirs();
		}
		File mFile = new File(logFolder, datef.format(new Date()) + ".txt");
		if(!mFile.exists()) {// 认为现在没有今天的log文件的时候才进行删除之前文件的方法，提高速度
			// 将之前的log文件都删除
			File[] fileList = logFolder.listFiles();
			for (int i = 0,len=fileList.length; i < len; i++) {
				fileList[i].delete();
			}
		}
		mWriter = new BufferedWriter(new FileWriter(mFile, true), 2048);
		df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]:");
		return mLogWriter;
	}

	public void close() throws IOException {
		mWriter.close();
	}

	public void print(String log) throws IOException {
		mWriter.append("\n" + df.format(new Date()));
		mWriter.append(log);
		mWriter.append("\n");
		mWriter.flush();
	}

	public void print(Class<?> cls, String methodName, String log) throws Exception {
		mWriter.append(df.format(new Date()) + "\n");
		mWriter.append(cls.getSimpleName() + "." + methodName + ":" + "\n" + log + "\n");
		mWriter.flush();
	}
}
