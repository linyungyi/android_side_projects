#include "com_my_control_MyClass.h"
#include <string.h>
#include <jni.h>
#include "Revision.h"
#include <fcntl.h> ///< open() read() close() write()
/// for output the debug log message
#include <android/log.h>
#define  LOG_TAG    "[MY-JNI]"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  DEVICE_NAME  "/dev/my_ts1"  //device point

typedef struct TouchDeviceIoPinSt
{
  	int bRegData;   ///< hine
	char regName[10];
	char pinName[10];
}TouchDeviceIoPinType;



int fd;
jstring dev_name;
int bModelId = "";

jstring ByteDataToPinAxisNameGet(int bData)
{  
  jstring str;
  int i;

	//LOGE("[MY] ByteDataToPinAxisNameGet() model_id = %02x\n",bModelId); ///<  Print Debug message


  str = "";
  return str;
}


jstring Java_com_my_control_MyClass_hello( JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "Hello Albert"); ///< Print Debug message
}

jstring Java_com_my_control_MyClass_tracename( JNIEnv*  env, jobject  thiz,int mode,  int buf )
{
  bModelId = mode;
  return (*env)->NewStringUTF(env, ByteDataToPinAxisNameGet(buf));
}

jint Java_com_my_control_MyClass_init( JNIEnv*  env , jobject  thiz, jstring  jstr)
{
	const char *pDevName;
	int  length   =   (*env)->GetStringLength(env, jstr);



	LOGI("myClass_Init() /n");

	if(length == 0)
	{
		dev_name = DEVICE_NAME;
	}
	else
	{
		dev_name = jstr;
	}

	pDevName = (*env)->GetStringUTFChars(env, dev_name, 0);
	fd = open(pDevName, O_RDWR);

	LOGI("myClass_Init()-> fd = %d  /n",fd);
	if(fd == -1)
	{
		LOGE("open device %s error /n ",pDevName); ///<  Print Debug message
		return 0;
	}
	else
	{
		return 1;
	}
}
 
jint Java_com_my_control_MyClass_ioctl( JNIEnv*  env, jobject  thiz, jint ctl_code, jbyteArray buf )
{
	int ret = 0;
	int code = ctl_code;
	jboolean copy;

	jbyte* buffer = (*env)->GetByteArrayElements(env, buf, &copy);

	//LOGI("IOCTL = %d/n",code);

	//system("su");

	if(fd == -1)
	{
		LOGE("ioctl : FAIL !! (open device %s ) /n ",DEVICE_NAME);
		return 0;
	}
	else
	{
		ret = ioctl(fd, code, buffer);
		LOGI("ioctl : cmd =%d  , ret=%d /n", code, ret);
	}

	//LOGI("begin JNI to Java...... /n");
	(*env)->ReleaseByteArrayElements(env, buf, buffer, JNI_COMMIT);
	//LOGI("finish JNI to Java! /n");

	return 1;
}

jint Java_com_my_control_MyClass_read(JNIEnv *env, jobject thiz, jint count, jbyteArray buf)
{
	int ret;
	jboolean copy;
	jbyte* buffer = (*env)->GetByteArrayElements(env, buf, &copy);


	if(fd == -1)
	{
		LOGE(" READ FAIL !! (open device %s ) /n ", DEVICE_NAME);
		return 0;
	}
	ret = read(fd, buffer, count);

	if( ret== 0)
	{
		LOGE(" READ FAIL !! (open device %s ) /n ", DEVICE_NAME);
		return 0;
	}

	LOGE(" READ FILE : Read %d bytes /n ", ret);

	(*env)->ReleaseByteArrayElements(env, buf, buffer, JNI_ABORT);
	return ret;
}


jint Java_com_my_control_MyClass_write(JNIEnv *env, jobject thiz, jint count, jbyteArray buf)
{
	int ret;
	jboolean copy;
	jbyte* buffer = (*env)->GetByteArrayElements(env, buf, &copy);

	LOGE("Java_com_my_control_MyClass_write .....!");
	if(fd == -1)
	{
		LOGE(" WRITE FAIL !! (open device %s ) /n ", DEVICE_NAME);
		return 0;
	}

	ret = write(fd, buffer, count);

	if( ret== 0)
	{
		LOGE(" WRITE FAIL !! (open device %s ) /n ", DEVICE_NAME);
		return 0;
	}

	LOGE(" WRITE FILE : Read %d bytes /n ", ret);

	(*env)->ReleaseByteArrayElements(env, buf, buffer, JNI_ABORT);
	return ret;
}
