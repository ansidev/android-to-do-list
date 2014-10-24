package com.android.ansidev.todolist;

import java.util.ArrayList;


public class Functions {
	public static void swapItem(ArrayList<String> arrList, int pos1, int pos2) {
		String temp = arrList.get(pos1);
		arrList.set(pos1, arrList.get(pos2));
		arrList.set(pos2, temp);
	}
	
	public static void moveTop(ArrayList<String> arrList, int pos) {
		arrList.add(0, arrList.get(pos));
		arrList.remove(pos + 1);
		
	}
	
	public static void moveBottom(ArrayList<String> arrList, int pos) {
		arrList.add(arrList.get(pos));
		arrList.remove(pos);
	}
	
	public static void moveUp(ArrayList<String> arrList, int pos) {
		if(pos > 0) {
			swapItem(arrList, pos, pos - 1);
		}
		else {
			return;
		}
	}
	
	public static void moveDown(ArrayList<String> arrList, int pos) {
		if(pos < arrList.size() - 1) {
			swapItem(arrList, pos, pos + 1);
		}
		else {
			return;
		}
	}
}
