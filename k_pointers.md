# Quick Index
+ **[Two Sum (1) - Mar 10th](#two-sum-and-two-diff-problem)**
    + [Exist a 2-sum pair](#given-a-sorted-array-determine-if-exist-a-pair-of-elements-sum-to-target)
    + [Number of 2-sum pairs](#given-a-sorted-array-find-how-many-pairs-sum-to-target)
    + [Unsorted number of 2-sum pairs](#given-an-unsorted-array-find-how-many-pairs-of-elements-sum-to-target)
+ **[Two Sum (2) - Mar 13th](#two-sum-and-two-diff-problem)**
    + [Largest sum <= target](#given-two-arbitary-array-find-all-pairs-with-largest-sum-smaller-than-target)
    + [Triangle problem](#given-an-integer-sorted-array-with-all-positive-integer-values)
    + [Two-diff](#two-diff-problems)
    + [max(subset) - min(subset) <= target)](#bar-raiser)

# Two-Sum and Two-Diff problem 

### **Given a sorted array, determine if exist a pair of elements sum to target**

1. Starting from `brute Force` way of thinking 

    For all possible pairs (i, j) check if array[i] + array[j] = target 
 
2. 因为`i`,`j` 两个指针都要移动，所以时间复杂度为O(n^2), 如何进一步优化？

    只让一个指针移动，固定一个dimension！

    + `binary search` using sorted array 
    + `HashSet` record all the elements before `j`
    + `Two pointers` traversing the array starting in the opposite direction 

```
//two pointers 
public boolean twoSum(int[] array, int target) {
  int left = 0; 
  int right = array.length - 1;
  while (left < right) {
    sum = array[left] + array[right]; 
    if (sum == target) return true; 
    else if (sum < target) left++;  // 固定j
    else right++;
  }
  return false; 
}
```

### **Given a sorted array, find how many pairs sum to target**

**(1) Sorted, No duplicate**

the only difference from the previous question is to maintain an `answer count`, when we have a sum equals to the target, increment the count and move any of the one pointers one step. 

``` 
public int twoSum(int[] array, int target) {
  int left = 0; 
  int right = array.length - 1;
  int count = 0; 
  while (left < right) {
    sum = array[left] + array[right]; 
    if (sum == target) {
      count++; 
      right--; 
    } else if (sum < target) left++;  // 固定j
    else right++;
  }
  return count; 
}
```

**(2) Sorted, duplicate, but we don't count the duplicate pairs** 

e.g: `[1, 1, 3, 3] target = 4, return 1` 

This is also easy because we can `skip the duplicate elements` (by moving `right` pointer). 

```
public int twoSum(int[] array, int target) {
  int left = 0; 
  int right = array.length - 1;
  int count = 0; 
  while (left < right) {
    sum = array[left] + array[right]; 
    if (sum == target) {
      count++; 
      //ONLY DIFFERENCE 
      while (right > left && array[right] == array[right - 1]) {
        right--;    // we arrive at the FIRST dup element. 
      }
      right--; 
    } else if (sum < target) left++;  // 固定j
    else right++;
  }
  return count; 
}
```

**(3) Sorted, duplicate, and we count the duplicate pairs** 

e.g: `[1, 1, 3, 3] target = 4, return 4` 

算法比较直接：找出有几个`left` pointer 和有几个`right`pointer的和加起来等于`target`, 然后相乘，但是实现起来要特别注意! 

```
public int twoSum(int[] array, int target) {
  int left = 0; 
  int right = array.length - 1;
  int count = 0; 
  while (left < right) {
    sum = array[left] + array[right]; 
    if (sum == target) {
      //CASE 1: array[left] == array[right] --> [2, 2, 2] target = 4 C3,2
      if (array[left] == array[right]) {
        count += (right - left + 1) * (right - left) / 2;   // (2 - 0 + 1) * (2 - 0) / 2 = 3  
        break; 
      }
      //CASE 2: 1, 1, 3, 3
      int leftCount = 1; 
      int rightCount = 1; 
      //Get how many left pointer that is equals to the target
      while (left + 1 < right && array[left] == array[left + 1]) {
        leftCount++; 
        left++; 
      }
      //get the right pointers
      while (right > left + 1 && array[right] == array[right - 1]) {
        rightCount++; 
        right--; 
      }
      left++;
      right--; 
      count += leftCount * rightCount; 
    } else if (sum < target) left++;  // 固定j
    else right++;
  }
  return count; 
}
```

### **Given an unsorted array, find how many pairs of elements sum to target**

e.g: `[1, 3, 1, 3] target = 4, return 4` 

两种解法： 
  * 先sort，然后按照上面的解法
  * 不sort，但需要借助其他辅助数据结构。 
  
 想法是： 固定了右边index j，有多少i (i < j), array[i] = target - array[j] 
 
 那么用什么数据结构比较好呢？ 第一想到是`HashMap`, Key是 `array当前元素`， Value是 `该元素之前出现次数`。 
 
 用上面的列子来实验： 
 
   `[1, 3, 1, 3] target = 4`
  
 1. `i` = 0,  array[i] = 1, Map<>, Map.get(4 - 1) = 0, count + 0 = 0, Map.put(1, 1)   
 2. `i` = 1,  array[i] = 3, Map<1, 1>, Map.get(4 - 3) = 1, count + 1 = 1, Map.put(3, 1)
 3. `i` = 2,  array[i] = 1, Map<1, 1> <3, 1>, Map.get(4 -1) = 1 count + 1 = 2, Map.put(1, 2) 
 4. `i` = 3,  array[i] = 3, Map<1, 2> <3, 1>, Map.get(4 -3) = 2, count + 2 = 4, Map.put(3, 2)
 
 return **4**
  
 ```
 public int twoSum(int[] array, int target) {
  int count = 0; 
  Map<Integer, Integer> map = new HashMap<>(); 
  for (int i = 0; i < array.length; i++) {
    count += map.getOrDefault(target - array[i], 0); 
    map.put(array[i], map.getOrDefault(array[i], 0) + 1); 
  }
  return count; 
 }
 ```
 
 ### **Given two arbitary array, find all pairs with largest sum smaller than target**
 
 note: possibly with duplicates. 
 
 ```
 A: [2, 1, 3, 3, 5]
 B: [4, 4, 9, 4, 9]
 target = 8 
```
Solution 1: 

先分别将两个数组排序，排序后数组变成： 

A: [1, 2, **3, 3**, 5]

B: [**4, 4, 4**, 9, 9]

直观可见，A数组中的两个`3`和B中的三个`4`为答案，最后答案为6。

**Follow up: What if we want to return the index**


### **Given an integer sorted array with all positive integer values**

**(1) Can we pick three elements in the array as the lengths of edges, to contrust any `triangles`**

Example: 

array = [1, 2, 3, 4], return `True`. (2, 3, 4) is a valid triangle. 

Solution: 

How to construct a triangle?

两边之和大于第三边： x + y > z 

So, the problem translates to: **Find if there is any index tuple, such i < j < k and array[i] + array[j] > array[k]**

题目只要找出一对满足要求的tuple，那我们选择固定最右边的K，所以 i = k - 1， j = k - 2

```
public boolean validTriangle(int[] array) {
    for (int i = array.length - 1; i > 1; i--) {
        if (array[i] < array[i - 1] + array[i - 2]) return true; 
        else continue; 
    }    
}
```

**(2) How `many` triangles can we get, still using the sorted array**

Example : 

array = [2, 3, 4, 5], return 3 

answers: `2, 3, 4`, `3, 4, 5`, `2, 4, 5` 

解题思路还跟上题一样，找出是否有index tuple，并且有几对，满足array[i] + array[j] > array[k] 

Solution：

固定K，找到有几对pairs of (i, j)，要求i < j < k, array[i] + array[j] > array[k]。

你可能已经看到了，这道题可以转换成`2 sum larger than target`的经典问题。

```
public int validTrianglePairs(int[] array) {
    int count = 0; 
    for (int i = array.length - 1; i > 1; i--) {
        int left = 0;
        int right = i - 1; 
        while (left < right) {
            if (array[left] + array[right] > array[i]) {
                count += (right - left); 
                right--; 
            } else left++; 
        }
        return count; 
    }
}
```

可能要解释一下 `count += (right - left)`这句话，因为 array是sorted的性质，又因为如果left+right已经大于了target，那么left和right中间所有的元素也都大于target。

### **Two-Diff problems**

#### How many pairs diff = target(< target, > target), where target > 0? 

**(1) The array is sorted, no duplicates**

Example: 

array = [1, 2, 3, 4] target = 2, return 2 (3-1), (4-2)

Solution:

还是和之前一样，固定J，只不过这一次同向而行，找出所有的i < j, such that array[i] = array[j] - diff 

```
public int diffPairs(int[] A, int diff) {
    int res = 0;
    int i = 0, j = 1; 
    while (j < A.length) {
        if (A[i] == A[j] - diff) {
            j++; 
            res++; 
        } else if (A[i] < A[j] - diff) {
            i++; 
        } else {
            j++; 
        }
    }
    return res; 
}
```

**(2) Follow up: how many pairs diff > target**

array = [1, 2, 3, 4, 5] target = 2, return 2 (4-1), (5-2)


```
public int diffPairs(int[] A, int diff) {
    int res = 0;
    int i = 0, j = 1; 
    while (j < A.length) {
        if (A[i] >= A[j] - diff) {
            j++; 
            res += i; 
        } else { //A[j] - A[i] > target
            i++; 
        }
    }
    return res; 
}
```

**(3) Follow up: The array is unsorted** 

array = [1, 3, 1, 3] target = 2, return 4 

Solution 1: sort the array first. 

Solution 2: 

(1) Find all pairs (i, j) such that i < j, and array[j] - array[i] = target.

(2) For each j, 
   
   Find numbers of i < j, that array[i] = array[j] - target
   Find numbers of i < j, that array[i] = array[j] + target
    
```
public int findPairs(int[] A, int diff) {
    //maintain a count for each value traversed so far
    Map<Integer, Integer> map = new HashMap<>(); 
    int count = 0; 
    for (int ele : A) {
        count += map.getOrDefault(ele - diff, 0); 
        count += map.getOrDefault(ele + diff, 0); 
        map.put(ele, map.getOrDefault(ele, 0) + 1); 
    }
    return count; 
}
```

### Bar-raiser 
**Given a sorted array, find the number of subsets that max(subset) - min(subset) <= target**

clarification: target > 0,  no duplicates in the sorted array, subset size >= 1

Example: 

array = [1, 3, 5, 6, 8], target = 4 

分解方式：

for each j (array[j] as the `max` number in the subsets ending at j):

**把Array[j]做为最大值！**

we find the leftmost `i` such that i <= j, and array[j] - array[i] <= target

```
public int findSubset(int[] A, int target) {
    int i = 0; 
    int j = 0; 
    int res = 0; 
    while (j < A.length) {
        if (A[j] - A[i] <= target) {
            res += 1 << (j - i); 
            j++; 
        } else {
            i++; 
        }
    }
    return res; 
}
```

 
