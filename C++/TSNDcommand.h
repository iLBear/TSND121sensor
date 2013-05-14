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

/*==========================
  Command Length Definition
 ==========================*/
/* Send Command Length */
//0x1X
const static int CL0x10 = 4;
const static int CL0x11 = 11;
const static int CL0x12 = 4;
const static int CL0x13 = 17;
const static int CL0x14 = 4;
const static int CL0x15 = 4;
const static int CL0x16 = 6;
const static int CL0x17 = 4;
const static int CL0x18 = 6;
const static int CL0x19 = 4;
const static int CL0x1A = 6;
const static int CL0x1B = 4;
const static int CL0x1C = 5;
const static int CL0x1D = 4;
const static int CL0x1E = 8;
const static int CL0x1F = 4;
//0x2X
const static int CL0x20 = 6;
const static int CL0x21 = 4;
const static int CL0x22 = 4;
const static int CL0x23 = 4;
const static int CL0x24 = 18;
const static int CL0x25 = 4;
const static int CL0x26 = 4;
const static int CL0x27 = 18;
const static int CL0x28 = 4;
const static int CL0x29 = 15;
const static int CL0x2A = 4;
const static int CL0x2B = 15;
const static int CL0x2C = 4;
const static int CL0x2D = 4;
const static int CL0x2E = 4;
const static int CL0x2F = 4;
//0x3X
const static int CL0x30 = 7;
const static int CL0x31 = 4;
const static int CL0x32 = 4;
const static int CL0x33 = 4;
const static int CL0x34 = 4;
const static int CL0x35 = 4;
const static int CL0x36 = 4;
const static int CL0x37 = 4;
const static int CL0x38 = 4;
const static int CL0x39 = 4;
const static int CL0x3A = 4;
const static int CL0x3B = 4;
const static int CL0x3C = 4;
const static int CL0x3D = 4;
const static int CL0x3E = 4;
const static int CL0x3F = 4;

/* Event Command Length */
//0x8X
const static int CL0x80 = 25;
const static int CL0x81 = 16;
const static int CL0x82 = 12;
const static int CL0x83 = 10;
const static int CL0x84 = 12;
const static int CL0x85 = 9;
const static int CL0x86 = 16;
const static int CL0x87 = 8;
const static int CL0x88 = 4;
const static int CL0x89 = 4;

/* Response Command Length [byte] */
//0x8X
const static int CL0x8F = 4;
//0x9X
const static int CL0x90 = 33;
const static int CL0x92 = 11;
const static int CL0x93 = 16;
const static int CL0x97 = 6;
const static int CL0x99 = 6;
const static int CL0x9B = 6;
const static int CL0x9D = 5;
const static int CL0x9F = 8;
//0xAX
const static int CL0xA1 = 6;
const static int CL0xA3 = 4;
const static int CL0xA6 = 4;
const static int CL0xAA = 15;
const static int CL0xAB = 12;
const static int CL0xAD = 4;
const static int CL0xAF = 4;
//0xBX
const static int CL0xB1 = 7;
const static int CL0xB3 = 4;
const static int CL0xB6 = 4;
const static int CL0xB7 = 27;
const static int CL0xB8 = 63;
const static int CL0xB9 = 4;
const static int CL0xBA = 8;
const static int CL0xBB = 6;
const static int CL0xBC = 4;
const static int CL0xBD = 15;
const static int CL0xBE = 15;


class TSNDcommand{
private:
    int commandLength;
	void makeBCCandWrite(int fd, unsigned char *sendData);
	template <class T> T isValueInRange(T value, T min, T max, T returnIfNot, const char* msg);	//min<=value<=max?
//	int accel_cycle_ms;
//	int accel_average;
	
public:
	
	void getTime(int fd);																//0x12
    void startMeasure(int fd, int second);												//0x13
    void stopMeasure(int fd);															//0x15
    void setAccelMeasurement(int fd, int cycle_ms, int average, int recordAverage);		//0x16
	void getAccelMeasurement(int fd);													//0x17
    void setGeometricMeasurement(int fd, int cycle_ms, int average, int recordAverage);	//0x18
    void setPressureMeasurement(int fd, int cycle_ms, int average, int recordAverage);	//0x1a
	void setBatteryMeasurement(int fd, bool send, bool record);							//0x1c
	void setIOMeasurement(int fd, int cycle_ms, int average, int recordAverage, bool sendEdgeData, bool recordEdgaData);	//0x1e
	
	void setAccelRange(int fd, int range);													//0x22
	void reviseAccelMeasurement(int fd, int xt, int yt, int zt, int xx, int yy, int zz);	//0x24
	void calibrateGeometricSensor(int fd);													//0x28
    void setOptionButtonMode(int fd, int mode);												//0x2c
    
	void setExternalIO(int fd, int terminal1, int terminal2, int terminal3, int terminal4);	//0x30
	void setBuzzerVolume(int fd, int volume);												//0x32
	void playBuzzer(int fd, int pattern);													//0x34
    void getBatteryRemain(int fd);															//0x3b
};


#endif
