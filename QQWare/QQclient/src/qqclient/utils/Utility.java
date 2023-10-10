package qqclient.utils;

import java.util.Scanner;

/**
 * @version 1.0
 * @Author King
 * 这是一个工具类，处理各种情况的用户输入
 */
public class Utility {
    private static Scanner scanner = new Scanner(System.in); //静态属性

    /**
     * 读取字符串
     */
    public static String readString(){
        return scanner.next();
    }
}
