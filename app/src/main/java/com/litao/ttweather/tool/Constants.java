package com.litao.ttweather.tool;

import java.io.File;


import android.net.Uri;
import android.os.Environment;

public class Constants {
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int PHOTOZOOM = 1;
	public static String PHOTO_HEAD="head_photo.png";
	public static String PHOTO_PATH_ROOT="";
	public static String CACHE_DIR="";
	public static final String CITY_DATA = "china_city_data.json";
	public static final int PAGESIZE=8;//һҳ���ص�����
	public static final String CACHE_FILE_NAME = "sima";// sp�洢������
	public static final String IS_FISRT_LOGIN="is_frist_login";//�Ƿ��״ν������
	public static final String IS_LOGIN="islogin";//�Ƿ��¼��
	public static final String IDENTITY="identity";//��¼�����
	public static final int LOAD_IMAGE_SUCCESS = 0;// ����ͼƬ�ɹ�
	public static final int LOAD_IMAGE_FAILED = 1;// ����ͼƬʧ��

	public static String PHOTONAME_HEAD="head_photo";
	public static String PHOTONAME_IDCARD="idcard_photo";
	public static String PHOTONAME_DRIVECARD="drivecard_photo";
	public static String PHOTONAME_CARCARD="carcard_photo";
	public static String PHOTONAME="photo";
	public static Uri imageUri = Uri.fromFile(new File(Environment
			.getExternalStorageDirectory(), "image.jpg"));
	
//	public static final String URL_PATH="http://ggwdemo.wicp.net:6666/LogisticsManage/";//����·��
	public static final String URL_PATH="http://www.weather.com.cn/weather/";//����·��
	public static final String URL_PATH_PHOTO="http://120.25.218.50/LogisticsManage";//����·��
	public static final String VERSION_KEY="User-Agent";//����ͷ��key
	public static final String VERSION_VALUE="AppBuilder_android/";//����ͷ��ֵ
	public static final String URL_REGISTER=URL_PATH+"User_register.action";//ע���URL
	public static final String URL_LOGIN=URL_PATH+"User_login.action";//��¼��URL
	public static final String URL_EDIT_INFO=URL_PATH+"User_updInfo.action";//�޸Ļ�Ա���ϵ�URL
	public static final String URL_EDIT_PHOTO=URL_PATH+"User_updImg.action";//�޸���Ƭ��·��
	public static final String URL_EDIT_UPDPORTRAIT=URL_PATH+"User_updPortrait.action";//�޸���Ƭ��·��
	public static final String URL_FIND_INFO=URL_PATH+"User_findById.action";//��ѯ��������

	public static final String URL_MANAGER_UPDATE = URL_PATH+"CarManage_updItem.action";//�����޸�
	public static final String URL_MANAGER_ADD=URL_PATH+"CarManage_addItem.action";//�������
	public static final String URL_CAR_INFO=URL_PATH+"CarManage_listItem.action";//������Ϣ�б�
	public static final String URL_CAR_DELETE=URL_PATH+"CarManage_delItem.action";//������Ϣɾ��
	public static final String URL_PEOPLE_DELETE=URL_PATH+"Consignee_delItem.action";//�ջ�����Ϣɾ��
	public static final String URL_PEOPLE_INFO=URL_PATH+"Consignee_listItem.action";//�ջ�����Ϣ�б�
	public static final String URL_PEOPLE_ADD=URL_PATH+"Consignee_addItem.action";//�ջ�����Ϣ���
	public static final String URL_PEOPLE_UPD=URL_PATH+"Consignee_updItem.action";//�ջ�����Ϣ�޸ļ�
	
	
}
