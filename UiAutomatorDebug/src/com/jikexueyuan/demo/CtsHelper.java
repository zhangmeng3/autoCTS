package com.jikexueyuan.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CtsHelper {
	/*
	 * 本类用于在CTS框架中运行uiautomator 基于Android 4.4 CTS
	 * 思路：
	 * 1.编译且复制jar包到CTS TestCase目录中
	 * 2.依据CTS框架格式创建TestCase 
	 * 3.依据CTS框架格式创建TestPlan
	 * 4.运行TestPlan
	 */
	//输入参数，改变以下参数来适配不同的类
	private String workspace="/home/zhangmeng/workspace/UiAutomatorDebug";
	private String className_FullName="com.jikexueyuan.demo.Demo1";
	private String jarName="CalculatorCaseCTS";
	private String androidId="4";
	private String ctsPath_testCase="${SDK_PATH}\\repository\\testcases\\";
	private String ctsPath_testPlan="${SDK_PATH}\\android-cts\\repository\\plans\\";
	//CTS Tools 命令路径
	private String ctsToolsPath="${SDK_PATH}\\android-cts\\tools\\";
	//ROOT SDK目录
	private String dcts_root_path="${SDK_PATH}";
	//log与result path
	private String logPath="";
	private String resultPath="";
	String fileName="";
	
	
	//运行命令
	/*
	cd ${SDK_PATH}\android-cts\tools
	java -cp ddmlib-prebuilt.jar;tradefed-prebuilt.jar;hosttestlib.jar;cts-tradefed.jar -DCTS_ROOT=${SDK_PATH} com.android.cts.tradefed.command.CtsConsole run cts --plan calculator
    */
	private String runClassName="com.android.cts.tradefed.command.CtsConsole";
	private String runPlanCmd="run cts --plan REPLAY";
	private String devices="";
	
	//结果路径保存
	private ArrayList<String> listResultPath=new ArrayList<String>();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String workspase="";
		String className="";
		String jarName="";
		String androidId="";
		String sdkPath="";
		String devices="";
        for(int i=0;i<args.length;i++){
        	if(args[i].equals("--workspase")){
        		workspase=args[i+1];
        	}else 
        	if(args[i].equals("--class_name")){
        		className=args[i+1];
        	}else 
        	if(args[i].equals("--jar_name")){
        		jarName=args[i+1];
        	}else 
        	if(args[i].equals("--android_id")){
            		androidId=args[i+1];
            }else if(args[i].equals("--sdk_path")){
            		sdkPath=args[i+1];
            }else
             if(args[i].equals("-s")){
             		devices=args[i+1];
             }
        }
        CtsHelper cts=new CtsHelper(workspase, className, jarName, androidId, sdkPath);
        cts.setDevices(devices);
        cts.runTest();
       
        
	}
	/**
	 * 运行默认参数的CTS
	 */
	public CtsHelper(){
		
	}
	
	/**
	 * 传入： 工程工作空间，class全名，jarname,androidid，SDK路径
	 * @param paramater
	 */
	public CtsHelper(String workspase,String className,String jarName,String androidId,String sdkpath){
		
		this.workspace=workspase+"\\";
		this.className_FullName=className;
		this.jarName=jarName;
		this.androidId=androidId;
		this.ctsPath_testCase=sdkpath+"\\android-cts\\repository\\testcases\\";
		this.ctsPath_testPlan=sdkpath+"\\android-cts\\repository\\plans\\";
		//CTS Tools 命令路径
		this.ctsToolsPath=sdkpath+"\\android-cts\\tools\\";
		//ROOT SDK目录
		this.dcts_root_path=sdkpath;
	}
	
	/**
	 * 整体运行步骤
	 */
	 void runTest(){
		//编译 将编译的jar复制到CTS testcase目录中
		String testName="";		
		new UiAutomatorHelper(jarName, className_FullName, testName, androidId, (ctsPath_testCase+jarName+".jar").replaceAll(";", ""));			

		//运行命令
		if(!devices.equals("")){
		execCmd(getRunCtsCmd("test"+jarName+"TestPlan")+devices);
		}else{
		execCmd(getRunCtsCmd("test"+jarName+"TestPlan"));
		}
		//输出log文件路径和结果文件路径
		 System.out.println("***************************");
	        for(String s:listResultPath){
	        	System.out.println(s);
	        }
	     System.out.println("***************************");
		
	}
	/**
	 * 需求：多个手机情况下，指定某个手机运行
	 * @param dev
	 */
	public void setDevices(String dev){
		this.devices=" -s "+dev;
	}
	
	/**
	 * 执行命令
	 * @param cmd
	 */
	private void execCmd(String cmd) {
		System.out.println("****commond: " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream in = p.getInputStream();
			InputStreamReader re = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(re);
			String info="";
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if(!info.equals("")){
				listResultPath.add(info);
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成CTS运行命令，基于Android 4.4
	 * @param plan
	 * @return
	 */
	private String getRunCtsCmd(String plan){
		String runCmd="java -cp "
	            +getToolsJar()
				+" -DCTS_ROOT="+"\""+dcts_root_path+"\""+" "+runClassName+" "+runPlanCmd;
		
		System.out.println(runCmd.replace("REPLAY", plan));
		return runCmd.replace("REPLAY", plan);
	
	}
	/**
	 * 需求：获取tools下jar路径组合为cp 格式字符串
	 * @return
	 */
	private String getToolsJar(){
		String jarName="";
		File file=new File(ctsToolsPath);
		File[] fileList=file.listFiles();
		for(int i=0;i<fileList.length;i++){
			if(fileList[i].getName().contains(".jar")){
				jarName=jarName+"\""+fileList[i].getAbsolutePath()+"\""+";";
			}
		}
		jarName=jarName.substring(0, jarName.length()-1);
		System.out.println(jarName);
		return jarName;
	}
}
