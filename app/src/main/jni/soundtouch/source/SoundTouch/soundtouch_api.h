//
// Created by zhangyu on 15-12-28.
//

#ifndef SMARTDEVICE_TEST_H
#define SMARTDEVICE_TEST_H

#include "SoundTouch.h"
#include "../../include/STTypes.h"
#include "../../include/SoundTouch.h"

class Base
{
private:
	int b_number;
public:
	Base( ){}
	Base(int i) : b_number (i) { }
	int get_number( ) {return b_number;}
	//void print( ) {cout << b_number << endl;}
};
using namespace soundtouch;
class soundtouch_api : public SoundTouch
{
public:
	int divoom_sound_change(signed char *in_buf, int in_len, signed char *out_buf, int out_buf_len, float tempo, float pitch, float rate);
	int divoom_process(signed char *in_buf, int in_len, signed char *out_buf, int out_buf_len);
	void divoom_setup(float tempo, float pitch, float rate);
};


#endif //SMARTDEVICE_TEST_H
