package org.symhodia.search;

import java.io.IOException;

public class BinarySearch {
	
	public int find(int value, int[] array) {
		int lo = 0;
		int hi = array.length - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (value < array[mid]) {
				hi = mid - 1;
			} else if (value > array[mid]) {
				lo = mid + 1;
			} else {
				return mid;
			}
		}
		return -1;
	}
	
	static class BinarySearchObj {
		int lo;
		int hi;
		int mid;
	}

	public int findObj(BinarySearchObj obj, int value, int[] array) {
		obj.lo = 0;
		obj.hi = array.length - 1;
		while (obj.lo <= obj.hi) {
			obj.mid = obj.lo + (obj.hi - obj.lo) / 2;
			allowNext(obj, value, array);
			if (value < array[obj.mid]) {
				obj.hi = obj.mid - 1;
				allowNext(obj, value, array);
			} else if (value > array[obj.mid]) {
				obj.lo = obj.mid + 1;
				allowNext(obj, value, array);
			} else {
				found(obj, value, array);
				return obj.mid;
			}			
			//allowNext(obj, value, array);
		}
		return -1;
	}
	
	public void allowNext(BinarySearchObj obj, int value, int[] array) {
		try {
			for (int i = 0; i <= array.length - 1; i ++) {
				System.out.print(array[i]);
				if (obj.lo == i) {
					System.out.print("l");
				}
				if (obj.hi == i) {
					System.out.print("h");
				}
				if (obj.mid == i) {
					System.out.print("m");
				}
				System.out.print(" ");
			}
			
			int input = System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void found(BinarySearchObj obj, int value, int[] array) {
		try {
			for (int i = 0; i <= array.length - 1; i ++) {
				System.out.print(array[i]);
				if (obj.mid == i) {
					System.out.print("!");
				}
				System.out.print(" ");
			}

			int input = System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new BinarySearch().findObj(new BinarySearchObj(), 3, new int[]{1,2,3,4,5,6,7,8,9});
	}
}
