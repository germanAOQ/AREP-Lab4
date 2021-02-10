package edu.eci.arep;

import java.util.Arrays;

public class Reflection {
    public static void main(String[] args) throws ClassNotFoundException {
        Class a = "hola".getClass();
        System.out.println(a);
        byte[] bytes = new byte[1024];
        Class b = bytes.getClass();
        System.out.println(b);
        Class c = boolean.class;
        System.out.println(c);
        Class d = Class.forName("java.lang.String");
        System.out.println(d);

        System.out.println(c.getSuperclass());
        System.out.println(Arrays.toString(a.getFields()));
        System.out.println(Arrays.toString(a.getDeclaredFields()));
    }
}
