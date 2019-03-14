+ [Two Sum (1) - Mar 10th](two-sum)

# Two-Sum and Two-Diff problem 

### **Q1 Given a sorted array, determine `if there` is a pair of elements sum to target**

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

### **Q2 Given a sorted array, find `how many pairs` of elements sum to target**

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

### **Q3 Given an `unsorted` array, find `how many pairs` of elements sum to target**

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
 
 ### **Q4 Given two arbitary array (with duplicates), find all pairs with `largest Sum <= target`**
 
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




    
 
