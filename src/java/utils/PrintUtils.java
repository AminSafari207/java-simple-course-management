package utils;

import java.util.List;

public class PrintUtils {
    public static void printList(List<Object> list, String header) {
        String headerLine = "--- " + header + " ---";
        String divider = "-".repeat(header.length());

        System.out.println(divider);
        System.out.println(headerLine);
        System.out.println(divider);
        System.out.println();
        list.forEach(obj -> {
            System.out.println();
            System.out.println(obj);
            System.out.println();
            System.out.println(divider);
            System.out.println();
        });
    }
}
