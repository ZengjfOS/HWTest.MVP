#include <stdio.h>
#include <string.h>
#include <linux/types.h>
#include <stdlib.h>
#include <fcntl.h>
#include "i2c_data.h"
#include <jni.h>

#include "android/log.h"
static const char *TAG="EEPROM_aplex";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
/*
 * Class:     com_android_eeprom_I2CController
 * Method:    open
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_aplex_aplextest_jninative_I2CController_open
        (JNIEnv * env, jobject thiz, jstring path, jint flags){

    int fd = -1;
    LOGE("open() fd = %d", fd);
    // Opening device
    {
        jboolean iscopy;
        /**
         * 将Java的字符串转换成C中的字符串
         */
        const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
        //LOGE("Open file %s with flags 0x%x", path_utf, O_RDWR | flags);
        fd = open(path_utf, O_RDWR | flags);
        LOGE("open() fd = %d", fd);
        /**
         * 和前面的GetStringUTFChars一对用法，相当于malloc和free
         */
        (*env)->ReleaseStringUTFChars(env, path, path_utf);
        if (fd == -1)
        {
            // Throw an exception
            LOGE("Cannot open file");
            // TODO: throw an exception
            return -1;
        }
    }
    return (jint)fd;
}

/*
 * Class:     com_android_eeprom_I2CController
 * Method:    readStr
 * Signature: (IIII)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_aplex_aplextest_jninative_I2CController_readStr
        (JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jint count){
    unsigned char buf[count+1];
    bzero(buf, count+1);
    if (i2c_data_read_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count) == 0) {
        return (*env)->NewStringUTF(env, buf);
    } else {
        return (*env)->NewStringUTF(env, "-1");
    }
    //LOGE("read i2c data: %s\n", buf);
}

/*
 * Class:     com_android_eeprom_I2CController
 * Method:    writeStr
 * Signature: (IIII)Ljava/lang/String;
 */
JNIEXPORT jint JNICALL Java_com_aplex_aplextest_jninative_I2CController_writeStr
        (JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jstring buffer, jint count){
    unsigned char* buf = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, buffer, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0)
    {
        buf = (unsigned char*)malloc(alen + 1);

        memcpy(buf, ba, alen);
        buf[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    LOGE("fd: %d, write String: %s, string.length: %d", fd, buf, alen);
    return i2c_data_write_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count > alen ? alen : count);
}

/*
 * Class:     com_android_eeprom_I2CController
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_aplex_aplextest_jninative_I2CController_close
        (JNIEnv * env, jobject thiz, jint fd){
    close( (int)fd );
    //LOGE("close() fd = %d", fd);
}

