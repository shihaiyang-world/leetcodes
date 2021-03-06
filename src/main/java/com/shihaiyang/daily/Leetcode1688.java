package com.shihaiyang.daily;

// 1688. 比赛中的配对次数.[位运算0ms 100%].
public class Leetcode1688 {
    public static void main(String[] args) {
        Solution1688 solution1688 = new Solution1688();
        int ofMatches = solution1688.numberOfMatches(14);
        System.out.println(ofMatches);
    }
}

/**
 * 给你一个整数 n ，表示比赛中的队伍数。比赛遵循一种独特的赛制：
 * 如果当前队伍数是 偶数 ，那么每支队伍都会与另一支队伍配对。总共进行 n / 2 场比赛，且产生 n / 2 支队伍进入下一轮。
 * 如果当前队伍数为 奇数 ，那么将会随机轮空并晋级一支队伍，其余的队伍配对。总共进行 (n - 1) / 2 场比赛，且产生 (n - 1) / 2 + 1 支队伍进入下一轮。
 * 返回在比赛中进行的配对次数，直到决出获胜队伍为止。
 * 输入：n = 7
 * 输出：6
 * 解释：比赛详情：
 * - 第 1 轮：队伍数 = 7 ，配对次数 = 3 ，4 支队伍晋级。
 * - 第 2 轮：队伍数 = 4 ，配对次数 = 2 ，2 支队伍晋级。
 * - 第 3 轮：队伍数 = 2 ，配对次数 = 1 ，决出 1 支获胜队伍。
 * 总配对次数 = 3 + 2 + 1 = 6
 */
class Solution1688 {
    public int numberOfMatches(int n) {
        int sum = 0;
        while (n > 1) {
            if ((n & 1) == 1) {
                n >>= 1;
                sum += n;
                n += 1;
            } else {
                n >>= 1;
                sum += n;
            }
        }
        return sum;
    }
}
