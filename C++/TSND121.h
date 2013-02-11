//
//  TSND121.h
//  TSND
//
//  Created by H.Sakuma on 2013/01/30.
//
//

#ifndef __TSND__TSND121__
#define __TSND__TSND121__

#include <iostream>
#include "TSNDcommand.h"

class TSND121{
private:
	int accel_cycle_ms;	//加速度の計測周期[ms]
	int accel_average;	//加速度送信時の平均回数
	
	
public:
	//TSNDへコマンドを送る
	TSNDcommand command;
	
	
	//TSNDから送られてくる値
	int pressure;		//(TSNDからの)気圧[Pa]
    int accel[3];		//(TSNDからの)加速度[mG]
	int initPressure;	//初期状態の気圧[Pa]
	
	
	//TSNDからの値を基に計算する値
	float rotate[3];	//本体回転角度[deg]
	float velocity[3];	//初速[cm/s]
	float distance[3];	//前回TSND加速度受信時からの移動距離[cm]
	float height;		//気圧より求める海抜[m]
	float height_delta;	//z軸の加速度から求める高さ[cm]（初期状態が0[cm]）
	
	bool initializeP;	//計測開始時の海抜を求める際のフラグ
	
	//Getter & Setter
	void setAccelCycle(int cycle);
	int getAccelCycle();
	
	void setAccelAverage(int average);
	int getAccelAverage();
	
	
	//加速度から移動距離を求める
	void calcDistFromAccel(char xyz);
};

#endif /* defined(__TSND__TSND121__) */
