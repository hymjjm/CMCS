package com.example.ose;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        System.out.println(method(input));
    }

    public static double method(String arr) {

        arr = arr.trim(); // 去除首尾空格
        int count = 0;
        for (int i = 0; i < arr.length(); i++) {
            if (arr.charAt(i) == ' ') {
                count++;
            }
        }
        if (count == 0) return arr.length();
        double out = (double) (arr.length() - count) / (++count);
        return Math.round(out * 100.0) / 100.0;
    }
}
