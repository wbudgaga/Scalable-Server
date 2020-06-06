package cs455.scaling.util;

import java.util.Random;

public class RandomData {
	public static byte[] getRandomData(int size){
		byte[] data = new byte[size];
		Random randomGenerater = new Random();
		randomGenerater.nextBytes(data);
		return data;
	}
}
