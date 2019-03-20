# Quick Index 

+ **[Partition/Removal](#partition-and-removal)**
  + [Move Zeros](#move-zeros)
+ **[Sliding Windows](#sliding-windows)**
  + [Fixed size window](#fixed-size)
    + [Implement strStr](#implement-strstr)
    + [Repeated DNA Strings](#repeated-dna-strings)
    + [Find all anagrams](#find-all-anagrams)

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

