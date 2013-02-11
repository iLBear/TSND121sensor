//
//  TSNDcommand.h
//  Created by iLBe@r on 2013/01/25.
//

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <termios.h>

#ifndef TSND_TSNDcommand_h
#define TSND_TSNDcommand_h

class TSNDcommand{
private:
    int commandLength;
    void makeBCC(unsigned char *sendData);
	template <class T> T isValueInRange(T value, T min, T max, T returnIfNot);	//min<=value<=max?
//	int accel_cycle_ms;
//	int accel_average;
	
public:
	
	void getTime(int fd);	//0x12
    void startMeasure(int fd, int second);			//0x13
    void stopMeasure(int fd);						//0x15
    
    void setAccelMeasurement(int fd, int cycle_ms, int average, int recordAverage);	//0x16
	void getAccelMeasurement(int fd);		//0x17
    void setGeometricMeasurement(int fd, int cycle_ms, int average, int recordAverage);	//0x18
    void setPressureMeasurement(int fd, int cycle_ms, int average, int recordAverage);	//0x1a
	void setBatteryMeasurement(int fd, bool send, bool record);	//0x1c
	void setAccelRange(int fd, int range);	//0x22
	void collectAccelMeasurement(int fd, int xt, int yt, int zt, int xx, int yy, int zz);	//0x24
    void setOptionButtonMode(int fd, int mode);	//0x2c
    
	void setBuzzerVolume(int fd, int volume);	//0x32
	void playBuzzer(int fd, int pattern);		//0x34
    void getBatteryRemain(int fd);	//0x3b
};


#endif
