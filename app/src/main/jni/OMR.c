

#include "OMR.h"
#include <jni.h>
#include <malloc.h>


#define  LOGT(...)  __android_log_print(ANDROID_LOG_ERROR, "android_Log", "print--- : num = %d",__VA_ARGS__);

int c_divoom_sound_change(signed char *in_buf, int in_len, signed char *out_buf, int out_buf_len, float tempo, float pitch, float rate);
jbyteArray Java_com_example_administrator_baseapp_ndk_NDKMain_variations(JNIEnv * env, jobject jObj,jbyteArray pcm,jfloat one,jfloat two,jfloat three){



    jsize len = (*env)->GetArrayLength(env, pcm);


    //在java中申请一块内存  以用来将C的数组传输给java程序
    jbyteArray rets=(*env)->NewByteArray(env,len);
    //获取传入的数组
    jbyte *jpcm = (*env)->GetByteArrayElements(env, pcm, 0);

    signed char *out_pcm = calloc(1, len*2);
    int ret_pcm_len = 0;

    ret_pcm_len = c_divoom_sound_change(jpcm, len, out_pcm, len*2,one, two, three);
    //防止崩溃
    if(ret_pcm_len==-1){
        ret_pcm_len=0;
    }
    jbyteArray ret=(*env)->NewByteArray(env,ret_pcm_len);
    (*env)->SetByteArrayRegion(env,ret,0,ret_pcm_len,out_pcm);
    (*env)->ReleaseByteArrayElements(env,pcm,jpcm,JNI_COMMIT);  //释放内存
    free(out_pcm);
    return ret;
}




