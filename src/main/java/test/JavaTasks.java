package test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JavaTasks {

    public static void main(String[] args) {
        /*System.out.println(isPalindrome("рвал дед лавр"));
        System.out.println(isPalindrome("коту тащат уток"));
        System.out.println(isPalindrome("Аргентина манит негра"));*/

        //System.out.println(isAnagram("нора", "рано"));

        //System.out.println(binarySearch(4, new int[]{2,7,4}));

        for (int i = 0; i < 100; i++) {
            fizzBuzz(i);
        }
    }

    public static boolean isPalindrome(String word) {
        String lowerWord = word.toLowerCase();
        int left = 0;
        int right = lowerWord.length() - 1;
        while(left < right) {
            if (lowerWord.charAt(left) != lowerWord.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    public static boolean isAnagram(String word1, String word2) {
        if (word1.length() != word2.length()) return false;
        char[] first = word1.toCharArray();
        char[] second = word2.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }

    public static int binarySearch(int num, int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == num) return mid;
            if (arr[mid] < num) left = mid + 1;
            else right = mid - 1;
        }

        return -1;
    }

    public static void fizzBuzz(int num) {
        StringBuilder sb = new StringBuilder();
        if (num % 3 == 0) sb.append("Fizz");
        if (num % 5 == 0) sb.append("Buzz");
        System.out.println(sb);
    }

    public final class ImmutableClass {
        private final List<Integer> list;

        public ImmutableClass(List<Integer> list) {
            this.list = List.copyOf(list);
        }
    }
}
