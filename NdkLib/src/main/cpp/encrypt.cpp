#include <jni.h>
#include <stdlib.h>
#include <string.h>

char *jstringTostring(JNIEnv *env, jstring jstr);

extern "C" JNIEXPORT jstring

JNICALL Java_com_kaibo_ndklib_encrypt_EncryptUtils_encrypt(
        JNIEnv *env,
        jclass clazz,
        jstring jstr) {
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

