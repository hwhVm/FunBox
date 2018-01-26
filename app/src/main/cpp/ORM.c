//
// Created by Administrator on 2018/1/26.
//

#include <jni.h>
#include <string.h>
#include <android/log.h>

#define  TAG "com.beini"
/**
  *访问Java类的非静态属性
  */
JNIEXPORT void JNICALL Java_com_beini_ndk_NDKMain_accessField
        (JNIEnv *env, jobject jobj) {
    //通过对象拿到Class
    jclass clz = (*env)->GetObjectClass(env, jobj);
    //拿到对应属性的ID
    jfieldID fid = (*env)->GetFieldID(env, clz, "str", "Ljava/lang/String;");
    //通过属性ID拿到属性的值
    jstring jstr = (*env)->GetObjectField(env, jobj, fid);

    //通过Java字符串拿到C字符串，第三个参数是一个出参，用来告诉我们GetStringUTFChars内部是否复制了一份字符串
    //如果没有复制，那么出参为isCopy，这时候就不能修改字符串的值了，因为Java中常量池中的字符串是不允许修改的（但是jstr可以指向另外一个字符串）
    const char *cstr = (*env)->GetStringUTFChars(env, jstr, NULL);
    //在C层修改这个属性的值
    //重新生成Java的字符串，并且设置给对应的属性
    const char *res = "fuck you";
    jstring jstr_new = (*env)->NewStringUTF(env, res);
    (*env)->SetObjectField(env, jobj, fid, jstr_new);
    //最后释放资源，通知垃圾回收器来回收
    //良好的习惯就是，每次GetStringUTFChars，结束的时候都有一个ReleaseStringUTFChars与之呼应
    (*env)->ReleaseStringUTFChars(env, jstr, cstr);
}

JNIEXPORT void JNICALL Java_com_beini_ndk_NDKMain_accessStaticField
        (JNIEnv *env, jobject instance) {
// TODO
    //与上面类似，只不过是某些方法需要加上Static
    jclass clz = (*env)->GetObjectClass(env, instance);
    jfieldID fid = (*env)->GetStaticFieldID(env, clz, "num", "I");
    jint jInt = (*env)->GetStaticIntField(env, clz, fid);
    jInt++;
    (*env)->SetStaticIntField(env, clz, fid, jInt);
}

