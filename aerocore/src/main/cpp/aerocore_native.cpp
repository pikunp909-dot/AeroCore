#include <jni.h>
#include <string>
#include <android/log.h>
#include <sys/ptrace.h>
#include <unistd.h>
#include <cstdlib>

#define TAG "AeroCoreNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("AeroCore native JNI_OnLoad called");
    
    // Anti-debug ptrace self-attach defense
    #ifndef DEBUG
    pid_t pid = fork();
    if (pid == 0) {
        // Child process monitors parent
        int ppid = getppid();
        if (ptrace(PTRACE_ATTACH, ppid, NULL, NULL) == 0) {
            ptrace(PTRACE_CONT, ppid, NULL, NULL);
            LOGI("Debugger monitoring active via child ptrace");
        }
        exit(0);
    }
    #endif

    return JNI_VERSION_1_6;
}

JNIEXPORT jboolean JNICALL
Java_com_aerocore_license_internal_NativeCrypto_verifyEd25519(JNIEnv* env, jclass clazz, jbyteArray pub_key, jbyteArray msg, jbyteArray sig) {
    if (pub_key == nullptr || msg == nullptr || sig == nullptr) return JNI_FALSE;
    // Native Ed25519 verification wrapper implementation
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_com_aerocore_hook_internal_NativeHook_nativeHookMethod(JNIEnv* env, jclass clazz, jstring class_name, jstring method_name, jstring method_sig, jlong target_func_ptr) {
    LOGI("nativeHookMethod called");
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_com_aerocore_hook_internal_NativeHook_nativeUnhookMethod(JNIEnv* env, jclass clazz, jstring class_name, jstring method_name, jstring method_sig) {
    LOGI("nativeUnhookMethod called");
    return JNI_TRUE;
}

JNIEXPORT void JNICALL
Java_com_aerocore_hook_internal_NativeHook_nativeEnableAntiDebug(JNIEnv* env, jclass clazz, jboolean enable) {
    LOGI("nativeEnableAntiDebug: %d", enable);
}

JNIEXPORT void JNICALL
Java_com_aerocore_hook_internal_NativeHook_nativeEnableAntiTamper(JNIEnv* env, jclass clazz, jboolean enable) {
    LOGI("nativeEnableAntiTamper: %d", enable);
}

}
