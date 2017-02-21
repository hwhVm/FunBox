//
// Created by zhangyu on 15-12-28.
//

#include <jni.h>
#include "soundtouch_api.h"
#include "../../include/STTypes.h"
#include <android/log.h>
#include <stdio.h>
#include <assert.h>
//#include "ORM.h"
//#include "net/include/divoom_net_interface.h"
//#include "net/include/command/user_struct.h"
//#include "net/include/command/device_struct.h"
//#include "net/include/divoom_net_com.h"
//#include "net/include/divoom_net_multicast_search.h"


//#define divoom_print printf

//jlong  Java_com_ndk_NDKMain_divoomShiftAAC(JNIEnv * env, jobject jObj,jbyteArray pcm){
//    jint aac_len = 0;
//    jsize len =(*env).GetArrayLength(pcm);
//    jsize aac_buf_len = len+8*1024;
//
//    __android_log_print(ANDROID_LOG_ERROR, "android_Log", "Java_com_ndk_NDKMain_divoomShiftAAC");
//   // divoom_print("Java_com_ndk_NDKMain_divoomShiftAAC\n");
//    jbyteArray rets=(*env).NewByteArray(len);
//    jbyte *jpcm =  (*env).GetByteArrayElements(pcm, 0);
//
//    jbyte *jaac= (*env).GetByteArrayElements(rets, 0);
//
//
//    class soundtouch_api soundtouch_test;
//
//
//    aac_len = soundtouch_test.divoom_sound_change(jpcm,len,jaac,aac_buf_len, 0.0, 0.0, 0.0);
//
//    jbyteArray ret=(*env).NewByteArray(aac_len);
//    (*env).SetByteArrayRegion(ret,0,aac_len,jaac);
//
//    return (jlong)ret;
//}
extern "C"{

int c_divoom_sound_change(signed char *in_buf, int in_len, signed char *out_buf, int out_buf_len, float tempo, float pitch, float rate)
{
    class soundtouch_api soundtouch_test;
    int len;

    len = soundtouch_test.divoom_sound_change(in_buf, in_len, out_buf, out_buf_len, tempo, pitch, rate);
    return len;
}

}


using namespace soundtouch;
//typedef float  SAMPLETYPE;
//// data type for sample accumulation: Use double to utilize full precision.
//typedef double LONG_SAMPLETYPE;
int soundtouch_api::divoom_sound_change(signed char *in_buf, int in_len, signed char *out_buf,
                                        int out_buf_len, float tempo, float pitch, float rate) {
    //  SoundTouch soundTouch;
    int ret_len = 0;

    //try
    {

        // Setup the 'SoundTouch' object for processing the sound
        divoom_setup(tempo, pitch, rate);

        // clock_t cs = clock();    // for benchmarking processing duration
        // Process the sound
        ret_len = divoom_process(in_buf, in_len, out_buf, out_buf_len);
        // clock_t ce = clock();    // for benchmarking processing duration
        // printf("duration: %lf\n", (double)(ce-cs)/CLOCKS_PER_SEC);

        // Close WAV file handles & dispose of the objects

        // fprintf(stderr, "Done!\n");
    }
#if 0
    catch (const runtime_error &e)
        {
            // An exception occurred during processing, display an error message
            fprintf(stderr, "%s\n", e.what());
            return -1;
        }
#endif

    return ret_len;
}

void soundtouch_api::divoom_setup(float tempo, float pitch, float rate) {
    int sampleRate;
    int channels;
    int quick = 0;
    int noAntiAlias = 0;
    int goalBPM = 0;
    int speech = 0;
    int detectBPM = 0;

    sampleRate = 44100; //
    channels = 1;
    setSampleRate(sampleRate);
    setChannels(channels);

#if 1
    setTempoChange(tempo);
    setPitchSemiTones(pitch);
    setRateChange(rate);
#endif

    //pSoundTouch->setSetting(SETTING_USE_QUICKSEEK, quick);
    //pSoundTouch->setSetting(SETTING_USE_AA_FILTER, !noAntiAlias);

    //divoom_print("%s\n", __func__);

#if 0
    if (speech)
        {
            // use settings for speech processing
            setSetting(SETTING_SEQUENCE_MS, 40);
            setSetting(SETTING_SEEKWINDOW_MS, 15);
            setSetting(SETTING_OVERLAP_MS, 8);
            fprintf(stderr, "Tune processing parameters for speech processing.\n");
        }
#endif

}

// helper-function to swap byte-order of 16bit integer
static inline short divoom_swap16(short &wData) {
    wData = ((wData >> 8) & 0x00FF) |
            ((wData << 8) & 0xFF00);
    return wData;
}

// helper-function to swap byte-order of buffer of 16bit integers
static inline void divoom_swap16Buffer(short *pData, int numWords) {
    int i;

    for (i = 0; i < numWords; i++) {
        pData[i] = divoom_swap16(pData[i]);
    }
}

#if 0
int soundtouch_api::divoom_process(signed char *in_buf, int in_len, signed char *out_buf, int out_buf_len)
    {
        int nSamples = 0;
        int nChannels = 1;
        int buffSizeSamples = 0;
        int out_offset = 0;
        int in_offset = 0;
        SAMPLETYPE sampleBuffer[BUFF_SIZE] = {0};
        int first = 0;
        int numElems = 0;
        int numBytes = 0;

        assert(nChannels > 0);
        buffSizeSamples = BUFF_SIZE / nChannels;

        // Process samples read from the input file
        while (in_len > 0)
        {

            ///divoom_print("in_len %d\n", in_len);
            // Read a chunk of samples from the input file
            if(in_len >= BUFF_SIZE*2)
            {
                numElems = BUFF_SIZE;
            }
            else
            {
                numElems = in_len/2;
            }

            numBytes = numElems * 2;
            memcpy(sampleBuffer, in_buf+in_offset, numBytes);
            numElems = numBytes / 2;

            in_offset += numBytes;
            in_len -= numBytes;

            nSamples = numElems / nChannels;

            //printf("input nSamples %d\n", nSamples);

            // Feed the samples into SoundTouch processor
            putSamples(sampleBuffer, nSamples);

            // Read ready samples from SoundTouch processor & write them output file.
            // NOTES:
            // - 'receiveSamples' doesn't necessarily return any samples at all
            //   during some rounds!
            // - On the other hand, during some round 'receiveSamples' may have more
            //   ready samples than would fit into 'sampleBuffer', and for this reason
            //   the 'receiveSamples' call is iterated for as many times as it
            //   outputs samples.
            do
            {
                nSamples = receiveSamples(sampleBuffer, buffSizeSamples);
                if(nSamples != 0) {
                    //printf("read nSamples %d\n", nSamples);
                }

                if(out_buf_len <= (out_offset + nSamples * nChannels))
                {
                    //divoom_print("out_buf_len is too small\n");
                    return -1;
                }

                if(nSamples != 0)
                {
                    numElems = nSamples * nChannels;
                    memcpy(out_buf+out_offset, sampleBuffer, numElems*2);
                    out_offset += 2 * numElems;
                }
            } while (nSamples != 0);
        }

        // Now the input file is processed, yet 'flush' few last samples that are
        // hiding in the SoundTouch's internal processing pipeline.
        flush();
#if 1
        do
        {
            nSamples = receiveSamples(sampleBuffer, buffSizeSamples);
            if(nSamples != 0) {
                //divoom_print("22 nSamples %d\n", nSamples);
            }
            if(nSamples != 0)
            {
                numElems = nSamples * nChannels;
                memcpy(out_buf+out_offset, sampleBuffer, numElems*2);
                out_offset += 2 * numElems;
            }
            if(out_buf_len <= (out_offset + nSamples * nChannels))
            {
                //divoom_print("out_buf_len is too small\n");
                return -1;
            }
        } while (nSamples != 0);
#endif

        return out_offset;
    }
#endif

// helper-function to swap byte-order of 16bit integer
static inline short _swap16(short &wData) {
    //wData = ((wData >> 8) & 0x00FF) | ((wData << 8) & 0xFF00);
    return wData;
}

int divoom_read(SAMPLETYPE *buffer, int maxElems, signed char *in_buffer) {
    unsigned int afterDataRead;
    int numBytes;
    int numElems;
    int bytesPerSample;
    int bits_per_sample = 16;

    assert(buffer);

    bytesPerSample = bits_per_sample / 8;

    if ((bytesPerSample < 1) || (bytesPerSample > 4)) {
        printf("erroe !!!!!\n");
        return 0;
    }

    numBytes = maxElems * bytesPerSample;

    // read raw data into temporary buffer
    char *temp = (char *) new char[numBytes];
    memcpy(temp, in_buffer, numBytes);

    //dataRead += numBytes;

    numElems = numBytes / bytesPerSample;

    // swap byte ordert & convert to float, depending on sample format
    switch (bytesPerSample) {
        case 1: {
            unsigned char *temp2 = (unsigned char *) temp;
            double conv = 1.0 / 128.0;
            for (int i = 0; i < numElems; i++) {
                buffer[i] = (float) (temp2[i] * conv - 1.0);
            }
            break;
        }

        case 2: {
            short *temp2 = (short *) temp;
            double conv = 1.0 / 32768.0;
            for (int i = 0; i < numElems; i++) {
                short value = temp2[i];
                //buffer[i] = (float) (_swap16(value) * conv);
                buffer[i] = (float) (_swap16(value));
            }
            break;
        }
    }
    free(temp);

    return numElems;
}

/// Convert from float to integer and saturate
inline int saturate(float fvalue, float minval, float maxval) {
    if (fvalue > maxval) {
        fvalue = maxval;
    }
    else if (fvalue < minval) {
        fvalue = minval;
    }
    return (int) fvalue;
}

int divoom_write(const SAMPLETYPE *buffer, int numElems, signed char *out_buffer) {
    int numBytes;
    int bytesPerSample;
    int bits_per_sample = 16;

    if (numElems == 0) return 0;

    bytesPerSample = bits_per_sample / 8;
    numBytes = numElems * bytesPerSample;

    short *temp = new short[numBytes];

    switch (bytesPerSample) {
        case 2: {
            short *temp2 = (short *) temp;
            for (int i = 0; i < numElems; i++) {
//                short value = (short) saturate(buffer[i] * 32768.0f, -32768.0f, 32767.0f);
                short value = (short) saturate(buffer[i], -32768.0f, 32767.0f);
                temp2[i] = _swap16(value);
            }
            break;
        }

        default:
            assert(false);
    }

    memcpy(out_buffer, temp, numBytes);
    free(temp);
    return numBytes;
}


#define BUFF_SIZE           6720

int soundtouch_api::divoom_process(signed char *in_buf, int in_len, signed char *out_buf,
                                   int out_buf_len) {
    int nSamples = 0;
    int nChannels = 1;
    int buffSizeSamples = 0;
    int out_offset = 0;
    int in_offset = 0;
    SAMPLETYPE sampleBuffer[BUFF_SIZE] = {0};
//    SAMPLETYPE tmp[BUFF_SIZE * 2] = {0};
//    SAMPLETYPE tmp_out[BUFF_SIZE * 2] = {0};
    int first = 0;
    int numElems = 0;
    int numBytes = 0;

    assert(nChannels > 0);
    buffSizeSamples = BUFF_SIZE / nChannels;

    // Process samples read from the input file
    while (in_len > 0) {
        if (in_len >= BUFF_SIZE * 2) {
            numElems = BUFF_SIZE;
        }
        else {
            numElems = in_len / 2;
        }
        nSamples = divoom_read(sampleBuffer, numElems, in_buf+in_offset);
        numBytes = numElems * 2;
        in_offset += numBytes;
        in_len -= numBytes;
        printf("input nSamples %d\n", nSamples);

        // Feed the samples into SoundTouch processor
        putSamples(sampleBuffer, nSamples);

        do {
            nSamples = receiveSamples(sampleBuffer, buffSizeSamples);
            if (nSamples != 0) {
                printf("read nSamples %d\n", nSamples);
                if (out_buf_len <= (out_offset + nSamples * nChannels)) {
//					divoom_print("out_buf_len is too small\n");
                    return -1;
                }
                out_offset += divoom_write(sampleBuffer, nSamples, out_buf+out_offset);
            }
        } while (nSamples != 0);
    }

    // Now the input file is processed, yet 'flush' few last samples that are
    // hiding in the SoundTouch's internal processing pipeline.
    flush();

    do {
        nSamples = receiveSamples(sampleBuffer, buffSizeSamples);
        if (nSamples != 0) {
//			divoom_print("22 nSamples %d\n", nSamples);
            out_offset += divoom_write(sampleBuffer, nSamples, out_buf+out_offset);
        }
    } while (nSamples != 0);

    return out_offset;
}
