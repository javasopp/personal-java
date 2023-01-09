package com.mds.business.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: Sopp
 * @Date: 2019/4/23 10:35
 **/
@Slf4j
public class Test {

    private static int[] numbers = new int[]{2, 3, 1, 0, 2, 5, 3};


    public static void main(String[] args) {
        System.out.println(new Test().numberRepitFind(numbers));
    }

    public int numberRepitFind(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            Integer number = nums[i];
            int hashCode = number.hashCode();
            if (!list.contains(hashCode)) {
                list.add(hashCode);
            } else {
                return number;
            }
        }
        return 0;
    }
}