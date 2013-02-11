package javaSample;

import java.io.OutputStream;
import java.io.*;

public class TSND_SendCommand {
	//�f�[�^���M
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
	
	//�@����擾
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
	
	//�����ݒ�i�N(2000�N����̌o�ߔN��)�C���C���C���C���C�b�C�~���b�j
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
		sendData[8] = (byte)(msec%16);	//���g���G���f�B�A��
		sendData[9] = (byte)(msec/16);
		sendData[10] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//�����擾
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
	
	//�v���J�n�i�v������[�b]�j
	public void startMeasure(OutputStream out, int sec){
		int commandLength = 17;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;	//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x13;	//Command Code		  [1 byte]
		sendData[2] = (byte)0x00;	//����(0) or ��Ύ���(1)
		sendData[3] = (byte)0x0c;	//�J�n�N
		sendData[4] = (byte)0x0c;	//�J�n��
		sendData[5] = (byte)0x01;	//�J�n��
		sendData[6] = (byte)0x00;	//�J�n��
		sendData[7] = (byte)0x00;	//�J�n��
		sendData[8] = (byte)0x00;	//�J�n�b
		sendData[9] = (byte)0x00;	//����(0) or ��Ύ���(1)
		sendData[10] = (byte)0x0c;	//�I���N
		sendData[11] = (byte)0x0c;	//�I����
		sendData[12] = (byte)0x01;	//�I����
		sendData[13] = (byte)0x00;	//�I����
		sendData[14] = (byte)0x00;	//�I����
		sendData[15] = (byte)sec;	//�I���b
		sendData[16] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);	//BCC
		}
		sendDatum(out, sendData);
	}
	//�v���J�n�i�v������[�b]�j
	public void startMeasure(OutputStream out,
			int sMode, int sYear, int sMon, int sDay, int sHour, int sMin, int sSec, 
			int eMode, int eYear, int eMon, int eDay, int eHour, int eMin, int eSec
			){
		//�f�[�^�`�F�b�N
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
		//���t�̑O��֌W�������������̃G���[�\��
//		System.out.println("invalid input!");			
		
		int commandLength = 17;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;	//Header (0x9a fixed) [1 byte]
		sendData[1] = (byte)0x13;	//Command Code		  [1 byte]
		sendData[2] = (byte)sMode;	//����(0) or ��Ύ���(1)
		sendData[3] = (byte)sYear;	//�J�n�N
		sendData[4] = (byte)sMon;	//�J�n��
		sendData[5] = (byte)sDay;	//�J�n��
		sendData[6] = (byte)sHour;	//�J�n��
		sendData[7] = (byte)sMin;	//�J�n��
		sendData[8] = (byte)sSec;	//�J�n�b
		sendData[9] = (byte)eMode;	//����(0) or ��Ύ���(1)
		sendData[10] = (byte)eYear;	//�I���N
		sendData[11] = (byte)eMon;	//�I����
		sendData[12] = (byte)eDay;	//�I����
		sendData[13] = (byte)eHour;	//�I����
		sendData[14] = (byte)eMin;	//�I����
		sendData[15] = (byte)eSec;	//�I���b
		sendData[16] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);	//BCC
		}
		sendDatum(out, sendData);
	}
	
	//�v���\��m�F
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
	
	//�v����~
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
	
	//�����x�E�p���x�v���ݒ�i�v������[ms]�C�f�[�^���ω�[��]�j
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
		sendData[2] = (byte)cycle;		//�v������ 					[1-255ms]
		sendData[3] = (byte)ave;		//�v���f�[�^���M�ݒ�E���ω� 	[1-255��]
		sendData[4] = (byte)recordAve;	//�f�[�^�L�^�����ω񐔐ݒ� 		[0:���Ȃ�, 1-255:����[��]]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//�����x�E�p���x�v���ݒ�擾
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
	
	//�n���C�v���ݒ�i�v������[�~���b]�C�f�[�^���ω�[��]�j
	public void setGeomagnetismMeasurement(OutputStream out, int cycle, int ave, int recordAve){
		if(cycle < 0 || cycle > 255)			cycle = 20;
		if(ave < 0 || ave > 255)				ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x18;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//�v������ 					[0-255ms]
		sendData[3] = (byte)ave;		//�v���f�[�^���M�ݒ�E���ω� 	[1-255��]
		sendData[4] = (byte)recordAve;	//�f�[�^�L�^�ݒ� 				[0:���Ȃ�, 1-255:����[��]]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//�n���C�v���ݒ�擾
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
	
	//�C���v���ݒ�i�v������[�~���b]�C�f�[�^���ω�[��]�j
	public void setPressureMeasurement(OutputStream out, int cycle, int ave, int recordAve){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		if(ave < 4 || ave > 255)		ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x1a;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//�v��������10 				[40-2550ms]
		sendData[3] = (byte)ave;		//�v���f�[�^���M�ݒ�E���ω� 	[1-255��]
		sendData[4] = (byte)recordAve;	//�f�[�^�L�^�ݒ� 				[0:���Ȃ�]
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//�C���v���ݒ�擾
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
	
	//�o�b�e���d���v���ݒ�
	public void setVoltageMeasurement(OutputStream out, boolean send, boolean record){
		int commandLength = 5;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x1c;
		if(send == false){
			sendData[2] = (byte)0x00;	//���M���Ȃ�
		}else{
			sendData[2] = (byte)0x01;	//���M����
		}
		if(record == false){
			sendData[3] = (byte)0x00;	//�L�^���Ȃ�
		}else{
			sendData[3] = (byte)0x01;	//�L�^����
		}
		sendData[4] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//�o�b�e���d���v���ݒ�擾
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
	
	//�O���g���[�q�v�����G�b�W�f�[�^�o�͐ݒ�
	public void setExternIOMeasurement(OutputStream out, int cycle, int ave, int recordAve, boolean send, boolean record){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		if(ave < 4 || ave > 255)		ave = 10;
		if(recordAve < 0 || recordAve > 255)	recordAve = 10;
		
		int commandLength = 8;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x1e;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//�v������(2ms����) 			[0-254ms]
		sendData[3] = (byte)ave;		//�v���f�[�^���M�ݒ�E���ω� 	[1-255��]
		sendData[4] = (byte)recordAve;	//�f�[�^�L�^�ݒ� 				[0-255ms]
		if(send == false){
			sendData[5] = (byte)0x00;	//���M���Ȃ�
		}else{
			sendData[5] = (byte)0x01;	//���M����
		}
		if(record == false){
			sendData[6] = (byte)0x00;	//�L�^���Ȃ�
		}else{
			sendData[6] = (byte)0x01;	//�L�^����
		}
		sendData[7] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//�O���g���[�q�v�����G�b�W�f�[�^�o�͐ݒ�擾
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

	//�O���g��I2C�ʐM�ݒ�
	public void setExternI2CCommunication(OutputStream out, int cycle, boolean send, boolean record){
		if(cycle < 0 || cycle > 255)	cycle = 20;
		
		int commandLength = 6;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;		//Header (0x9a fixed) 		[1 byte]
		sendData[1] = (byte)0x20;		//Command Code		  		[1 byte]
		sendData[2] = (byte)cycle;		//�v������(2ms����) 			[0-254ms]
		if(send == false){
			sendData[3] = (byte)0x00;	//���M���Ȃ�
		}else{
			sendData[3] = (byte)0x01;	//���M����
		}
		if(record == false){
			sendData[4] = (byte)0x00;	//�L�^���Ȃ�
		}else{
			sendData[4] = (byte)0x01;	//�L�^����
		}
		sendData[5] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
	}
	
	//�O���g��I2C�ʐM�擾
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
	
	//�����x�Z���T�v�������W�ݒ�
	public void setAccelRange(OutputStream out, int range){
		if(range < 0 || range > 3)	range = 0;

		int commandLength = 4;
		byte[] sendData = new byte[commandLength];
		sendData[0] = (byte)0x9a;
		sendData[1] = (byte)0x22;
		sendData[2] = (byte)range;		//0:�}2G, 1:�}4G, 2:�}8G, 3:�}16G
		sendData[3] = (byte)0x00;
		for(int i = 0; i < commandLength-1; i++){
			sendData[commandLength-1] = (byte)(sendData[commandLength-1] ^ sendData[i]);
		}
		sendDatum(out, sendData);
	}
	
	//�����x�Z���T�v�������W�ݒ�擾
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