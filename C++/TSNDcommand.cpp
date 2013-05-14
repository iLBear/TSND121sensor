//
//  TSNDcommand.cpp
//  Created by iLBe@r on 2013/01/25.
//

#include "TSNDcommand.h"

/* 入力値valueがmin以上max以下かどうか．そうでない場合はreturnIfNotの値を返す */
template <class T> T TSNDcommand::isValueInRange(T value, T min, T max, T returnIfNot, const char* msg) {
	if(value < min || value > max){
		printf(" [ERROR] Invalid value in %s\n", msg);
		return returnIfNot;
	}else{			//value >= min && value <= max
		return value;
	}
}

/* パケットの最後に詰めるBCC[1byte]を生成し、writeする */
void TSNDcommand::makeBCCandWrite(int fd, unsigned char *sendData){
	sendData[commandLength-1] = 0x00;
    for(int i = 0; i < commandLength-1; i++){
        sendData[commandLength-1] = (sendData[commandLength-1] ^ sendData[i]);	//BCC
    }
	(void)write(fd, sendData, commandLength);
}


/*======
 0x12
 時刻取得
 ======*/
void TSNDcommand::getTime(int fd){
    commandLength = CL0x12;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x12;
    sendData[2] = 0x00;
    makeBCCandWrite(fd, sendData);
	printf("- send[0x12]: getTime\n");
}


/*=====================
 0x13
 計測開始（改変版）
 [measure_sec: 計測時間]
	10~:計測継続時間[s]
 =====================*/
void TSNDcommand::startMeasure(int fd, int measure_sec){
	measure_sec = isValueInRange(measure_sec, 10, INT_MAX, 10, "startMeasure");
	
    commandLength = CL0x13;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;         //Header (0x9a fixed) [1 byte]
    sendData[1] = 0x13;         //Command Code		  [1 byte]
    sendData[2] = 0x00;         //相対(0) or 絶対時刻(1)
    sendData[3] = 0x0c;         //開始年
    sendData[4] = 0x0c;         //開始月
    sendData[5] = 0x01;         //開始日
    sendData[6] = 0x00;         //開始時
    sendData[7] = 0x00;         //開始分
    sendData[8] = 0x00;         //開始秒
    sendData[9] = 0x00;         //相対(0) or 絶対時刻(1)
    sendData[10] = 0x0c;        //終了年
    sendData[11] = 0x0c;        //終了月
    sendData[12] = 0x01;        //終了日
    sendData[13] = measure_sec/3600;	//終了時
    sendData[14] = (measure_sec/60)%60; //終了分
    sendData[15] = measure_sec%60;		//終了秒
    sendData[16] = 0x00;
    makeBCCandWrite(fd, sendData);
    printf("- send[0x13]: startMeasure\n");
}


/*======
 0x15
 計測終了
 ======*/
void TSNDcommand::stopMeasure(int fd){
    commandLength = CL0x15;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x15;
    sendData[2] = 0x00;
    makeBCCandWrite(fd, sendData);
    printf("- send[0x15]: stopMeasure\n");
}


/*===============================
 0x16
 加速／角速度計測設定
 [cycle_ms: 計測OFF or 計測周期]
	0:計測OFF
	1~255:計測周期[ms]
 [average: 計測データ送信設定]
	0:送信しない
	1~255:平均回数[回]
 [recordAverage: 計測データ記録設定]
	0:記録しない
	1~255:平均回数[回]
 ===============================*/
void TSNDcommand::setAccelMeasurement(int fd, int cycle_ms, int average, int recordAverage){
	static const char* msg = "setAccelMeasurement";
	cycle_ms = isValueInRange(cycle_ms, 0, 255, 20, msg);
	average = isValueInRange(average, 0, 255, 10, msg);
	recordAverage = isValueInRange(recordAverage, 0, 255, 0, msg);
	
    commandLength = CL0x16;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;             //Header (0x9a fixed)		[1 byte]
    sendData[1] = 0x16;             //Command Code				[1 byte]
    sendData[2] = cycle_ms;         //計測周期 					[1-255ms]
    sendData[3] = average;          //計測データ送信設定・平均回数 	[1-255回]
    sendData[4] = recordAverage;	//データ記録時平均回数設定 		[0:しない, 1-255:する[回]]
    sendData[5] = 0x00;
    makeBCCandWrite(fd, sendData);
    printf("- send[0x16]: setAccelMeasurement\n");
}

/*==============
 0x17
 加速度計速設定取得
 ==============*/
void TSNDcommand::getAccelMeasurement(int fd){
    commandLength = CL0x17;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;             //Header (0x9a fixed)		[1 byte]
    sendData[1] = 0x17;             //Command Code				[1 byte]
    sendData[2] = 0x00;
    makeBCCandWrite(fd, sendData);
    printf("- send[0x17]: getAccelMeasurement\n");
}


/*===============================
 0x18
 地磁気計測設定
 [cycle_ms: 計測OFF or 計測周期]
	0:計測OFF
	10~255:計測周期[ms]
 [average: 計測データ送信設定]
	0:送信しない
	1~255:平均回数[回]
 [recordAverage: 計測データ記録設定]
	0:記録しない
	1~255:平均回数[回]
 ===============================*/
void TSNDcommand::setGeometricMeasurement(int fd, int cycle_ms, int average, int recordAverage){
	static const char* msg = "setGeometricMeasurement";
	if(cycle_ms != 0){
		cycle_ms = isValueInRange(cycle_ms, 10, 255, 20, msg);
	}
	average = isValueInRange(average, 0, 255, 10, msg);
	recordAverage = isValueInRange(recordAverage, 0, 255, 0, msg);
	
    commandLength = CL0x18;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;             //Header (0x9a fixed) 		[1 byte]
    sendData[1] = 0x18;             //Command Code		  		[1 byte]
    sendData[2] = cycle_ms;         //計測周期 					[0-255ms]
    sendData[3] = average;          //計測データ送信設定・平均回数 	[1-255回]
    sendData[4] = recordAverage;	//データ記録設定 				[0:しない, 1-255:する[回]]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x18]: setGeometricMeasurement\n");
}


/*===============================
 0x1A
 気圧計測設定
 [cycle_ms: 計測OFF or 計測周期]
	0:計測OFF
	4~255:計測周期[10ms]
 [average: 計測データ送信設定]
	0:送信しない
	1~255:平均回数[回]
 [recordAverage: 計測データ記録設定]
	0:記録しない
	1~255:平均回数[回]
 ===============================*/
void TSNDcommand::setPressureMeasurement(int fd, int cycle_ms, int average, int recordAverage){
	static const char* msg = "setPressureMeasurement";
	if(cycle_ms != 0){
		cycle_ms = isValueInRange(cycle_ms, 4, 255, 4, msg);
	}
	average = isValueInRange(average, 0, 255, 10, msg);
	recordAverage = isValueInRange(recordAverage, 0, 255, 10, msg);
	
    commandLength = CL0x1A;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;             //Header (0x9a fixed) 		[1 byte]
    sendData[1] = 0x1a;             //Command Code		  		[1 byte]
    sendData[2] = cycle_ms;         //計測周期÷10 				[40-2550ms]
    sendData[3] = average;          //計測データ送信設定・平均回数 	[1-255回]
    sendData[4] = recordAverage;	//データ記録設定 				[0:しない]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x1a]: setPressureMeasurement\n");
}


/*========================
 0x1C
 バッテリ電圧計測設定
 [send: 計測データ送信設定]
	0:送信しない
	1:送信する
 [record: 計測データ記録設定]
	0:記録しない
	1:記録する
 ========================*/
void TSNDcommand::setBatteryMeasurement(int fd, bool send, bool record){
    commandLength = CL0x1C;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x1C;
    /*
     true = 1; false = 0;であるので
     sendData[2] = send; sendData[3] = record;
     でも良いと思われるが，念のため．
     */
    if(send){
        sendData[2] = 0x01;
    }else{
        sendData[2] = 0x00;
    }
    if(record){
        sendData[3] = 0x01;
    }else{
        sendData[3] = 0x00;
    }
    makeBCCandWrite(fd, sendData);
    printf("- send[0x1C]: setBatteryMeasurement\n");
}


/*===============================
 0x1E
 外部拡張端子計測＆エッジデータ出力設定
 [cycle_ms: 計測OFF or 計測周期]
	0:計測OFF
	2~254:計測周期[ms]（2[ms]刻み）
 [average: 計測データ送信設定]
	0:送信しない
	1~255:平均回数[回]
 [recordAverage: 計測データ記録設定]
	0:記録しない
	1~255:平均回数[回]
 [sendEdgeData: エッジデータ送信設定]
	true(1):送信する
	false(0):送信しない
 [recordEdgeData: エッジデータ記録設定]
	true(1):記録する
	false(0):記録しない
 ===============================*/
void TSNDcommand::setIOMeasurement(int fd, int cycle_ms, int average, int recordAverage, bool sendEdgeData, bool recordEdgaData){
	static const char* msg = "setIOMeasurement";
	cycle_ms = isValueInRange(cycle_ms, 0, 254, 20, msg);
	if(cycle_ms%2 == 1){	//if(cycle_ms == ODD)
		cycle_ms++;			//cycle_ms -> EVEN
	}
	average = isValueInRange(average, 0, 255, 10, msg);
	recordAverage = isValueInRange(recordAverage, 0, 255, 10, msg);
	
    commandLength = CL0x1E;
    unsigned char sendData[commandLength];
	
    sendData[0] = 0x9a;
    sendData[1] = 0x1e;
    sendData[2] = cycle_ms;         //計測周期					[0:計測しない,1-255:計測周期(ms)]
    sendData[3] = average;          //計測データ送信設定・平均回数 	[1-255回]
    sendData[4] = recordAverage;	//データ記録設定 				[0:しない]
	if(sendEdgeData){
        sendData[5] = 0x01;
    }else{
        sendData[5] = 0x00;
    }
    if(recordEdgaData){
        sendData[6] = 0x01;
    }else{
        sendData[6] = 0x00;
    }
	
    makeBCCandWrite(fd, sendData);
    printf("- send[0x1e]: setIOMeasurement\n");
}


/*==================
 0x22
 加速度計測レンジ設定
 [range: 加速度レンジ]
	0:±2G
	1:±4G
	2:±8G
	3:±16G
 ==================*/
void TSNDcommand::setAccelRange(int fd, int range){
	range = isValueInRange(range, 0, 3, 0, "setAccelRange");
	
    commandLength = CL0x22;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x22;
    sendData[2] = range;	//レンジ[0:±2G, 1:±4G, 2:±8G, 3:±16G]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x22]: setAccelRange\n");
}


/*========================
 0x24
 加速度センサ補正設定
 [xt,yt,zt: xyz軸補正目標値]
	0:補正なし（補正値クリア）
	1:0G
	2:1G
	3:-1G
	4:絶対値指定
 [xx,yy,zz: xyz軸補正]
	-20000~20000[0.1mG単位]
 ========================*/
void TSNDcommand::reviseAccelMeasurement(int fd, int xt, int yt, int zt, int xx, int yy, int zz){
	static const char* msg = "reviseAccelMeasurement";
	xt = isValueInRange(xt, 0, 4, 0, msg);
	yt = isValueInRange(yt, 0, 4, 0, msg);
	zt = isValueInRange(xt, 0, 4, 0, msg);
	xx = isValueInRange(xx, -20000, 20000, 0, msg);
	yy = isValueInRange(yy, -20000, 20000, 0, msg);
	zz = isValueInRange(zz, -20000, 20000, 0, msg);
	
    commandLength = CL0x24;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x24;
    sendData[2] = xt;	 //x軸補正目標値[0:補正なし, 1:0G, 2:1G, 3:-1G, 4:絶対値指定]
	sendData[3] = yt;	 //y軸補正目標値
	sendData[4] = zt;	 //z軸補正目標値
	
	if(xx < 0){				//x軸補正(-20000~20000[0.1mG]), 4byte
		sendData[5] = -xx%256;
		sendData[6] = -xx/256;
		sendData[7] = 0xff;
		sendData[8] = 0xff;
	}else{
		sendData[5] = xx%256;
		sendData[6] = xx/256;
		sendData[7] = 0x00;
		sendData[8] = 0x00;
	}
	
	if(yy < 0){				//y軸補正(-20000~20000[mG]), 4byte
		sendData[9]  = -yy%256;
		sendData[10] = -yy/256;
		sendData[11] = 0xff;
		sendData[12] = 0xff;
	}else{
		sendData[9]	 = yy%256;
		sendData[10] = yy/256;
		sendData[11] = 0x00;
		sendData[12] = 0x00;
	}
	
	if(zz < 0){				//z軸補正(-20000~20000[mG]), 4byte
		sendData[13] = -zz%256;
		sendData[14] = -zz/256;
		sendData[15] = 0xff;
		sendData[16] = 0xff;
	}else{
		sendData[13] = zz%256;
		sendData[14] = zz/256;
		sendData[15] = 0x00;
		sendData[16] = 0x00;
	}
	
    makeBCCandWrite(fd, sendData);
    printf("- send[0x24]: collectAccelMeasurement\n");
}

/*========================
 0x28
 地磁気センサキャリブレーション
 ========================*/
void TSNDcommand::calibrateGeometricSensor(int fd){
	commandLength = CL0x28;
	unsigned char sendData[commandLength];
	sendData[0] = 0x9a;
	sendData[1] = 0x28;
	sendData[2] = 0x00;
	makeBCCandWrite(fd, sendData);
    printf("- send[0x28]: calibrateGeometricSensor\n");
}


/*============================
 0x2C
 オプションボタン長押し時の動作設定
 [mode: 動作モード]
	0:操作無効
	1:計測停止
	2:計測開始/停止
	3:計測中イベント通知
	4:計測中イベント通知＋ブザー音
 ============================*/
void TSNDcommand::setOptionButtonMode(int fd, int mode){
    if(mode < 0 | mode > 4)     mode = 0;
	mode = isValueInRange(mode, 0, 4, 0, "setOptionButtonMode");
	
    commandLength = CL0x2C;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x2C;
    sendData[2] = mode;             //モード[0-4]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x2C]: setOptionButtonMode\n");
}


/*==============================================
 0x30
 外部拡張端子設定
 [terminalX: 外部端子モード凡例]
	0:未使用端子
	1:入力端子
	2:立ち下りエッジ検出機能付き入力端子
	3:立ち上りエッジ検出機能付き入力端子
	4:両エッジ検出機能付き入力端子
	5:立ち下りエッジ検出＋チャタリング除去機能付き入力端子
	6:立ち上りエッジ検出＋チャタリング除去機能付き入力端子
	7:両エッジ検出＋チャタリング除去機能付き入力端子
	8:Low入力
	9:High入力
	10:AD入力（外部端子3, 4のみ）
 ==============================================*/
void TSNDcommand::setExternalIO(int fd, int terminal1, int terminal2, int terminal3, int terminal4){
	static const char* msg = "setExternalIO";
	terminal1 = isValueInRange(terminal1, 0, 9, 0, msg);
	terminal2 = isValueInRange(terminal2, 0, 9, 0, msg);
	terminal3 = isValueInRange(terminal3, 0, 10, 0, msg);
	terminal4 = isValueInRange(terminal4, 0, 10, 0, msg);
	
	commandLength = CL0x30;
	unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x30;
    sendData[2] = terminal1;    //外部端子1モード
    sendData[3] = terminal2;    //外部端子2モード
    sendData[4] = terminal3;	//外部端子3モード
	sendData[5] = terminal4;	//外部端子4モード
    makeBCCandWrite(fd, sendData);
    printf("- send[0x30]: setExternalIO\n");
}

/*================
 0x32
 ブザー音量設定
 [volume]
	0:消音, 1:小, 2:大
 ================*/
void TSNDcommand::setBuzzerVolume(int fd, int volume){
	volume = isValueInRange(volume, 0, 2, 0, "setBuzzerVolume");
	
    commandLength = CL0x32;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x32;
    sendData[2] = volume;		//ボリューム[0:消音, 1:小, 2:大]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x32]: getBuzzerVolume\n");
}

/*=========================
 0x34
 ブザー再生
 [pattern: ブザー鳴動パターン]
	0:低音、短音1回
	1:高音、短音1回
	2:低音、短音2回
	3:高音、短音3回
	4:低音、長音3回
	5:高音、長音3回
	6:低音、長音1回
	7:高音、長音1回
 =========================*/
void TSNDcommand::playBuzzer(int fd, int pattern){
	pattern = isValueInRange(pattern, 0, 7, 0, "playBuzzer");
	
    commandLength = CL0x34;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x34;
    sendData[2] = pattern;		//ブザー再生パターン[0~7]
    makeBCCandWrite(fd, sendData);
    printf("- send[0x34]: playBuzzer\n");
}

/*=============
 0x3B
 バッテリ状態取得
 =============*/
void TSNDcommand::getBatteryRemain(int fd){
    commandLength = CL0x3B;
    unsigned char sendData[commandLength];
    sendData[0] = 0x9a;
    sendData[1] = 0x3B;
    sendData[2] = 0x00;
    makeBCCandWrite(fd, sendData);
    printf("- send[0x3B]: getBatteryRemain\n");
}