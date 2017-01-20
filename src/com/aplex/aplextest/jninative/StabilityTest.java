package com.aplex.aplextest.jninative;

public class StabilityTest {
	static{
		System.loadLibrary("aplex");
	}
	public native int StartMestTest(int sizeOfMib);
	public native int StopMemTest();
	public native int getPercent();
}
