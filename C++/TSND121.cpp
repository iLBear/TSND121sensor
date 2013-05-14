//
//  TSND121.cpp
//  TSND
//
//  Created by H.Sakuma on 2013/01/30.
//
//

#include "TSND121.h"

void TSND121::calcDistFromAccel(char xyz){
	int axis;
	switch(xyz){
		case 'x':
		case 'X':
			axis = 0;
			break;

		case 'y':
		case 'Y':
			axis = 1;
			break;

		case 'z':
		case 'Z':
			axis = 2;
			break;

		default:
			printf("Err: invalid input xyz!\n");
			axis = -1;
			break;
	}
	if(axis != -1){
		//time[s]  = cycle[ms]*average
		// 		   = (cycle[s]*0.001)*average
		//		   = cycle*average*0.001[s]
		float time = getAccelCycle()*getAccelAverage()*0.001;
		
		//d[cm]        = 0.5*accel[mG]*time^2[s^2] + velocity[m/s]*time[s]
		//		       = 0.5*(accel*9.8*0.001)[cm/s^2]*time^2[s^2] + velocity[m/s]*time[s]
		//		       = 0.5*accel*9.8*0.001*time^2[cm] + velocity*time[cm]
		distance[axis] = 0.5*accel[axis]*9.8*0.001*time*time + velocity[axis]*time;
		
		//v[cm/s]      = accel[mG]*time[s] + velocity[cm/s]
		//		       = accel*9.8*0.001[cm/s^2]*time[s] + velocity[cm/s]
		//		       = accel*9.8*0.001*time[cm/s] + velocity[cm/s]
		velocity[axis] = accel[axis]*9.8*0.001*time;// + velocity[axis];
		
//		printf("time:%.3f, dist:%.3f, vel:%.3f\n", time, distance[axis], velocity[axis]);
	}
}

/* Accel Cycle */
void TSND121::setAccelCycle(int cycle){
	accel_cycle_ms = cycle;
//	printf("=====setCy:%d\n", accel_cycle_ms);
}
int TSND121::getAccelCycle(){
	return accel_cycle_ms;
}

/* Accel Average */
void TSND121::setAccelAverage(int average){
	accel_average = average;
//	printf("=====setAv:%d\n", accel_average);
}
int TSND121::getAccelAverage(){
	return accel_average;
}