package javaSample;

import java.io.OutputStream;
import java.io.*;

public class TSND_SendCommand {
	//データ送信
	public void sendDatum(OutputStream out, byte[] sendData){
		try{
			out.write(sendData);
		}catch(IOException e){
			//fizz
		}
		System.out.println("# send command 0x"+Integer.toHexString(sendData[1]));
		try{
			out.close();
		}catch(IOException e){
			//buzz
		}
	}
	
	//機器情報取得
	public void getSensorData(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x10;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//時刻設定（年(2000年からの経過年数)，月，日，時，分，秒，ミリ秒）
	public void setTime(OutputStream out, int year, int month, int day, int hour, int min, int sec, int msec){
		int commandLength = 11;
		if(year < 0 || year > 99)	year = 0;
		if(month < 1 || month > 12)	month = 1;
		if(day < 1 || day > 31)		day = 1;
		if(hour < 0 || hour > 23)	hour = 0;
		if(min < 0 || min > 59)		min = 0;
		if(sec < 0 || sec > 59)		sec = 0;
		if(msec < 0 || msec > 999)	msec = 0;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x11;
		sendData[2] = (byte)year;
		sendData[3] = (byte)month;
		sendData[4] = (byte)day;
		sendData[5] = (byte)hour;
		sendData[6] = (byte)min;
		sendData[7] = (byte)sec;
		sendData[8] = (byte)(msec%16);	//リトルエンディアン
		sendData[9] = (byte)(msec/16);
		sendData[10] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//時刻取得
	public void getTime(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x12;		//Command Code		  [1 byte]
		sendData[2] = (byte)0x00;		//Parameter			  [1-264 byte]
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//計測開始（計測時間[秒]）
	public void startMeasure(OutputStream out, int sec){
		int commandLength = 17;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;	//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x13;	//Command Code		  [1 byte]
		sendData[2] = (byte)0x00;	//相対(0) or 絶対時刻(1)
		sendData[3] = (byte)0x0c;	//開始年
		sendData[4] = (byte)0x0c;	//開始月
		sendData[5] = (byte)0x01;	//開始日
		sendData[6] = (byte)0x00;	//開始時
		sendData[7] = (byte)0x00;	//開始分
		sendData[8] = (byte)0x00;	//開始秒
		sendData[9] = (byte)0x00;	//相対(0) or 絶対時刻(1)
		sendData[10] = (byte)0x0c;	//終了年
		sendData[11] = (byte)0x0c;	//終了月
		sendData[12] = (byte)0x01;	//終了日
		sendData[13] = (byte)0x00;	//終了時
		sendData[14] = (byte)0x00;	//終了分
		sendData[15] = (byte)sec;	//終了秒
		sendData[16] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);	//BCC
		}
		sendDatum(out, sendData);
	}
	//計測開始（計測時間[秒]）
	public void startMeasure(OutputStream out,
			int sMode, int sYear, int sMon, int sDay, int sHour, int sMin, int sSec, 
			int eMode, int eYear, int eMon, int eDay, int eHour, int eMin, int eSec
			){
		//データチェック
		if(sYear < 0 || sYear > 99)	sYear = 0;
		if(sMon < 1 || sMon > 12)	sMon = 1;
		if(sDay < 1 || sDay > 31)	sDay = 1;
		if(sHour < 0 || sHour > 23)	sHour = 0;
		if(sMin < 0 || sMin > 59)	sMin = 0;
		if(sSec < 0 || sSec > 59)	sSec = 0;
		if(eYear < 0 || eYear > 99)	eYear = 0;
		if(eMon < 1 || eMon > 12)	eMon = 1;
		if(eDay < 1 || eDay > 31)	eDay = 1;
		if(eHour < 0 || eHour > 23)	eHour = 0;
		if(eMin < 0 || eMin > 59)	eMin = 0;
		if(eSec < 0 || eSec > 59)	eSec = 0;
//		System.out.println("invalid input!");
		//日付の前後関係がおかしい時のエラー表示
//		System.out.println("invalid input!");			
		
		int commandLength = 17;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;	//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x13;	//Command Code		  [1 byte]
		sendData[2] = (byte)sMode;	//相対(0) or 絶対時刻(1)
		sendData[3] = (byte)sYear;	//開始年
		sendData[4] = (byte)sMon;	//開始月
		sendData[5] = (byte)sDay;	//開始日
		sendData[6] = (byte)sHour;	//開始時
		sendData[7] = (byte)sMin;	//開始分
		sendData[8] = (byte)sSec;	//開始秒
		sendData[9] = (byte)eMode;	//相対(0) or 絶対時刻(1)
		sendData[10] = (byte)eYear;	//終了年
		sendData[11] = (byte)eMon;	//終了月
		sendData[12] = (byte)eDay;	//終了日
		sendData[13] = (byte)eHour;	//終了時
		sendData[14] = (byte)eMin;	//終了分
		sendData[15] = (byte)eSec;	//終了秒
		sendData[16] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);	//BCC
		}
		sendDatum(out, sendData);
	}
	
	//計測予約確認
	public void checkReserve(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x14;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//計測停止
	public void stopMeasure(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x15;		//Command Code		  [1 byte]
		sendData[2] = (byte)0x00;		//Parameter			  [1-264 byte]
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}	
	
	//加速度・角速度計測設定（計測周期[ms]，データ平均回数[回]）
	public void setAccelMeasurement(OutputStream out, int cycle, int ave, int recordAve){
		if(cycle < 0 || cycle > 255)
			cycle = 20;
		if(ave < 0 || ave > 255)
			ave = 10;
		if(recordAve < 0 || recordAve > 255)
			recordAve = 10;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed)		[1 byte]
		sendData[1] = (byte)0x16;		//Command Code				[1 byte]
		sendData[2] = (byte)cycle;		//計測周期 					[1-255ms]
		sendData[3] = (byte)ave;		//計測データ送信設定・平均回数 	[1-255回]
		sendData[4] = (byte)recordAve;	//データ記録時平均回数設定 		[0:しない, 1-255:する[回]]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//加速度・角速度計測設定取得
	public void getAccelMeasurement(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x17;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//地磁気計測設定（計測周期[ミリ秒]，データ平均回数[回]）
	public void setGeomagnetismMeasurement(OutputStream out, int cycle, int ave, int recordAve){
		if(cycle < 0 || cycle > 255)			cycle = 20;
		if(ave < 0 || ave > 255)				ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x18;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//計測周期 					[0-255ms]
		sendData[3] = (byte)ave;		//計測データ送信設定・平均回数 	[1-255回]
		sendData[4] = (byte)recordAve;	//データ記録設定 				[0:しない, 1-255:する[回]]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//地磁気計測設定取得
	public void getGeomagnetismMeasurement(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x19;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//気圧計測設定（計測周期[ミリ秒]，データ平均回数[回]）
	public void setPressureMeasurement(OutputStream out, int cycle, int ave, int recordAve){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		if(ave < 4 || ave > 255)		ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x1a;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//計測周期÷10 				[40-2550ms]
		sendData[3] = (byte)ave;		//計測データ送信設定・平均回数 	[1-255回]
		sendData[4] = (byte)recordAve;	//データ記録設定 				[0:しない]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//気圧計測設定取得
	public void getPressureMeasurement(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x1b;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//バッテリ電圧計測設定
	public void setVoltageMeasurement(OutputStream out, boolean send, boolean record){
		int commandLength = 5;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x1c;
		if(send == false){
			sendData[2] = (byte)0x00;	//送信しない
		}else{
			sendData[2] = (byte)0x01;	//送信する
		}
		if(record == false){
			sendData[3] = (byte)0x00;	//記録しない
		}else{
			sendData[3] = (byte)0x01;	//記録する
		}
		sendData[4] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//バッテリ電圧計測設定取得
	public void getVoltageMeasurement(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x1d;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//外部拡張端子計測＆エッジデータ出力設定
	public void setExternIOMeasurement(OutputStream out, int cycle, int ave, int recordAve, boolean send, boolean record){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		if(ave < 4 || ave > 255)		ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 8;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x1e;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//計測周期(2ms刻み) 			[0-254ms]
		sendData[3] = (byte)ave;		//計測データ送信設定・平均回数 	[1-255回]
		sendData[4] = (byte)recordAve;	//データ記録設定 				[0-255ms]
		if(send == false){
			sendData[5] = (byte)0x00;	//送信しない
		}else{
			sendData[5] = (byte)0x01;	//送信する
		}
		if(record == false){
			sendData[6] = (byte)0x00;	//記録しない
		}else{
			sendData[6] = (byte)0x01;	//記録する
		}
		sendData[7] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//外部拡張端子計測＆エッジデータ出力設定取得
	public void getExternIOMeasurement(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x1f;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}

	//外部拡張I2C通信設定
	public void setExternI2CCommunication(OutputStream out, int cycle, boolean send, boolean record){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x20;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//計測周期(2ms刻み) 			[0-254ms]
		if(send == false){
			sendData[3] = (byte)0x00;	//送信しない
		}else{
			sendData[3] = (byte)0x01;	//送信する
		}
		if(record == false){
			sendData[4] = (byte)0x00;	//記録しない
		}else{
			sendData[4] = (byte)0x01;	//記録する
		}
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//外部拡張I2C通信取得
	public void getExternI2CCommunication(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x21;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//加速度センサ計測レンジ設定
	public void setAccelRange(OutputStream out, int range){
		if(range < 0 || range > 3)	range = 0;

		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x22;
		sendData[2] = (byte)range;		//0:±2G, 1:±4G, 2:±8G, 3:±16G
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//加速度センサ計測レンジ設定取得
	public void getAccelRange(OutputStream out){
		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x23;
		sendData[2] = (byte)0x00;
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	


}