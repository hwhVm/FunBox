//
// Created by beini on 2017/5/2.
//

#include <jni.h>
#include <string>
#include<android/log.h>

#define  TAG "com.beini"
/**
 * JNIEnv *env:代表了VM里的环境，本地代码可以通过这个env指针对Java代码进行操作，例如：创建Java类对象，调用Java对象方法，获取Java对象属性等
 *
 *jobject obj相当于Java中的Object类型，它代表调用这个本地方法的对象，例如：如果有new NativeTest.CallNative()，CallNative()是本地方法，本地方法第二个参数是jobject表示的是NativeTest类的对象的本地引用。
 *
 *extern "C"的主要作用就是为了能够正确实现C++代码调用其他C语言代码。加上extern "C"后，会指示编译器这部分代码按C语言的进行编译，而不是C++的。由于C++支持函数重载，因此编译器编译函数的过程中会将函数的参数类型也加到编译后的代码中，而不仅仅是函数名；而C语言并不支持函数重载，因此编译C语言代码的函数时不会带上函数的参数类型，一般之包括函数名。
 *比如说你用C 开发了一个DLL 库，为了能够让C ++语言也能够调用你的DLL输出(Export)的函数，你需要用extern "C"来强制编译器不要修改你的函数名。
 *
 * JNIEXPORT:声明该接口为动态库导出接口。
 *
 * JNICALL:声明了函数参数的入栈方式。
 *
 * **/
extern "C"
JNIEXPORT jstring JNICALL
Java_com_beini_ndk_NDKMain_getPassword(JNIEnv *env, jobject/* this */) {
    // TODO
    std::string password = "123456";
    __android_log_print(ANDROID_LOG_ERROR, TAG, "------->password=%s", password.c_str());
    return env->NewStringUTF(password.c_str());
}


/**
*调用Java中的函数
*/
jstring MNewString(JNIEnv *env, const char *chars, jint len) {
    jclass stringClass;
    jmethodID cid;
    jbyteArray elemArr;
    jstring result;
    jstring jencoding;

    stringClass = env->FindClass("java/lang/String");
    if (stringClass == NULL) {
        return NULL;/* exception thrown */
    }
/* Get the method ID for the String(byte[] data, String charsetName) constructor */
    cid = env->GetMethodID(stringClass, "<init>", "([BLjava/lang/String;)V");
    if (cid == NULL) {
        return NULL;/* exception thrown */
    }
    jencoding = env->NewStringUTF("GBK");
    /* Create a byte[] that holds the string characters */
    elemArr = env->NewByteArray(len);
    if (elemArr == NULL) {
        return NULL;/* exception thrown */
    }
    env->SetByteArrayRegion(elemArr, 0, len, (jbyte *) chars);
    /* Construct a java.lang.String object */
    result = (jstring) (env->NewObject(stringClass, cid, elemArr, jencoding));
    /* Free local references */
    env->DeleteLocalRef(elemArr);
    env->DeleteLocalRef(stringClass);
    return result;
}

/**
 *传入参数
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_beini_ndk_NDKMain_withArgs(JNIEnv *env, jobject/* this */, jstring args) {
    // TODO
    const char *str = env->GetStringUTFChars(args, 0);
    __android_log_print(ANDROID_LOG_ERROR, TAG, " ");
    __android_log_print(ANDROID_LOG_ERROR, TAG, "------------>%c", str);
    env->ReleaseStringUTFChars(args, str);

    return args;
}
