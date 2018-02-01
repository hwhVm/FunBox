//
// Created by Administrator on 2018/1/31.
//

/**
*声音
*/

#include <jni.h>
#include <android/log.h>
#include "../inc/fmod.hpp"
#include <unistd.h>

#define  TAG "com.beini"

#define MODE_NORMAL 0
#define MODE_LUOLI 1
#define MODE_DASHU 2
#define MODE_JINGSONG 3
#define MODE_GAOGUAI 4
#define MODE_KONGLING 5
using namespace FMOD;

extern "C"
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_playsMusic(JNIEnv
                                      *env,
                                      jobject instance, jstring
                                      music_path_) {
// TODO
    __android_log_print(ANDROID_LOG_ERROR, TAG,
                        "------------>%c", music_path_);
//得到音乐文件地址
    const char *music_path = env->GetStringUTFChars(music_path_, 0);
//FMOD系统对象
    FMOD::System *system = 0;
//音效
    FMOD::Sound *sound = 0;
//声轨
    FMOD::Channel *channel = 0;
//创建对象
    FMOD::System_Create(&system);
//初始化 系统对象最大声轨为32
    system->init(32, FMOD_INIT_NORMAL, 0);
//加载声音 如果过大建议你用FMOD_CREATESTREAM 标志
    system->createSound(music_path, FMOD_DEFAULT, 0, &sound);
//播放音乐
    system->playSound(sound, 0, false, &channel);
//开启更新通道 官方demo写了 但是我我发现不写也没事
    system->update();
//保存声音时间
    unsigned int duration = 0;
//得到声音
    sound->getLength(&duration, FMOD_TIMEUNIT_MS);
//线程休眠 以微妙 所以要转
    usleep(duration * 1000);//释放资源
    sound->release();
    system->close();
    system->release();
    env->ReleaseStringUTFChars(music_path_, music_path);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_beini_ndk_NDKMain_playMusicByType(JNIEnv *env, jobject instance, jstring music_path_,
                                           jint type) {
//初始化
    System *system;
    Sound *sound;
    DSP *dsp;
    bool playing = true;
    Channel *channel;
    float frequency = 0;
    System_Create(&system);
    system->init(32, FMOD_INIT_NORMAL, NULL);
    const char *path_cstr = env->GetStringUTFChars(music_path_, NULL);

    try {
//创建声音
        system->createSound(path_cstr, FMOD_DEFAULT, NULL, &sound);
        switch (type) {
            case MODE_NORMAL:
//原生播放
                system->playSound(sound, 0, false, &channel);
                break;
            case MODE_LUOLI:
//loli
//DSP digital singal process
//dsp->音效
//dsp 提升或者降低音调的一种音效
                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
//设置音调的参数
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 8.0);
//添加进到channel
                system->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                break;
            case MODE_DASHU:
//大叔
                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
//设置音调的参数
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.8);
//添加进到channel
                system->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                break;
            case MODE_JINGSONG:
//惊悚
                system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 5);
                system->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                break;
            case MODE_GAOGUAI:
//搞怪
//提高说话的速度
                system->playSound(sound, 0, false, &channel);
                channel->getFrequency(&frequency);
                frequency = frequency * 2;
                channel->setFrequency(frequency);
                break;
            case MODE_KONGLING:
//空灵
                system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 20);
                system->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                break;
            default:
                break;
        }
    } catch (...) {
//        LOGE("%s", "发生异常");
        goto
                end;
    }
    system->update();
//释放资源
//单位是微妙
//每秒钟判断下是否是播放
    while (playing) {
        channel->isPlaying(&playing);
        usleep(1000 * 1000);
    }
    goto end;
    end:
    env->ReleaseStringUTFChars(music_path_, path_cstr);
    sound->release();
    system->close();
    system->release();

}
/**
 * 基于字节流
 */