package com.hyts.assemble.uuidkey.snowflake;

import java.util.Date;

public class TimeUtil {

	public static long twepoch = 1288834974657L;

	public static long timeGen() {
		return System.currentTimeMillis() - twepoch;
	}
	
	public static long timeGen(Date date) {
		return date.getTime() - twepoch;
	}
	
	public static long timeGen(long time) {
		return time - twepoch;
	}

	public static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	public static void main(String[] args){
		System.out.println(timeGen());
		System.out.println(tilNextMillis(System.currentTimeMillis()));
	}
}
