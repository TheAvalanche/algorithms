package org.symhodia.search;

public class Test {

	public static void main(String[] args) {
		short a = 10000;
		byte b = (byte) (a & 0x00FF);
		byte c = (byte) ((a & 0xFF00) >> 8);

		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}
}
