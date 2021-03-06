package com.shihaiyang.offer;

// Offer II 064. 神奇的字典.[暴力 21ms 前缀树 30ms].

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 设计一个使用单词列表进行初始化的数据结构，单词列表中的单词 互不相同 。
 * 如果给出一个单词，请判定能否只将这个单词中一个字母换成另一个字母，使得所形成的新单词存在于已构建的神奇字典中。
 * <p>
 * 实现 MagicDictionary 类：
 * <p>
 * MagicDictionary() 初始化对象
 * void buildDict(String[] dictionary) 使用字符串数组 dictionary 设定该数据结构，dictionary 中的字符串互不相同
 * bool search(String searchWord) 给定一个字符串 searchWord ，判定能否只将字符串中 一个 字母换成另一个字母，使得所形成的新字符串能够与字典中的任一字符串匹配。如果可以，返回 true ；否则，返回 false 。
 */
public class Offer064 {
    @Test
    public void case1() {
        MagicDictionaryViolent magicDictionary = new MagicDictionaryViolent();
        magicDictionary.buildDict(new String[]{"hello", "leetcode"});
        Assertions.assertTrue(!magicDictionary.search("hello")); // 返回 False
        Assertions.assertTrue(magicDictionary.search("hhllo")); // 将第二个 'h' 替换为 'e' 可以匹配 "hello" ，所以返回 True
        Assertions.assertTrue(!magicDictionary.search("hell")); // 返回 False
        Assertions.assertTrue(!magicDictionary.search("leetcoded")); // 返回 False
    }

    @Test
    public void case2() {
        MagicDictionaryViolent magicDictionary = new MagicDictionaryViolent();
        magicDictionary.buildDict(new String[]{"hello", "hallo", "leetcode"});
        Assertions.assertTrue(magicDictionary.search("hello")); // 返回 True
        Assertions.assertTrue(magicDictionary.search("hhllo")); // 将第二个 'h' 替换为 'e' 可以匹配 "hello" ，所以返回 True
        Assertions.assertTrue(!magicDictionary.search("hell")); // 返回 False
        Assertions.assertTrue(!magicDictionary.search("leetcoded")); // 返回 False
    }
}

/**
 * 按长度分配到一个map中暴力检索
 * 第二个用前缀树
 * 匹配前缀树。
 * 如果匹配不到，并且修改次数0；就匹配所有其他的字符。标记修改次数1.
 * 如果修改次数=1，且匹配不到，就返回匹配不到。
 * 遍历每一个其他字符，如果能匹配到某一个，就返回true。
 * 如果遍历完都没有true，就返回false
 * <p>
 * ---
 * 遗漏了一个。
 * ---
 * 能判断完成的条件
 * 1. 当已经到达匹配串最后字符
 *      如果字典串也是最后一个字符，且修改过一次。  true
 *      如果字典串不是最后一个字符。  false
 * 2. 如果已经修改过一次
 *      且又出现不匹配情况。   false
 *      否则递归到下一个匹配串的字符
 * 3. 如果修改过0次
 *      需要匹配所有的存在的字符，如果遍历到一次true，直接返回true
 *          如果能匹配，递归到下一个字符，修改次数不变
 *          如果不能匹配，递归到下一个字符，修改次数+1
 *      遍历结束，说明没有匹配到   返回false
 *
 * 代码写的好多if，不知道能不能优化掉一些。
 */
class MagicDictionary {
    MagicNode root;
    Set<Integer> counts;

    public MagicDictionary() {
        root = new MagicNode();
        counts = new HashSet<>();
    }

    public void buildDict(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            MagicNode p = root;
            counts.add(dictionary[i].length());
            char[] chars = dictionary[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                int index = chars[j] - 'a';
                if (p.child[index] == null) {
                    p.child[index] = new MagicNode();
                }
                p = p.child[index];
            }
            p.isEnd = true;
        }
    }

    public boolean search(String searchWord) {
        if (!counts.contains(searchWord.length())) {
            return false;
        }
        char[] chars = searchWord.toCharArray();
        MagicNode p = root;
        boolean dfs = dfs(p, chars, 0, 0);
        return dfs;
    }

    public boolean dfs(MagicNode p, char[] chars, int i, int changeCount) {
        // 如果已经到了字符串结尾。
        if (i == chars.length) {
            // 但是匹配到的串也是结尾，那么查看是否完全相同，如果一次都没改过，也不算是。只有改过一次才是true
            if (p.isEnd) {
                return changeCount == 1;
            }
            return false;
        }
        int index = chars[i] - 'a';
        // 如果已经修改过一次，但是依然出现了不匹配情况，false
        if (changeCount == 1) {
            if (p.child[index] == null) {
                return false;
            } else {
                // 如果能匹配到，就继续往下匹配。p深入，i+1，changeCount不变
                return dfs(p.child[index], chars, i + 1, changeCount);
            }
        }

        // 这儿会有hello,hallo   匹配hello是true的情况。所以如果changeCount=0,那每个都还是要匹配一下的。
        for (int j = 0; j < 26; j++) {
            boolean dfs;
            if (p.child[j] != null) {
                // 修改一下：即遍历其他字符，并且修改次数+1
                // 如果出现一次true，那就直接是true了。 否则就需要遍历完所有的其他字符，都是false，最终才是false
                if (index != j) {
                    dfs = dfs(p.child[j], chars, i + 1, 1);
                } else {
                    dfs = dfs(p.child[index], chars, i + 1, changeCount);
                }
                if (dfs) {
                    return true;
                }
            }
        }
        return false;
    }

    class MagicNode {
        MagicNode[] child = new MagicNode[26];
        public boolean isEnd = false;

        public MagicNode() {
        }
    }
}

class MagicDictionaryViolent {
    String[] dict;
    public MagicDictionaryViolent() {
    }

    public void buildDict(String[] dictionary) {
        dict = dictionary;
    }

    public boolean search(String searchWord) {
        for (int i = 0; i < dict.length; i++) {
            boolean ret = diff(searchWord, dict[i]);
            if (ret) return ret;
        }
        return false;
    }

    private boolean diff(String searchWord, String dict) {
        if (dict.length() != searchWord.length()) {
            return false;
        }
        char[] chars = searchWord.toCharArray();
        char[] chars1 = dict.toCharArray();
        int cnt = 0;
        for (int j = 0; j < searchWord.length(); j++) {
            if (chars[j] != chars1[j]) {
                if (++cnt > 1) {
                    return false;
                }
            }
        }
        if (cnt == 1) {
            return true;
        }
        return false;
    }
}
