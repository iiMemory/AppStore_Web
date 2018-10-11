package util;

public class L {
    public static void d(String msg) {
        System.out.println("------------------------------");
        System.out.println(msg);
        System.out.println("------------------------------");
    }

    public static void d(String tag, String msg) {
        System.out.println("-------------"+tag+"----------start-------");
        System.out.println(msg);
        System.out.println("-------------"+tag+"----------end-------");
    }
}
