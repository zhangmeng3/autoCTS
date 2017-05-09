package com.jikexueyuan.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class UiAutomatorHelper {

	// 以下参数需要配置，用例集id，用例id，安卓id
	private static String android_id = "3";
	private static String jar_name = "";
	private static String test_class = "";
	private static String test_name = "";

	// 工作空间不需要配置，自动获取工作空间目录
	private static String workspace_path;

    public static void main(String[] args) {
		
	}
	public UiAutomatorHelper() {
		workspace_path = getWorkSpase();
		System.out.println("---工作空间：\t\n" + getWorkSpase());
	}

	/**
	 * 需求：UI工程调试构造器，输入jar包名，包名，类名，用例名
	 * @param jarName
	 * @param testClass
	 * @param testName
	 * @param androidId
	 */
	public UiAutomatorHelper(String jarName, String testClass, String testName,
			String androidId) {
		System.out.println("-----------start--uiautomator--debug-------------");
		workspace_path = getWorkSpase();
		System.out.println("----工作空间：\t\n" + getWorkSpase());

		jar_name = jarName;
		test_class = testClass;
		test_name = testName;
		android_id = androidId;
		System.out.println("*******************");
		System.out.println("---FINISH DEBUG----");
		System.out.println("*******************");
	}
	/**
	 * 需求：build 和 复制jar到指定目录
	 * @param jarName
	 * @param testClass
	 * @param testName
	 * @param androidId
	 * @param isRun
	 */
	public UiAutomatorHelper(String jarName, String testClass, String testName,
			String androidId,String ctsTestCasePath){
		System.out.println("-----------start--uiautomator--debug-------------");
		workspace_path = getWorkSpase();
		System.out.println("----工作空间：\t\n" + getWorkSpase());

		jar_name = jarName;
		test_class = testClass;
		test_name = testName;
		android_id = androidId;
		
		System.out.println("*******************");
		System.out.println("---FINISH DEBUG----");
		System.out.println("*******************");
		
	}

	// 运行测试
	public void runTest(String jarName, String testName) {
		String runCmd = "adb shell uiautomator runtest ";
		String testCmd = jarName + ".jar " + "--nohup -c " + testName;
		System.out.println("----runTest:  " + runCmd + testCmd);
		execCmd(runCmd + testCmd);
	}

	public String getWorkSpase() {
		File directory = new File("");
		String abPath = directory.getAbsolutePath();
		return abPath;
	}
	
	/**
	 * 需求：执行cmd命令，且输出信息到控制台
	 * @param cmd
	 */
	public void execCmd(String cmd) {
		System.out.println("----execCmd:  " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			//正确输出流
			InputStream input = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			//错误输出流
			InputStream errorInput = p.getErrorStream();
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(
					errorInput));
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {
				System.out.println(eline);
			}       
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}