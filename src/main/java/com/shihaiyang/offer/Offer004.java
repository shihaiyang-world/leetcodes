package com.shihaiyang.offer;

// 剑指 Offer II 004. 只出现一次的数字
// Offer004. 只出现一次的数字.[二进制操作1ms].
public class Offer004 {
    public static void main(String[] args) {
        SolutionOffer004 offer004 = new SolutionOffer004();
        int singleNumber = offer004.singleNumber(new int[]{0, 1, 0, 1, 0, 1, 100});
        System.out.println(singleNumber);
    }
}

/**
 *
 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
 示例 1：
 输入：nums = [2,2,3,2]
 输出：3
 输入：nums = [0,1,0,1,0,1,100]
 输出：100
 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 */

/**
 * 就是某个元素出现一次，其他出现两次的变化版。
 * 两次的是借助 ^异或   0^n=n  n^n=0
 * 这个没办法用异或...1次和3次效果一样...
 * 用map可以两次遍历求得，空间复杂的O(n)  不写了
 * 数字电路的属实看不太懂...
 * 统计二进制位的能看懂一点。写一下
 * 思路就是一个数字出现三次的话，每个二进制位都相同出现三次。就可以判断出现一次的那个数字的二进制位值
 * 确定一个二进制位有几种情况：
 * 1. 3个0 1个1 最后=1%3=1 说明出现一次的是1
 * 2. 3个0 1个0 最后=0%3=0 说明出现一次的是0
 * 3. 3个1 1个0 最后=3%3=0 说明出现一次的是0
 * 4. 3个1 1个1 最后=4%3=1 说明出现一次的是1
 * 这样就能把出现1次的每一位二进制位全算出来。那这个数也就算出来了。。
 * 最终这个值应该怎么算呢？
 * 如果 i%3==1 说明这一位二进制是1
 * 1 << i 就是能找到对应二进制位
 * |或运算  0|1=1  所有最终值的对应二进制位，不能使用加法，只能使用或运算把对应二进制位设置1
 */
class SolutionOffer004 {
    public int singleNumber(int[] nums) {
        int ret=0;
        for (int i = 0; i < 32; i++) {
            int total = 0;
            for (int j = 0; j < nums.length; j++) {
                int num = nums[j];
                // 统计对应二进制位上的值
                // 出现3次的数字二进制位上应该有3个1或者3个0  出现1次的二进制位上是1个1或者1个0
                total += (num >> i) & 1;
            }
            // 通过%3就能知道是3个还是1个
            // 如果出现了1，就通过或运算把对应二进制位的1加上去
            if (total % 3 != 0) {
                ret |= (1 << i);
            }
        }
        return ret;
    }
}