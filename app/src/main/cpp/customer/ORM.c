//
// Created by Administrator on 2018/1/26.
//

#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdio.h>
#include "CustomerForTest.h"

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
/**
  * 访问Java类的静态属性
  */
JNIEXPORT void JNICALL Java_com_beini_ndk_NDKMain_accessStaticField
        (JNIEnv *env, jobject instance) {
    //与上面类似，只不过是某些方法需要加上Static
    jclass clz = (*env)->GetObjectClass(env, instance);
    jfieldID fid = (*env)->GetStaticFieldID(env, clz, "num", "I");
    jint jInt = (*env)->GetStaticIntField(env, clz, fid);
    jInt++;
    (*env)->SetStaticIntField(env, clz, fid, jInt);
}
/**
  * 访问Java类的非静态方法
  */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_accessMethod(JNIEnv *env, jobject instance) {
    // TODO
    jclass clz = (*env)->GetObjectClass(env, instance);
    //拿到方法的ID，最后一个参数是方法的签名
    jmethodID jmethodID1 = (*env)->GetMethodID(env, clz, "getRandom", "(I)I");
    //调用该方法，最后一个是可变参数，就是调用该方法所传入的参数
    //套路是如果返回是：Call返回类型Method
    jint jint1 = (*env)->CallIntMethod(env, instance, jmethodID1, 100);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "------------>%d", jint1);
}
/**
  * 访问Java类的静态方法
  */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_accessStaticMethod(JNIEnv *env, jobject instance) {
    // TODO
    jclass clz = (*env)->GetObjectClass(env, instance);
    jmethodID jmethodID1 = (*env)->GetStaticMethodID(env, clz, "getUUID", "()Ljava/lang/String;");
    //调用java的静态方法，拿到返回值
    jstring jstring1 = (*env)->CallStaticObjectMethod(env, clz, jmethodID1);
//把拿到的Java字符串转换为C的字符串
    char *cstr = (*env)->GetStringUTFChars(env, jstring1, NULL);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "", cstr);
}
/**
  * 间接访问Java类的父类的方法
  */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_accessNonvirtualMethod(JNIEnv *env, jobject instance) {
    // TODO
//    当有这个类的对象的时候，使用(*env)->GetObjectClass()，相当于Java中的test.getClass()
//    当有没有这个类的对象的时候，(*env)->FindClass()，相当于Java中的Class.forName("com.test.TestJni")
    jclass clz = (*env)->GetObjectClass(env, instance);
    //先拿到属性man   com.beini.ui.fragment.cpptest
    jmethodID jmethodID1 = (*env)->GetFieldID(env, clz, "man",
                                              "Lcom/beini/ui/fragment/cpptest/Human;");
    jobject man = (*env)->GetObjectField(env, instance, jmethodID1);
    //拿到父类的类，以及speek的方法id
    jclass clz_human = (*env)->FindClass(env, "com/beini/ui/fragment/cpptest/Human");
    jmethodID jmethodID2 = (*env)->GetMethodID(env, clz_human, "speak", "()V");
    //调用自己的speek实现
    (*env)->CallVoidMethod(env, man, jmethodID2);
    //调用父类的speek实现
    (*env)->CallNonvirtualVoidMethod(env, man, clz_human, jmethodID2);
}

JNIEXPORT jlong JNICALL
Java_com_beini_ndk_NDKMain_accessConstructor(JNIEnv *env, jobject instance) {
    // TODO
    jclass clz_date = (*env)->FindClass(env, "java/util/Date");
    //构造方法的函数名的格式是：<init>
    //不能写类名，因为构造方法函数名都一样区分不了，只能通过参数列表（签名）区分
    jmethodID jmethodID1 = (*env)->GetMethodID(env, clz_date, "<init>", "()V");
    //调用构造函数
    jobject date = (*env)->NewObject(env, clz_date, jmethodID1);
    //注意签名，返回值long的属性签名是J
    jmethodID mid_getTime = (*env)->GetMethodID(env, clz_date, "getTime", "()J");
    //调用getTime方法
    jlong jtime = (*env)->CallLongMethod(env, date, mid_getTime);
    return jtime;
}
/**
 * 中文乱码问题
 */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_testChineseIn(JNIEnv *env, jobject instance, jstring chinese_) {
    const char *chinese = (*env)->GetStringUTFChars(env, chinese_, 0);
    // TODO
    __android_log_print(ANDROID_LOG_ERROR, TAG, "--->%s", chinese);
    (*env)->ReleaseStringUTFChars(env, chinese_, chinese);
}

JNIEXPORT jstring JNICALL
Java_com_beini_ndk_NDKMain_testChineseOut(JNIEnv *env, jobject instance) {
    // TODO
    char *chinese = "点对点";
    jstring jstring1 = (*env)->NewStringUTF(env, chinese);
    return jstring1;
}
/**
 * 数组的处理
 */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_sortArray(JNIEnv *env, jobject instance, jintArray sorArry_) {
    //通过Java的数组，拿到C的数组的指针
    jint *c_arr = (*env)->GetIntArrayElements(env, sorArry_, NULL);
    //获取Java数组的大小
    jsize len = (*env)->GetArrayLength(env, sorArry_);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "--->%d", len);
    //排序，其中compare是函数指针，用于比较大小，与Java类似
//    qsort(c_arr, len, sizeof(jint), compare);

    //操作完之后需要同步C的数组到Java数组中
    (*env)->ReleaseIntArrayElements(env, sorArry_, c_arr, 0);
}

void qsort(jint *c_arr, jsize len, jint size, int(*fun_compare)(int *a, int *b)) {


}

int compare(const int *a, const int *b) {
    return (*a) - (*b);
}
/**
 * 局部引用
 */
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_localRef(JNIEnv *env, jobject instance) {
    int i = 0;
    for (; i < 10; i++) {//在循环中创建了占用内存大的对象（例子）
        jclass clz_date = (*env)->FindClass(env, "java/util/Date");
        jmethodID jmethodID1 = (*env)->GetMethodID(env, clz_date, "<init>", "()V");
        jobject jobject_date = (*env)->NewObject(env, clz_date, jmethodID1);
        //使用
        //...
        //不再使用jobject对象了
        //通知垃圾回收器回收这些对象，确保内存充足
        (*env)->DeleteLocalRef(env, jobject_date);
    }
}

/**
  * 全局引用
  */
jstring jstringGlobal;

JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_createGlobalRef(JNIEnv *env, jobject instance) {//创建
    jstringGlobal = (*env)->NewStringUTF(env, "not let the youth wating for");
//    jstringGlobal = "Hi";
//    jstringGlobal = (*env)->NewString(env, "全局变量", sizeof("not let the youth wating for"));
    //通过NewGlobalRef创建全局引用
    (*env)->NewGlobalRef(env, jstringGlobal);

}

JNIEXPORT jstring JNICALL
Java_com_beini_ndk_NDKMain_getGlobalRef(JNIEnv *env, jobject instance) {//获取
    return jstringGlobal;
}

JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_deteleGlobalRef(JNIEnv *env, jobject instance) {//删除
    (*env)->DeleteGlobalRef(env, jstringGlobal);
}

JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_getCmethod(JNIEnv *env, jobject instance) {
    // TODO
    int result = add(1, 2);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "------------>%d", result);
}

