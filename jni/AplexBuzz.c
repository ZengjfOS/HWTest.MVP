#include <unistd.h>
#include <fcntl.h>
#include <jni.h>

#include "com_aplex_aplextest_jninative_AplexBuzz.h"
#include "android/log.h"
static const char *TAG="aplexBuzz";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define BUZZER_ENABLE   182
#define BUZZER_FREQENCY 183
#define BUZZER_DISABLE  184

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    enable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_aplex_aplextest_jninative_AplexBuzz_enable
        (JNIEnv *env, jobject obj) {
    int fd = open("/dev/buzzer", O_RDWR);
    if ( fd == -1 ) {
        LOGE("open buzzer device error.");
        return;
    }

    ioctl(fd, BUZZER_ENABLE, 0);

    close(fd);

    //LOGE("enable buzzer device. ");
}

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    setFrequency
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_aplex_aplextest_jninative_AplexBuzz_setFrequency
        (JNIEnv *env, jobject obj, jint frequency) {

    int fd = open("/dev/buzzer", O_RDWR);
    if ( fd == -1 ) {
        LOGE("open buzzer device error.");
        return;
    }

    ioctl(fd, BUZZER_FREQENCY, frequency);

    close(fd);

    //LOGE("set buzzer device frequency. ");
}

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    disable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_aplex_aplextest_jninative_AplexBuzz_disable
        (JNIEnv *env, jobject obj) {

    int fd = open("/dev/buzzer", O_RDWR);
    if ( fd == -1 ) {
        LOGE("open buzzer device error.");
        return;
    }

    ioctl(fd, BUZZER_DISABLE, 0);

    close(fd);

    //LOGE("disable buzzer device. ");
}

