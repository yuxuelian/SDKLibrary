#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include "com_kaibo_ndklib_encrypt_EncryptUtils.h"
#include <android/log.h>

#ifdef __cplusplus
extern "C" {
#endif

#define TAG "NdkLib"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

char *jstringTostring(JNIEnv *env, jstring jstr);

/*
 * Class:     com_kaibo_ndklib_encrypt_EncryptUtils
 * Method:    encrypt
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_kaibo_ndklib_encrypt_EncryptUtils_encrypt(
        JNIEnv *env,
        jobject instance,
        jstring jstr) {

    LOGV("LOGV");
    LOGD("LOGD");
    LOGI("LOGI");
    LOGW("LOGW");
    LOGE("LOGE");

    char *cstr = jstringTostring(env, jstr);
    return env->NewStringUTF(cstr);
}

/*
 * Class:     com_kaibo_ndklib_encrypt_EncryptUtils
 * Method:    decrypt
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_kaibo_ndklib_encrypt_EncryptUtils_decrypt
        (JNIEnv *env, jobject instance, jstring jstr) {

    LOGV("LOGV");
    LOGD("LOGD");
    LOGI("LOGI");
    LOGW("LOGW");
    LOGE("LOGE");

    char *cstr = jstringTostring(env, jstr);
    return env->NewStringUTF(cstr);
}

char *jstringTostring(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}
#ifdef __cplusplus
}
#endif

