package com.litao.ttweather.tool;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;





import android.content.Context;
import android.util.Xml;

public class XmlUtils {
	public static void createXml(Context context) {
		// xml�ļ������л�������xml�ļ�
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(context.getFilesDir(), "insurancetest.xml");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			
			// ��ʼ��xml�ļ����л���
			serializer.setOutput(fos, "utf-8");
			serializer.startDocument("utf-8", true);//��ʼ
			    //��ʼд��һ��Tag
				serializer.startTag(null,"policys");	
					serializer.startTag(null,"policy");
						
						serializer.startTag(null,"base");
						serializer.startTag(null,"plyappno");
						serializer.text("lyf"+System.currentTimeMillis()); //��ˮ���ظ��ͻᱨ�������쳣 
						serializer.endTag(null,"plyappno");
						serializer.startTag(null,"amt");
						serializer.text("1000000.00");
						serializer.endTag(null,"amt");
						serializer.startTag(null,"currency");
						serializer.text("CNY");
						serializer.endTag(null,"currency");
						serializer.startTag(null,"sametoinsrnt");
						serializer.text("0");
						serializer.endTag(null,"sametoinsrnt");
						serializer.startTag(null,"benfmode");
						serializer.text("1");
						serializer.endTag(null,"benfmode");
						serializer.startTag(null,"benfcname");
						serializer.text("����");
						serializer.endTag(null,"benfcname");
						serializer.endTag(null,"base");
						
						
						serializer.startTag(null,"insured");
						serializer.startTag(null,"insuredcname");
						serializer.text("����");
						serializer.endTag(null,"insuredcname");
						serializer.startTag(null,"insuredcerttype");
						serializer.text("01");
						serializer.endTag(null,"insuredcerttype");
						serializer.startTag(null,"insuredcertno");
						serializer.text("430524199308081775");
						serializer.endTag(null,"insuredcertno");
						serializer.startTag(null,"insuredmobilephone");
						serializer.text("13480871111");
						serializer.endTag(null,"insuredmobilephone");
						serializer.startTag(null,"insuredemail");
						serializer.text("505364390@qq.com");
						serializer.endTag(null,"insuredemail");
						serializer.endTag(null,"insured");
						
						
						serializer.startTag(null,"insrnt_list");
						serializer.startTag(null,"insrntcname");
						serializer.text("����");
						serializer.endTag(null,"insrntcname");
						serializer.startTag(null,"insrntcerttype");
						serializer.text("01");
						serializer.endTag(null,"insrntcerttype");
						serializer.startTag(null,"insrntcertno");
						serializer.text("430524199308081775");
						serializer.endTag(null,"insrntcertno");
						serializer.startTag(null,"insrntmobilephone");
						serializer.text("133373322");
						serializer.endTag(null,"insrntmobilephone");
						serializer.startTag(null,"insrntemail");
						serializer.text("505364390@qq.com");
						serializer.endTag(null,"insrntemail");
						serializer.endTag(null,"insrnt_list");
						
						
						serializer.startTag(null, "conveyance_list");
						serializer.startTag(null,"conveyance");
						serializer.startTag(null,"conveyancetypename");
						serializer.text("10");
						serializer.endTag(null,"conveyancetypename");
						serializer.startTag(null,"conveyancename");
						serializer.text("�ɻ�");
						serializer.endTag(null,"conveyancename");
						serializer.startTag(null,"startsitename");
						serializer.text("����");
						serializer.endTag(null,"startsitename");
						serializer.startTag(null,"viasitename");
						serializer.text("����");
						serializer.endTag(null,"viasitename");
						serializer.startTag(null,"targetsitename");
						serializer.text("����");
						serializer.endTag(null,"targetsitename");
						serializer.startTag(null,"voyageno");
						serializer.text("m8098");
						serializer.endTag(null,"voyageno");
						serializer.startTag(null,"startdate");
						serializer.text("2015-09-19");
						serializer.endTag(null,"startdate");
						serializer.startTag(null,"claimat");
						serializer.text("����");
						serializer.endTag(null,"claimat");
						serializer.startTag(null,"blno");
						serializer.text("567843454");
						serializer.endTag(null,"blno");
						serializer.startTag(null,"lcno");
						serializer.text("1234");
						serializer.endTag(null,"lcno");
						serializer.startTag(null,"bnak");
						serializer.text("�й�����");
						serializer.endTag(null,"bnak");
						serializer.startTag(null,"allbelongflag");
						serializer.text("2");
						serializer.endTag(null,"allbelongflag");
						serializer.startTag(null,"insuredplan");
						serializer.text("1");
						serializer.endTag(null,"insuredplan");
						serializer.endTag(null,"conveyance");
						serializer.endTag(null, "conveyance_list");
						
						
						serializer.startTag(null, "item_list");
						serializer.startTag(null,"itemdetail");
						serializer.startTag(null,"itemno");
						serializer.text("01");
						serializer.endTag(null,"itemno");
						serializer.startTag(null,"quantity");
						serializer.text("20");
						serializer.endTag(null,"quantity");
						serializer.startTag(null,"packing");
						serializer.text("̨");
						serializer.endTag(null,"packing");
						serializer.startTag(null,"itemdetaillist");
						serializer.text("����");
						serializer.endTag(null,"itemdetaillist");
						serializer.startTag(null,"marks");
						serializer.text("123456");
						serializer.endTag(null,"marks");
						serializer.startTag(null,"billno");
						serializer.text("851421");
						serializer.endTag(null,"billno");
						serializer.endTag(null,"itemdetail");
						serializer.endTag(null, "item_list");
					serializer.endTag(null,"policy");
				serializer.endTag(null,"policys");
			serializer.endDocument();//����
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	

/*	// xml�ļ������л�������xml�ļ�
	XmlSerializer serializer = Xml.newSerializer();
	File file = new File(context.getFilesDir(), "insurancetest.xml");
	try {
		FileOutputStream fos = new FileOutputStream(file);
		// ��ʼ��xml�ļ����л���
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);//��ʼ
		    //��ʼд��һ��Tag
			serializer.startTag(null,"POLICYS");	
				serializer.startTag(null,"POLICY");
					
					serializer.startTag(null,"BASE");
					serializer.startTag(null,"PLYAPPNO");
					serializer.text("hyx201508181641");
					serializer.endTag(null,"PLYAPPNO");
					serializer.startTag(null,"AMT");
					serializer.text("1000000.00");
					serializer.endTag(null,"AMT");
					serializer.startTag(null,"CURRENCY");
					serializer.text("CNY");
					serializer.endTag(null,"CURRENCY");
					serializer.startTag(null,"SAMETOINSRNT");
					serializer.text("0");
					serializer.endTag(null,"SAMETOINSRNT");
					serializer.startTag(null,"BENFMODE");
					serializer.text("1");
					serializer.endTag(null,"BENFMODE");
					serializer.startTag(null,"BENFCNAME");
					serializer.text("����");
					serializer.endTag(null,"BENFCNAME");
					serializer.endTag(null,"BASE");
					
					
					serializer.startTag(null,"INSURED");
					serializer.startTag(null,"INSUREDCNAME");
					serializer.text("����");
					serializer.endTag(null,"INSUREDCNAME");
					serializer.startTag(null,"INSUREDCERTTYPE");
					serializer.text("01");
					serializer.endTag(null,"INSUREDCERTTYPE");
					serializer.startTag(null,"INSUREDCERTNO");
					serializer.text("440881198XXXXXX");
					serializer.endTag(null,"INSUREDCERTNO");
					serializer.startTag(null,"INSUREDMOBILEPHONE");
					serializer.text("13480871111");
					serializer.endTag(null,"INSUREDMOBILEPHONE");
					serializer.startTag(null,"INSUREDEMAIL");
					serializer.text("34234@qq.com");
					serializer.endTag(null,"INSUREDEMAIL");
					serializer.endTag(null,"INSURED");
					
					
					serializer.startTag(null,"INSRNT_LIST");
					serializer.startTag(null,"INSRNTCNAME");
					serializer.text("����");
					serializer.endTag(null,"INSRNTCNAME");
					serializer.startTag(null,"INSRNTCERTTYPE");
					serializer.text("01");
					serializer.endTag(null,"INSRNTCERTTYPE");
					serializer.startTag(null,"INSRNTCERTNO");
					serializer.text("4408888343");
					serializer.endTag(null,"INSRNTCERTNO");
					serializer.startTag(null,"INSRNTMOBILEPHONE");
					serializer.text("133373322");
					serializer.endTag(null,"INSRNTMOBILEPHONE");
					serializer.startTag(null,"INSRNTEMAIL");
					serializer.text("34234@qq.com");
					serializer.endTag(null,"INSRNTEMAIL");
					serializer.endTag(null,"INSRNT_LIST");
					
					
					serializer.startTag(null, "CONVEYANCE_LIST");
					serializer.startTag(null,"CONVEYANCE");
					serializer.startTag(null,"CONVEYANCETYPENAME");
					serializer.text("10");
					serializer.endTag(null,"CONVEYANCETYPENAME");
					serializer.startTag(null,"CONVEYANCENAME");
					serializer.text("�ɻ�");
					serializer.endTag(null,"CONVEYANCENAME");
					serializer.startTag(null,"STARTSITENAME");
					serializer.text("����");
					serializer.endTag(null,"STARTSITENAME");
					serializer.startTag(null,"VIASITENAME");
					serializer.text("����");
					serializer.endTag(null,"VIASITENAME");
					serializer.startTag(null,"TARGETSITENAME");
					serializer.text("����");
					serializer.endTag(null,"TARGETSITENAME");
					serializer.startTag(null,"VOYAGENO");
					serializer.text("m8098");
					serializer.endTag(null,"VOYAGENO");
					serializer.startTag(null,"STARTDATE");
					serializer.text("2015-09-19");
					serializer.endTag(null,"STARTDATE");
					serializer.startTag(null,"CLAIMAT");
					serializer.text("����");
					serializer.endTag(null,"CLAIMAT");
					serializer.startTag(null,"BLNO");
					serializer.text("567843454");
					serializer.endTag(null,"BLNO");
					serializer.startTag(null,"LCNO");
					serializer.text("1234");
					serializer.endTag(null,"LCNO");
					serializer.startTag(null,"BNAK");
					serializer.text("�й�����");
					serializer.endTag(null,"BNAK");
					serializer.startTag(null,"ALLBELONGFLAG");
					serializer.text("2");
					serializer.endTag(null,"ALLBELONGFLAG");
					serializer.startTag(null,"INSUREDPLAN");
					serializer.text("1");
					serializer.endTag(null,"INSUREDPLAN");
					serializer.endTag(null,"CONVEYANCE");
					serializer.endTag(null, "CONVEYANCE_LIST");
					
					
					serializer.startTag(null, "ITEM_LIST");
					serializer.startTag(null,"ITEMDETAIL");
					serializer.startTag(null,"ITEMNO");
					serializer.text("01");
					serializer.endTag(null,"ITEMNO");
					serializer.startTag(null,"QUANTITY");
					serializer.text("20");
					serializer.endTag(null,"QUANTITY");
					serializer.startTag(null,"PACKING");
					serializer.text("̨");
					serializer.endTag(null,"PACKING");
					serializer.startTag(null,"ITEMDETAILLIST");
					serializer.text("����");
					serializer.endTag(null,"ITEMDETAILLIST");
					serializer.startTag(null,"MARKS");
					serializer.text("123456");
					serializer.endTag(null,"MARKS");
					serializer.startTag(null,"BILLNO ");
					serializer.text("851421");
					serializer.endTag(null,"BILLNO ");
					serializer.endTag(null,"ITEMDETAIL");
					serializer.endTag(null, "ITEM_LIST");
				serializer.endTag(null,"POLICY");
			serializer.endTag(null,"POLICYS");
		serializer.endDocument();//����
		fos.close();
	} catch (Exception e) {
		e.printStackTrace();
	}*/


	
	public static void createXml2(Context context){
		
		// xml�ļ������л�������xml�ļ�
				XmlSerializer serializer = Xml.newSerializer();
				File file = new File(context.getFilesDir(), "insurance2.xml");
				try {
					FileOutputStream fos=new FileOutputStream(file);
					// ��ʼ��xml�ļ����л���
					serializer.setOutput(fos, "utf-8");
					
					serializer.startDocument("utf-8", true);//��ʼ
						//��ʼд��һ��Tag
						serializer.startTag(null,"POLICYS");	
						serializer.startTag(null,"POLICY");						
						serializer.startTag(null,"PLYAPPNO");
						serializer.text("1055001");
						serializer.endTag(null,"PLYAPPNO");
						serializer.startTag(null,"PROPOSALNO");
						serializer.text("06020100270311462012000296");
						serializer.endTag(null,"PROPOSALNO");
						serializer.startTag(null,"POLICYNO");
						serializer.text("06020100270311462012000296");
						serializer.endTag(null,"POLICYNO");
						serializer.startTag(null,"APPTIME");
						serializer.text("2010-01-09");
						serializer.endTag(null,"APPTIME");
						serializer.startTag(null,"RESULT");
						serializer.text("0000");
						serializer.endTag(null,"RESULT");
						serializer.startTag(null,"INFO");
						serializer.text("06020100270311462010011");
						serializer.endTag(null,"INFO");
						serializer.endTag(null,"POLICY");
						serializer.endTag(null,"POLICYS");
					serializer.endDocument();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	
	
	
}
