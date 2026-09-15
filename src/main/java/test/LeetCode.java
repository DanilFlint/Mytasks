package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeetCode {

    public static void main(String[] args) {

    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        int[] indexes = new int [2];
        for (int i = 0; i < nums.length; i++) {
            if (map.get(target - nums[i]) == null) {
                List<Integer> list = new ArrayList<>();
                list.add(nums[i]);
                list.add(i);
                map.put(target - nums[i], list);
            } else {
                indexes[0] = i;
                indexes[1] = map.get(target - nums[i]).get(1);
            }
        }
        return indexes;
    }

    public String longestCommonPrefix(String[] strs) {
        char[] prefix = new char[200];
        String firstWord = strs[0];
        int lengthFirst = firstWord.length();
        for (int i = 0; i < lengthFirst; i++) {
            char currentChar = firstWord.charAt(i);
            for(int j = 1; j < strs.length; j++) {
                if(i >= strs[j].length()) return new String(prefix, 0, prefix.length);
                if (strs[j].charAt(i) != currentChar) return new String(prefix, 0, prefix.length);
            }
            prefix[i] = currentChar;
        }

        return new String(prefix, 0, prefix.length);
    }
}
