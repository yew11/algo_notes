# Quick Index 

+ **[Partition/Removal](#partition-and-removal)**
  + [Move Zeros](#move-zeros)
+ **[Sliding Windows](#sliding-windows)**
  + [Fixed size window](#fixed-size)
    + [Implement strStr](#implement-strstr)
    + [Repeated DNA Strings](#repeated-dna-strings)
    + [Find all anagrams](#find-all-anagrams)
    + [Max value in subarray of size k](#max-value-of-each-size-k-subarray)
    + [K largest elements in subarray of size n](#k-largest-elements-in-each-subarray-of-size-n)
    + **[Contain duplicates](#contain-duplicates)**  
    + [Time and Value](#time-and-value)
    + [Unchanged Order task scheduler ](#task-scheduler)
  + [Non-fixed size window](#non-fixed-size)
+ **[2D-Sliding Windows](#sliding-windows)**

# Partition and Removal

### Move Zeros

**Parition the array into two parts, the left subarray are non zeros, the right subarray are zeros**

example: `[1, 0, 0, 2, 3, 0, 4]` --> `[1, 2, 3, 4, 0, 0, 0]`

需要搞清楚每一步的物理意义,并且维持物理意义! 有哪些物理意义呢？比如，结果到底包不包括 `slow` pointer？ 


**Two pointers**: `slow`: the [0, slow] part are non-zeros (including slow). `Slow` is initialized as `-1`.   

`fast`: [fast, end] part are the area **NOT** yet being discovered (including fast).

(slow, fast]: the area we don't really care. 

```
public void moveZeros(int[] array) {
  int slow = -1; 
  for (int fast = 0; fast < array.length; i++) {
    if (array[fast] != 0) {
      array[++slow] = array[fast]; 
    } else continue; 
  }
  for (int i = slow + 1; i < nums.length; i++) {
    nums[i] = 0; 
  }
}
```

# Sliding Windows

与上面不同的是，我们只在乎sliding window里面的情况：

`[0, slow)` not useful anymore 

`[slow, fast]` the actual sliding window we are interested in

`(fast, end)` area to explore. 

## Fixed size 

### Implement strStr

Example: `haystack = "hello", needle = "ll"`

Key point : **Fixed sliding window with a length of the smaller string**. 

(1) Brutal Force: Check each if there is a subsubtring in the longer string matches all the characters in the shorter one. 

```
public int isStr(String needle, String haystack) {
  for (int i = 0; i <= haystack.length() - needle.length(); i++) {
    if (equals(haystack, needle, i)) return i; 
  }
  return -1; 
}

private boolean equals(String haystack, String needle, int index) {
  for (int i = 0; i < needle.length(); i++) {
    if (haystack.charAt(i + index) != needle.charAt(i)) return false; 
  }
  return true; 
}
```

TC: `O(k * N)`, `k` is the shorter string and `N` is the larger string. 

(2) Optimizition: If we want to optimize the solution, we need to reduce the time spent on **Checking** strings. By using `Hashing` can the give the results we want. 

abc = a * 26^2 + b * 26^1 + c * 26^0 

bcd = b * 26^2 + c * 26^1 + d * 26^0  

When the two strings are the same, they share the `Hash Value`.

```
public int isStr2(String needle, String haystack) {
  //large prime as module end. 
  int largePrime = 101; 
  //small prime to calculate the hash value, "26" in the above illustration. 
  int prime = 31; 
  int seed = 1; 
  //hash = (s0*prime^k + s1*prime^(k-1) + ... + sk*prime^0) % largePrime
  int targetHash = needle.charAt(0) % largePrime; 
  for (int i = 0; i < needle.length(); i++) {
    seed = moduleHash(seed, 0, prime, largePrime); 
    targetHash = moduleHash(targetHash, needle.charAt(i), prime, largePrime);  
  }
  int hash = 0; 
  for (int i = 0; i < needle.length(); i++) {
    hash = moduleHash(hash, haystack.charAt(i), prime, largePrime); 
  }
  if (hash == targetHash && equals(haystack, 0, needle)) return 0; 
  
  for (int i = 1; i <= haystack.length() - needle.length(); i++) {
    //module number is non-negative
    hash = nonNegative(hash - seed * haystack.charAt(i - 1) % largePrime, largePrime); 
    hash = moduleHash(hash, haystack.charAt(i + needle.length() -1), prime, largePrime); 
    //even if hash matches. there is still might be collision. we need to check if the substring really mathces
    if (hash == targetHash && equals(haystack, i, needle)) return i; 
  }
  return -1; 
}

private boolean equals(String haystack, String needle, int index) {
  for (int i = 0; i < needle.length(); i++) {
    if (haystack.charAt(i + index) != needle.charAt(i)) return false; 
  }
  return true; 
}

private int moduleHash(int hash, int addition, int prime, int largePrime) {
  return (hash * prime % largePrime + addition) % largePrime; 
}

private int nonNegative(int hash, int largeprime) {
  if (hash < 0) hash += largePrime; 
  return hash; 
}
```

TC: `O(k + N)`, the worst case is still O(k * N) 

### Repeated DNA Strings

All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T, for example: "ACGAATTCCG". When studying DNA, it is sometimes useful to identify repeated sequences within the DNA.

Write a function to find all the 10-letter-long sequences (substrings) that occur more than once in a DNA molecule.

`Input: s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"`

`Output: ["AAAAACCCCC", "CCCCCAAAAA"]`

(1) Brutal Force: for each of the substring of size 10, need to find the duplicated ones. 

use **Set<String>** to record all the traversed size 10 substrings 

TC: O(k * N)

```
public List<String> findRepeatedDnaSequences(String s) {
  Set<String> res = new HashSet<>(); 
  Set<String> seen = new HashSet<>(); 
  for (int i = 0; i + 9 < s.length(); i++) {
    String subs = s.substring(i, i + 10); 
    if (!seen.add(subs)) {
      res.add(subs); 
    }
  }
  return new LinkedList(res); 
}

```

(2) Rolling Hash: See this [post](https://leetcode.com/problems/repeated-dna-sequences/discuss/53867/Clean-Java-solution-(hashmap-+-bits-manipulation)?orderBy=most_votes) for detailed explanation 

Basically, this method maps the size 10 substrings into a 4-bytes number, it is possible because in this question they can have only 4 differfent characters.

0 = 00 (bits in binary number system) = 'A'

1 = 01 (bits in binary number system) = 'C'

2 = 10 (bits in binary number system) = 'G'

3 = 11 (bits in binary number system) = 'T'

Note that since there 10 letters and each letter requires only 2 bits, we will need only 10 * 2= 20 bits to code the string (which is less then size of integer in java (as well as in all othere popular languages), which is 4 bytes = 32 bits).

For example, this is how "AACCTCCGGT" string will be coded:

A A C C T C C G G T

00 00 01 01 11 01 01 10 10 11 = 00000101110101101011 (binary) = 23915 (decimal)

```
public List<String> findRepeatedDnaSequences(String s) {
    Set<Integer> words = new HashSet<>();
    Set<Integer> doubleWords = new HashSet<>();
    List<String> rv = new ArrayList<>();
    char[] map = new char[26];
    //map['A' - 'A'] = 0;
    map['C' - 'A'] = 1;
    map['G' - 'A'] = 2;
    map['T' - 'A'] = 3;

    for(int i = 0; i < s.length() - 9; i++) {
        int v = 0;  
        for(int j = i; j < i + 10; j++) {
            v <<= 2;   // move v left 2 bit, for example 01 after moving 0100
            v |= map[s.charAt(j) - 'A']; //Append 2 bits, 0100 | 11 => 0111
        }
        if(!words.add(v) && doubleWords.add(v)) {
            rv.add(s.substring(i, i + 10));
        }
    }
    return rv;
}
```

### Find all anagrams 

Using a variable `matched` to record **number of distinct characters already matched to the shorter string** 

use **HashMap<Character, count> + `matched`** to represent sliding window, which gives us: 

1. `add` fast --> O(1) 
2. `remove` slow --> O(1) 
3. `check` anagram --> check if `matched` == map.size();  

```
public List<Integer> findAnagrams(String s, String p) {
  List<Integer> res = new ArrayList<>(); 
  Map<Character, Integer> map = new HashMap<>(); 
  for (char c : p.toCharArray()) {
    map.put(c, map.getOrDefault(c, 0) + 1); 
  }
  int matched = 0; 
  for (int i = 0; i < s.length(); i++) {
    char cur = s.charAt(i); 
    if (map.containsKey(cur)) {
      map.put(cur, map.get(cur) - 1); 
      if (map.get(cur) == 0) matched++; 
    }
    if (i >= p.length()) {
      cur = s.charAt(i - p.length());
      if (map.containsKey(cur)) {
        map.put(cur, map.get(cur) + 1); 
        if (map.get(cur) == 1) count--;
      }
    }
    if (matched == map.size()) res.add(i - p.length() + 1)
  }
  return res; 
}
```

### Max value of each size k subarray

Example: `[1, 4, 3, 2, 1, 2] k = 3` 

result: `[4, 4, 3, 2]`

(1) Brute Force: for each of the sliding window of size k, find the **largest value**

TC: O(nk)

The question follows: With what data structure can we represent the sliding window efficiently? 

First we need to look at what operations we need to handle in this sliding window. 

1. **add** array[fast] - insert()
2. **remove** array[slow] - remove()
3. **getMax**  - max() 

Knowing that we need to handle three different operations, we can have the following options: 

1. `maxHeap` 
  + **add** O(logk)
  + **remove** O(n) --> can be reduced to O(logk) for lazy deletion
  + **getMax** O(1)
2. `treeMap/treeSet` 
  + **add** O(logk)
  + **remove** O(logk)
  + **getMax** O(logk)
3. `monotonically decreasing deque`
  + **add** O(1)  --> amortized 
  + **remove** O(1)
  + **getMax** O(1)
  
```
public int[] maxSlidingWindow(int[] nums, int k) {
  if (nums == null || nums.length == 0) return new int[0]; 
  int[] res = new int[nums.length - k + 1];
  Deque<Integer> dq = new ArrayDeque<>();    //dq to store index; 
  for (int i = 0; i < nums.length; i++) {
  //拿出旧又小的元素
    while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) dq.pollLast(); 
  //拿出过时的元素
    if (!dq.isEmpty() && i - k >= dq.peekFirst()) dq.pollFirst(); 
    dq.offerLast(i);
    if (i >= k - 1) {
      res[i - k + 1] = nums[dq.peekFirst()]; 
    }
  }
  return res; 
}
```

### K largest elements in each subarray of size n

Example: `[1, 4, 3, 2, 1, 2] n = 3  k = 2`

result: `<4,3>,<4,3>,<3,2>,<2,2>`

Can we still use `monotonically decreasing deque` ? **NO**, because we would lose values. 

`treeMap/treeSet` 
  + **add** O(logk)
  + **remove** O(logk)
  + **get K largest** O(logk + k)
  


### Contain Duplicates  

**(1) Given an array of integers, find if the array contains any duplicates. [Originial Problem see here](https://leetcode.com/problems/contains-duplicate/description/)**

```
Input: [1,2,3,1]
Output: true
```

(1) Use `Hashset` to check if a value already visited. 

TC: O(n) SC: O(n)

(2) Sort the array, the duplicate elements must be next to each other. 

TC: O(nlogn + n) SC: O(1) 

**(2) Given an array of integers and an integer k, find out whether there are two distinct indices i and j in the array such that nums[i] = nums[j] and the absolute difference between i and j is at most k. [Originial Problem](https://leetcode.com/problems/contains-duplicate-ii/description/)**

```
Input: nums = [1,2,3,1], k = 3
Output: true

Input: nums = [1,2,3,1,2,3], k = 2
Output: false
```

(1) for each `i`, check `i + 1` to `i + k`, if there is any elements equals to array[i]

TC: O(n * k)

(2) for each value, 找到左侧最近的出现过这个value的位置。 

Map<value, lastSeenIndex> 

TC : O(n) 

SC: worst case O(n) - no duplicates at all. 

```
public boolean containsNearbyDuplicate(int[] nums, int k) {
  Map<Integer, Integer> map = new HashMap<>(); 
  for (int i = 0; i < nums.length; i++) {
    if (map.containsKey(nums[i])) {
      if (i - map.get(nums[i]) <= k) return true; 
    }
    map.put(nums[i], i); 
  }
  return false; 
}
```

(3) to optimize **space consumption**, from `O(n)` to `O(k)`, we just need to check if the nearest K elements have the same value with the current value nums[i]. 

So the question breaks down to: 

1. Fixed size k sliding window 
2. HashSet to represent the size k sliding window


```
public boolean containsNearbyDuplicate(int[] nums, int k) {
  Set<Integer> set = new HashSet<>();
  for (int i = 0; i < nums.length; i++) {
  //maintain a k size window 
    if (!set.add(nums[i])) return true; 
   //remove slow 
    if (i >= k) {
      set.remove(nums[i - k]); 
    }
  }
  return false; 
}
```

**(3) Given an array of integers, find out whether there are two distinct indices i and j in the array such that the absolute difference between nums[i] and nums[j] is at most t and the absolute difference between i and j is at most k.**

```
Input: nums = [1,2,3,1], k = 3, t = 0
Output: true

Input: nums = [1,5,9,1,5,9], k = 2, t = 3
Output: false
```

Solution 1: 

1. Fixed size k sliding window 
2. For each value: 
  1. 最近的`k`元素里，比我大的最小的和比我小的最大值，绝对值是否 <= d

Operations we need: 
- insert()
- remove()
- largestSmallerOrEquals --> floor()
- smallestLargerOrEquals --> ceiling() 

```
 public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
  TreeSet<Long> window = new TreeSet<>(); 
  for (int fast = 0; fast < nums.length; fast++) {
  //invariant, check nums[fast], window record last k elements 
    long num = nums[fast]; 
    Long floor = window.floor(num); 
    if (floor != null && num - floor <= t) return true; 
    Long celling = window.ceiling(num); 
    if (celling != null && celling - num <= t) return true; 
    window.add(num); 
    //remove data 
    if (fast >= k) {
      window.remove((long)nums[fast - k]); 
    }
   }
   return false; 
}
```

**Note: we need to convert `Integer` to `Long` to prevent overflow in OJ**

Solution 2:

Using `buckets` to split the value range can reduce the TC from **O(nlogn)** to **O(n)**, [Here](https://leetcode.com/problems/contains-duplicate-iii/discuss/61645/AC-O(N)-solution-in-Java-using-buckets-with-explanation) is a detailed explanation on Leetcode. 
  
### Time and Value 

Given a sequence of event happened in **ascending** timestamps, each event is associated with a value, represented by two arrays. 

`int[] time =      {2,   3, 5, 7,  8,   9}`

`double[] value = {-3.2, 0, 2, 1, 0.8, -2}`

Also given an int **value k > 0**, we would like to calculate for each of the **event time** `x`, the average value of all the events that happened between time `[x - k, x + k]` 

Solution: 

For each **Time index i**, **[x - k, x + k]** maintain the sum of the sliding window

note: 

`left`: including 
`right` : not including

```
public double[] runningAvg(int[] time, double[] value, int k) {
  double[] avgs = new double[time.length]; 
  //initialization, must maintain its semantic meaning. 
  int left = 0; 
  int right = 0; //smallest time where diff > k 
  int sum = 0; 
  for (int i = 0; i < time.length; i++) {
    //find the correct right position
    while (right < time.length && time[right] - time[i] <= k) {
      sum += value[right];
      right++; 
    }
    while (time[i] - time[right] > k) {
      sum -= value[left]; 
      left++; 
    }
    avgs[i] = sum / (right - left); 
  }
  return avgs; 
}
```

### Task Scheduler 

要求：冷却时间`K`, 运行顺序不能变，完成任务的总时间.

example: 

`AABCB` `k = 2` 

return `A--ABC-B`

For each **task**, we only care about the tasks and their **execution time** in last k mins, which translates to a `K-size sliding window` 

Data structures we need: 

1. Map<Task, Last execution time> 
2. Queue<task> of size K. 
  
```
public int taskTime(chars[] tasks, int k) {
  Map<Character, Integer> taskTime = new HashMap<>(); 
  Queue<Character> window = new LinkedList<>(); 
  int currentTime = 0; 
  for (char t: tasks) {
    //sliding window: 前k个时间之内运行的taks和时间!
    //goal: determine the current time for "this" task
    Integer lastTime = taskTime.get(t); 
    if (lastTime == null) {
      currentTime++; 
    } else {
      currentTime = lastTime + k + 1;
    }
    
    //step1 : remove the previous task not in the sliding window
    while (!window.isEmpty() && currentTime - taskTime.get(window.peek()) > k) {
      taskTime.remove(window.poll()); 
    }
    //step2 : add the current task 
    window.offer(t); 
    taskTime.put(t, currentTime); 
  }
  return currentTime; 
} 
```

## Non Fixed size 
