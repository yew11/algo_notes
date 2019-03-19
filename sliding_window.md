# Quick Index 

+ **[Partition/Removal](#partition-and-removal)**
  + [Move Zeros](#move-zeros)
+ **[Sliding Windows](#sliding-windows)**
  + [Fixed size window](#fixed-size)

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

TC: O(k * N)  `k` is the shorter string and `N` is the larger string. 

If we want to optimize the solution, we want to reduce the time spent on **Checking**  strings 

