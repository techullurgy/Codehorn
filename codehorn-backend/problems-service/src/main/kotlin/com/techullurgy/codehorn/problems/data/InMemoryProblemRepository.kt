package com.techullurgy.codehorn.problems.data

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem
import com.techullurgy.codehorn.problems.domain.model.ProblemExample
import com.techullurgy.codehorn.problems.domain.model.TestCase
import com.techullurgy.codehorn.problems.domain.repository.ProblemRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryProblemRepository : ProblemRepository {
    private val problems = ConcurrentHashMap<String, Problem>()

    init {
        seedInitialProblems().forEach { problem ->
            problems[problem.slug] = problem
        }
    }

    override fun findAll(difficulty: Difficulty?, category: String?, search: String?): List<Problem> {
        return problems.values.filter { problem ->
            val matchesDifficulty = difficulty == null || problem.difficulty == difficulty
            val matchesCategory = category.isNullOrBlank() || problem.category.contains(category, ignoreCase = true)
            val matchesSearch = search.isNullOrBlank() ||
                    problem.title.contains(search, ignoreCase = true) ||
                    problem.description.contains(search, ignoreCase = true) ||
                    problem.category.contains(search, ignoreCase = true)

            matchesDifficulty && matchesCategory && matchesSearch
        }.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
    }

    override fun findBySlug(slug: String): Problem? {
        return problems[slug] ?: problems.values.find { it.id == slug }
    }

    override fun findById(id: String): Problem? {
        return problems.values.find { it.id == id } ?: problems[id]
    }

    override fun save(problem: Problem): Problem {
        problems[problem.slug] = problem
        return problem
    }

    override fun deleteBySlug(slug: String): Boolean {
        return problems.remove(slug) != null
    }

    private fun seedInitialProblems(): List<Problem> {
        return listOf(
            Problem(
                id = "1",
                title = "Two Sum",
                slug = "two-sum",
                difficulty = Difficulty.Easy,
                category = "Algorithms / Arrays",
                acceptanceRate = 51.5,
                description = """Given an array of integers `nums` and an integer `target`, return *indices of the two numbers such that they add up to `target`*.

You may assume that each input would have ***exactly* one solution**, and you may not use the *same* element twice.

You can return the answer in any order.""",
                examples = listOf(
                    ProblemExample(1, "nums = [2,7,11,15], target = 9", "[0,1]", "Because nums[0] + nums[1] == 9, we return [0, 1]."),
                    ProblemExample(2, "nums = [3,2,4], target = 6", "[1,2]"),
                    ProblemExample(3, "nums = [3,3], target = 6", "[0,1]")
                ),
                constraints = listOf(
                    "2 <= nums.length <= 10^4",
                    "-10^9 <= nums[i] <= 10^9",
                    "-10^9 <= target <= 10^9",
                    "Only one valid answer exists."
                ),
                hints = listOf(
                    "A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Can you think of a way to do it in linear time complexity?",
                    "Try using a hash map to map each value to its index. When we look at a number x, we can check if target - x is already in our map!"
                ),
                editorial = """### Approach 1: Brute Force
Loop through each element x and find if there is another value equal to target - x.
- **Time Complexity:** O(N^2)
- **Space Complexity:** O(1)

### Approach 2: One-Pass Hash Map
Iterate through the array and store each element's index in a hash map. Check if complement target - x exists in the map.
- **Time Complexity:** O(N)
- **Space Complexity:** O(N)""",
                solutionApproaches = listOf(
                    "Brute Force - O(N^2) Time, O(1) Space",
                    "One-Pass Hash Map - O(N) Time, O(N) Space (Optimal)"
                ),
                starterCode = mapOf(
                    "javascript" to """/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number[]}
 */
function twoSum(nums, target) {
    // Write your code here
    
};""",
                    "python" to """class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # Write your code here
        pass""",
                    "cpp" to """class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // Write your code here
        
    }
};""",
                    "java" to """class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Write your code here
        return new int[]{};
    }
}"""
                ),
                templates = mapOf(
                    "javascript" to """const fs = require('fs');

// USER_CODE_HERE

const input = fs.readFileSync('/dev/stdin', 'utf-8').trim().split(/\s+/);
if (input.length >= 2) {
    const n = parseInt(input[0]);
    const nums = [];
    for(let i = 1; i <= n; i++) nums.push(parseInt(input[i]));
    const target = parseInt(input[n + 1]);
    const res = twoSum(nums, target);
    console.log(JSON.stringify(res).replace(/\s+/g, ''));
}""",
                    "python" to """import sys
import json

// USER_CODE_HERE

if __name__ == '__main__':
    lines = sys.stdin.read().splitlines()
    if lines:
        n = int(lines[0])
        nums = [int(lines[i]) for i in range(1, n + 1)]
        target = int(lines[n + 1])
        sol = Solution()
        res = sol.twoSum(nums, target)
        print(json.dumps(res).replace(" ", ""))""",
                    "cpp" to """#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

// USER_CODE_HERE

int main() {
    int n;
    if (!(cin >> n)) return 0;
    vector<int> nums(n);
    for(int i = 0; i < n; i++) cin >> nums[i];
    int target;
    cin >> target;
    
    Solution sol;
    vector<int> result = sol.twoSum(nums, target);
    cout << "[" << result[0] << "," << result[1] << "]" << endl;
    return 0;
}""",
                    "java" to """import java.util.Scanner;
import java.util.Arrays;

// USER_CODE_HERE

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        int[] nums = new int[n];
        for(int i = 0; i < n; i++) nums[i] = scanner.nextInt();
        int target = scanner.nextInt();
        
        Solution solution = new Solution();
        int[] result = solution.twoSum(nums, target);
        System.out.println(Arrays.toString(result).replaceAll(" ", ""));
    }
}"""
                ),
                solutions = mapOf(
                    "javascript" to """function twoSum(nums, target) {
    const map = new Map();
    for (let i = 0; i < nums.length; i++) {
        const complement = target - nums[i];
        if (map.has(complement)) {
            return [map.get(complement), i];
        }
        map.set(nums[i], i);
    }
    return [];
}""",
                    "python" to """class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        seen = {}
        for i, num in enumerate(nums):
            comp = target - num
            if comp in seen:
                return [seen[comp], i]
            seen[num] = i
        return []""",
                    "cpp" to """class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        unordered_map<int, int> mp;
        for(int i = 0; i < nums.size(); ++i) {
            int comp = target - nums[i];
            if(mp.count(comp)) return {mp[comp], i};
            mp[nums[i]] = i;
        }
        return {};
    }
};""",
                    "java" to """class Solution {
    public int[] twoSum(int[] nums, int target) {
        java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int comp = target - nums[i];
            if (map.containsKey(comp)) {
                return new int[] { map.get(comp), i };
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }
}"""
                ),
                testcases = listOf(
                    TestCase("1-1", "[2,7,11,15]\n9", "[0,1]", isSample = true),
                    TestCase("1-2", "[3,2,4]\n6", "[1,2]", isSample = true),
                    TestCase("1-3", "[3,3]\n6", "[0,1]", isSample = true),
                    TestCase("1-4", "[5,2,1,10,18,3]\n13", "[3,5]", isSample = false)
                )
            ),
            Problem(
                id = "2",
                title = "Valid Parentheses",
                slug = "valid-parentheses",
                difficulty = Difficulty.Easy,
                category = "Algorithms / Stack",
                acceptanceRate = 40.8,
                description = """Given a string `s` containing just the characters `'('`, `')'`, `'{'`, `'}'`, `'['` and `']'`, determine if the input string is valid.

An input string is valid if:
1. Open brackets must be closed by the same type of brackets.
2. Open brackets must be closed in the correct order.
3. Every close bracket has a corresponding open bracket of the same type.""",
                examples = listOf(
                    ProblemExample(1, "s = \"()\"", "true"),
                    ProblemExample(2, "s = \"()[]{}\"", "true"),
                    ProblemExample(3, "s = \"(]\"", "false"),
                    ProblemExample(4, "s = \"([])\"", "true")
                ),
                constraints = listOf(
                    "1 <= s.length <= 10^4",
                    "s consists of parentheses only: '()[]{}'"
                ),
                hints = listOf(
                    "Use a stack to keep track of the most recent open bracket.",
                    "When you encounter a closing bracket, check if the top of the stack has the matching open bracket."
                ),
                editorial = """### Stack Approach
We utilize a stack to match brackets:
1. Push open brackets onto stack.
2. For closing bracket, verify top element matches.
- **Time Complexity:** O(N)
- **Space Complexity:** O(N)""",
                solutionApproaches = listOf(
                    "Stack-based Validation - O(N) Time, O(N) Space"
                ),
                starterCode = mapOf(
                    "javascript" to """/**
 * @param {string} s
 * @return {boolean}
 */
function isValid(s) {
    // Write your code here
    
};""",
                    "python" to """class Solution:
    def isValid(self, s: str) -> bool:
        # Write your code here
        pass""",
                    "cpp" to """class Solution {
public:
    bool isValid(string s) {
        // Write your code here
        
    }
};""",
                    "java" to """class Solution {
    public boolean isValid(String s) {
        // Write your code here
        return false;
    }
}"""
                ),
                templates = mapOf(
                    "javascript" to """const fs = require('fs');

// USER_CODE_HERE

const input = fs.readFileSync('/dev/stdin', 'utf-8').trim();
console.log(isValid(input) ? 'true' : 'false');""",
                    "python" to """import sys

// USER_CODE_HERE

if __name__ == '__main__':
    s = sys.stdin.read().strip()
    sol = Solution()
    print('true' if sol.isValid(s) else 'false')""",
                    "cpp" to """#include <iostream>
#include <string>
#include <stack>
using namespace std;

// USER_CODE_HERE

int main() {
    string s;
    cin >> s;
    Solution sol;
    cout << (sol.isValid(s) ? "true" : "false") << endl;
    return 0;
}""",
                    "java" to """import java.util.Scanner;
import java.util.Stack;

// USER_CODE_HERE

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNext()) return;
        String s = scanner.next();
        Solution solution = new Solution();
        System.out.println(solution.isValid(s) ? "true" : "false");
    }
}"""
                ),
                solutions = mapOf(
                    "javascript" to """function isValid(s) {
    const stack = [];
    const map = { ')': '(', '}': '{', ']': '[' };
    for (let char of s) {
        if (char === '(' || char === '{' || char === '[') {
            stack.push(char);
        } else {
            if (stack.pop() !== map[char]) return false;
        }
    }
    return stack.length === 0;
}""",
                    "python" to """class Solution:
    def isValid(self, s: str) -> bool:
        stack = []
        mapping = {')': '(', '}': '{', ']': '['}
        for char in s:
            if char in mapping:
                top = stack.pop() if stack else '#'
                if mapping[char] != top:
                    return False
            else:
                stack.append(char)
        return not stack""",
                    "cpp" to """class Solution {
public:
    bool isValid(string s) {
        stack<char> st;
        for(char c : s) {
            if(c == '(' || c == '{' || c == '[') st.push(c);
            else {
                if(st.empty()) return false;
                if(c == ')' && st.top() != '(') return false;
                if(c == '}' && st.top() != '{') return false;
                if(c == ']' && st.top() != '[') return false;
                st.pop();
            }
        }
        return st.empty();
    }
};""",
                    "java" to """class Solution {
    public boolean isValid(String s) {
        java.util.Stack<Character> stack = new java.util.Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        return stack.isEmpty();
    }
}"""
                ),
                testcases = listOf(
                    TestCase("2-1", "\"()\"", "true", isSample = true),
                    TestCase("2-2", "\"()[]{}\"", "true", isSample = true),
                    TestCase("2-3", "\"(]\"", "false", isSample = true),
                    TestCase("2-4", "\"([])\"", "true", isSample = false),
                    TestCase("2-5", "\"([)]\"", "false", isSample = false)
                )
            ),
            Problem(
                id = "3",
                title = "Longest Substring Without Repeating Characters",
                slug = "longest-substring-without-repeating-characters",
                difficulty = Difficulty.Medium,
                category = "Algorithms / Sliding Window",
                acceptanceRate = 34.2,
                description = """Given a string `s`, find the length of the **longest substring** without repeating characters.

A **substring** is a contiguous non-empty sequence of characters within a string.""",
                examples = listOf(
                    ProblemExample(1, "s = \"abcabcbb\"", "3", "The answer is \"abc\", with the length of 3."),
                    ProblemExample(2, "s = \"bbbbb\"", "1", "The answer is \"b\", with the length of 1."),
                    ProblemExample(3, "s = \"pwwkew\"", "3", "The answer is \"wke\", with the length of 3.")
                ),
                constraints = listOf(
                    "0 <= s.length <= 5 * 10^4",
                    "s consists of English letters, digits, symbols and spaces."
                ),
                hints = listOf(
                    "Think about using a sliding window with two pointers.",
                    "Maintain a set of characters inside the current window."
                ),
                editorial = """### Sliding Window Approach
Maintain a sliding window [i, j]. Expand j and shrink i whenever a duplicate is encountered.
- **Time Complexity:** O(N)
- **Space Complexity:** O(min(M, N))""",
                solutionApproaches = listOf(
                    "Sliding Window with HashSet - O(N) Time, O(min(M, N)) Space (Optimal)"
                ),
                starterCode = mapOf(
                    "javascript" to """/**
 * @param {string} s
 * @return {number}
 */
function lengthOfLongestSubstring(s) {
    // Write your code here
    
};""",
                    "python" to """class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        # Write your code here
        pass""",
                    "cpp" to """class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        // Write your code here
        
    }
};""",
                    "java" to """class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Write your code here
        return 0;
    }
}"""
                ),
                templates = mapOf(
                    "javascript" to """const fs = require('fs');

// USER_CODE_HERE

const input = fs.readFileSync('/dev/stdin', 'utf-8').replace(/^"|"$/g, '').trim();
console.log(lengthOfLongestSubstring(input));""",
                    "python" to """import sys

// USER_CODE_HERE

if __name__ == '__main__':
    s = sys.stdin.read().strip().strip('"')
    sol = Solution()
    print(sol.lengthOfLongestSubstring(s))""",
                    "cpp" to """#include <iostream>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

// USER_CODE_HERE

int main() {
    string s;
    cin >> s;
    if (!s.empty() && s.front() == '"') s.erase(0, 1);
    if (!s.empty() && s.back() == '"') s.pop_back();
    Solution sol;
    cout << sol.lengthOfLongestSubstring(s) << endl;
    return 0;
}""",
                    "java" to """import java.util.Scanner;

// USER_CODE_HERE

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.hasNext() ? scanner.next().replaceAll("^\"|\"$", "") : "";
        Solution solution = new Solution();
        System.out.println(solution.lengthOfLongestSubstring(s));
    }
}"""
                ),
                solutions = mapOf(
                    "javascript" to """function lengthOfLongestSubstring(s) {
    let maxLen = 0, i = 0;
    const set = new Set();
    for (let j = 0; j < s.length; j++) {
        while (set.has(s[j])) {
            set.delete(s[i]);
            i++;
        }
        set.add(s[j]);
        maxLen = Math.max(maxLen, j - i + 1);
    }
    return maxLen;
}""",
                    "python" to """class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        seen = {}
        left = max_len = 0
        for right, char in enumerate(s):
            if char in seen and seen[char] >= left:
                left = seen[char] + 1
            seen[char] = right
            max_len = max(max_len, right - left + 1)
        return max_len""",
                    "cpp" to """class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        unordered_map<char, int> mp;
        int left = 0, maxLen = 0;
        for(int right = 0; right < s.length(); right++) {
            if(mp.count(s[right]) && mp[s[right]] >= left) {
                left = mp[s[right]] + 1;
            }
            mp[s[right]] = right;
            maxLen = max(maxLen, right - left + 1);
        }
        return maxLen;
    }
};""",
                    "java" to """class Solution {
    public int lengthOfLongestSubstring(String s) {
        java.util.Map<Character, Integer> map = new java.util.HashMap<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c) && map.get(c) >= left) {
                left = map.get(c) + 1;
            }
            map.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}"""
                ),
                testcases = listOf(
                    TestCase("3-1", "\"abcabcbb\"", "3", isSample = true),
                    TestCase("3-2", "\"bbbbb\"", "1", isSample = true),
                    TestCase("3-3", "\"pwwkew\"", "3", isSample = true),
                    TestCase("3-4", "\"\"", "0", isSample = false),
                    TestCase("3-5", "\"au\"", "2", isSample = false)
                )
            ),
            Problem(
                id = "4",
                title = "Container With Most Water",
                slug = "container-with-most-water",
                difficulty = Difficulty.Medium,
                category = "Algorithms / Two Pointers",
                acceptanceRate = 54.1,
                description = """You are given an integer array `height` of length `n`. There are `n` vertical lines drawn such that the two endpoints of the `i`th line are `(i, 0)` and `(i, height[i])`.

Find two lines that together with the x-axis form a container, such that the container contains the most water.

Return *the maximum amount of water a container can store*.""",
                examples = listOf(
                    ProblemExample(1, "height = [1,8,6,2,5,4,8,3,7]", "49"),
                    ProblemExample(2, "height = [1,1]", "1")
                ),
                constraints = listOf(
                    "n == height.length",
                    "2 <= n <= 10^5",
                    "0 <= height[i] <= 10^4"
                ),
                hints = listOf(
                    "Start with two pointers at both ends of the array.",
                    "Move the pointer with the smaller height inwardly to try and find a larger container."
                ),
                editorial = """### Two Pointers Approach
Compute area = min(height[left], height[right]) * (right - left). Move shorter bar inward.
- **Time Complexity:** O(N)
- **Space Complexity:** O(1)""",
                solutionApproaches = listOf(
                    "Two Pointer Shrinking Range - O(N) Time, O(1) Space (Optimal)"
                ),
                starterCode = mapOf(
                    "javascript" to """/**
 * @param {number[]} height
 * @return {number}
 */
function maxArea(height) {
    // Write your code here
    
};""",
                    "python" to """class Solution:
    def maxArea(self, height: List[int]) -> int:
        # Write your code here
        pass""",
                    "cpp" to """class Solution {
public:
    int maxArea(vector<int>& height) {
        // Write your code here
        
    }
};""",
                    "java" to """class Solution {
    public int maxArea(int[] height) {
        // Write your code here
        return 0;
    }
}"""
                ),
                templates = mapOf(
                    "javascript" to """const fs = require('fs');

// USER_CODE_HERE

const input = fs.readFileSync('/dev/stdin', 'utf-8').trim().split(/\s+/);
if (input.length >= 1) {
    const n = parseInt(input[0]);
    const height = [];
    for(let i = 1; i <= n; i++) height.push(parseInt(input[i]));
    console.log(maxArea(height));
}""",
                    "python" to """import sys

// USER_CODE_HERE

if __name__ == '__main__':
    lines = sys.stdin.read().splitlines()
    if lines:
        n = int(lines[0])
        height = [int(lines[i]) for i in range(1, n + 1)]
        sol = Solution()
        print(sol.maxArea(height))""",
                    "cpp" to """#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// USER_CODE_HERE

int main() {
    int n;
    if (!(cin >> n)) return 0;
    vector<int> height(n);
    for(int i = 0; i < n; i++) cin >> height[i];
    Solution sol;
    cout << sol.maxArea(height) << endl;
    return 0;
}""",
                    "java" to """import java.util.Scanner;

// USER_CODE_HERE

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        int[] height = new int[n];
        for(int i = 0; i < n; i++) height[i] = scanner.nextInt();
        Solution solution = new Solution();
        System.out.println(solution.maxArea(height));
    }
}"""
                ),
                solutions = mapOf(
                    "javascript" to """function maxArea(height) {
    let left = 0, right = height.length - 1, maxWater = 0;
    while (left < right) {
        const area = Math.min(height[left], height[right]) * (right - left);
        maxWater = Math.max(maxWater, area);
        if (height[left] < height[right]) left++;
        else right--;
    }
    return maxWater;
}""",
                    "python" to """class Solution:
    def maxArea(self, height: List[int]) -> int:
        left, right = 0, len(height) - 1
        max_water = 0
        while left < right:
            area = min(height[left], height[right]) * (right - left)
            max_water = max(max_water, area)
            if height[left] < height[right]:
                left += 1
            else:
                right -= 1
        return max_water""",
                    "cpp" to """class Solution {
public:
    int maxArea(vector<int>& height) {
        int left = 0, right = height.size() - 1, maxWater = 0;
        while(left < right) {
            int area = min(height[left], height[right]) * (right - left);
            maxWater = max(maxWater, area);
            if(height[left] < height[right]) left++;
            else right--;
        }
        return maxWater;
    }
};""",
                    "java" to """class Solution {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1, maxWater = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxWater = Math.max(maxWater, area);
            if (height[left] < height[right]) left++;
            else right--;
        }
        return maxWater;
    }
}"""
                ),
                testcases = listOf(
                    TestCase("4-1", "[1,8,6,2,5,4,8,3,7]", "49", isSample = true),
                    TestCase("4-2", "[1,1]", "1", isSample = true),
                    TestCase("4-3", "[4,3,2,1,4]", "16", isSample = false)
                )
            ),
            Problem(
                id = "5",
                title = "Median of Two Sorted Arrays",
                slug = "median-of-two-sorted-arrays",
                difficulty = Difficulty.Hard,
                category = "Algorithms / Binary Search",
                acceptanceRate = 38.9,
                description = """Given two sorted arrays `nums1` and `nums2` of size `m` and `n` respectively, return **the median** of the two sorted arrays.

The overall run time complexity should be **O(log(m+n))**.""",
                examples = listOf(
                    ProblemExample(1, "nums1 = [1,3], nums2 = [2]", "2.00000"),
                    ProblemExample(2, "nums1 = [1,2], nums2 = [3,4]", "2.50000")
                ),
                constraints = listOf(
                    "0 <= m <= 1000",
                    "0 <= n <= 1000",
                    "1 <= m + n <= 2000"
                ),
                hints = listOf(
                    "Can you partition the arrays into equal left and right halves using Binary Search?"
                ),
                editorial = """### Binary Search Partition Approach
Perform binary search on partition of the smaller array such that L1 <= R2 and L2 <= R1.
- **Time Complexity:** O(log(min(M, N)))
- **Space Complexity:** O(1)""",
                solutionApproaches = listOf(
                    "Binary Search on Median Split - O(log(min(M,N))) Time, O(1) Space (Optimal)"
                ),
                starterCode = mapOf(
                    "javascript" to """/**
 * @param {number[]} nums1
 * @param {number[]} nums2
 * @return {number}
 */
function findMedianSortedArrays(nums1, nums2) {
    // Write your code here
    
};""",
                    "python" to """class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        # Write your code here
        pass""",
                    "cpp" to """class Solution {
public:
    double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
        // Write your code here
        
    }
};""",
                    "java" to """class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // Write your code here
        return 0.0;
    }
}"""
                ),
                templates = mapOf(
                    "javascript" to """const fs = require('fs');

// USER_CODE_HERE

const input = fs.readFileSync('/dev/stdin', 'utf-8').trim().split(/\s+/);
if (input.length >= 2) {
    const m = parseInt(input[0]);
    const nums1 = [];
    for(let i = 1; i <= m; i++) nums1.push(parseInt(input[i]));
    const n = parseInt(input[m + 1]);
    const nums2 = [];
    for(let i = m + 2; i <= m + 1 + n; i++) nums2.push(parseInt(input[i]));
    console.log(findMedianSortedArrays(nums1, nums2).toFixed(5));
}""",
                    "python" to """import sys

// USER_CODE_HERE

if __name__ == '__main__':
    lines = sys.stdin.read().splitlines()
    if lines:
        m = int(lines[0])
        nums1 = [int(lines[i]) for i in range(1, m + 1)]
        n = int(lines[m + 1])
        nums2 = [int(lines[i]) for i in range(m + 2, m + 2 + n)]
        sol = Solution()
        print(f"{sol.findMedianSortedArrays(nums1, nums2):.5f}")""",
                    "cpp" to """#include <iostream>
#include <vector>
#include <iomanip>
using namespace std;

// USER_CODE_HERE

int main() {
    int m;
    if (!(cin >> m)) return 0;
    vector<int> nums1(m);
    for(int i = 0; i < m; i++) cin >> nums1[i];
    int n;
    cin >> n;
    vector<int> nums2(n);
    for(int i = 0; i < n; i++) cin >> nums2[i];
    
    Solution sol;
    cout << fixed << setprecision(5) << sol.findMedianSortedArrays(nums1, nums2) << endl;
    return 0;
}""",
                    "java" to """import java.util.Scanner;

// USER_CODE_HERE

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int m = scanner.nextInt();
        int[] nums1 = new int[m];
        for(int i = 0; i < m; i++) nums1[i] = scanner.nextInt();
        int n = scanner.nextInt();
        int[] nums2 = new int[n];
        for(int i = 0; i < n; i++) nums2[i] = scanner.nextInt();
        
        Solution solution = new Solution();
        System.out.printf("%.5f\n", solution.findMedianSortedArrays(nums1, nums2));
    }
}"""
                ),
                solutions = mapOf(
                    "javascript" to """function findMedianSortedArrays(nums1, nums2) {
    if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
    const m = nums1.length, n = nums2.length;
    let low = 0, high = m;
    while (low <= high) {
        const i = Math.floor((low + high) / 2);
        const j = Math.floor((m + n + 1) / 2) - i;
        const maxLeft1 = (i === 0) ? -Infinity : nums1[i - 1];
        const minRight1 = (i === m) ? Infinity : nums1[i];
        const maxLeft2 = (j === 0) ? -Infinity : nums2[j - 1];
        const minRight2 = (j === n) ? Infinity : nums2[j];
        if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
            if ((m + n) % 2 === 0) {
                return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2;
            } else {
                return Math.max(maxLeft1, maxLeft2);
            }
        } else if (maxLeft1 > minRight2) {
            high = i - 1;
        } else {
            low = i + 1;
        }
    }
    return 0.0;
}""",
                    "python" to """class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        if len(nums1) > len(nums2):
            nums1, nums2 = nums2, nums1
        m, n = len(nums1), len(nums2)
        low, high = 0, m
        while low <= high:
            i = (low + high) // 2
            j = (m + n + 1) // 2 - i
            maxLeft1 = float('-inf') if i == 0 else nums1[i - 1]
            minRight1 = float('inf') if i == m else nums1[i]
            maxLeft2 = float('-inf') if j == 0 else nums2[j - 1]
            minRight2 = float('inf') if j == n else nums2[j]
            if maxLeft1 <= minRight2 and maxLeft2 <= minRight1:
                if (m + n) % 2 == 0:
                    return (max(maxLeft1, maxLeft2) + min(minRight1, minRight2)) / 2.0
                else:
                    return float(max(maxLeft1, maxLeft2))
            elif maxLeft1 > minRight2:
                high = i - 1;
            else:
                low = i + 1;
        return 0.0""",
                    "cpp" to """class Solution {
public:
    double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
        if (nums1.size() > nums2.size()) return findMedianSortedArrays(nums2, nums1);
        int m = nums1.size(), n = nums2.size();
        int low = 0, high = m;
        while (low <= high) {
            int i = (low + high) / 2;
            int j = (m + n + 1) / 2 - i;
            double maxLeft1 = (i == 0) ? -1e9 : nums1[i - 1];
            double minRight1 = (i == m) ? 1e9 : nums1[i];
            double maxLeft2 = (j == 0) ? -1e9 : nums2[j - 1];
            double minRight2 = (j == n) ? 1e9 : nums2[j];
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                if ((m + n) % 2 == 0) {
                    return (max(maxLeft1, maxLeft2) + min(minRight1, minRight2)) / 2.0;
                } else {
                    return max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                high = i - 1;
            } else {
                low = i + 1;
            }
        }
        return 0.0;
    }
};""",
                    "java" to """class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
        int m = nums1.length, n = nums2.length;
        int low = 0, high = m;
        while (low <= high) {
            int i = (low + high) / 2;
            int j = (m + n + 1) / 2 - i;
            double maxLeft1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            double minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            double maxLeft2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            double minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                } else {
                    return Math.max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                high = i - 1;
            } else {
                low = i + 1;
            }
        }
        return 0.0;
    }
}"""
                ),
                testcases = listOf(
                    TestCase("5-1", "[1,3]\n[2]", "2.0", isSample = true),
                    TestCase("5-2", "[1,2]\n[3,4]", "2.5", isSample = true),
                    TestCase("5-3", "[]\n[1]", "1.0", isSample = false)
                )
            )
        )
    }
}
