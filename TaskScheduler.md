# Task Scheduler

You can find the problem description [here](https://leetcode.com/problems/task-scheduler/description/).

Given a char array representing tasks CPU need to do. It contains capital letters A to Z where different letters represent different tasks. Tasks could be done without original order. Each task could be done in one interval. For each interval, CPU could finish one task or just be idle.

However, there is a non-negative cooling interval n that means between two **same tasks**, there must be at least n intervals that CPU are doing different tasks or just be idle.

You need to return the **least** number of intervals the CPU will take to finish all the given tasks.

Example: 

```
Input: tasks = ["A","A","A","B","B","B"], n = 2
Output: 8
Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.
```

翻译：我们要求安排CPU的任务，规定在两个相同任务之间至少隔n个时间点。

## Solution 1 - PriorityQueue

与 `LC 767 Reorganize String` 解法类似，是一个类似greedy的解法。我们先统计每个字符出现的次数。比如以上面这个列子来说，其中`A`出现了3次，`B`也出现了3次。如果`n = 1` 的话（n为两个相同task中间所间隔的长度），那么这个tasks最后可以表现成这样：`ABABAB`，答案为**6**。

那么如果`n = 2`呢？说明两个相邻的task之间必须要隔两个位置，答案为 `AB_AB_AB`，由此可见，不管N等于多少，A和B的相对位置不会有变化。

具体的思路是建立一个`最大的优先队列`(Max Heap)，把每个字符出现的次数都放在优先队列里面，这样出现次数最多的字符会在最前面 

这道题的本质是要将统计好的字符来分 **”块“**，什么是分块？ 比如按照上面的列子来说的话：每个A后面必须要隔两个非A的task， 即：

`A_ _|A_ _|A_ _|A....`

那么“每一块” 都有`n+1`个任务（字符），接下来我们要做的就是从PQ里面依次拿字符来填充这个`n+1`个坑。

(1) 我们取出来任务（字符）后，将它们都放在一个temp的临时数组里面。

(2) 然后再遍历这个数组，目的是将其哈希表映射的次数减1。如果减1后，次数仍大于0，则将此任务次数再次排入队列中 `(pq.offer())`。

(3) 遍历完成后如果优先队列**还不为空**，那么说明这`n + 1`个坑全部被填满，结果需要加上`n + 1`。

(4) 我们最理想的情况下是每次恰好取`n + 1`个，但是如果此时PQ里面**少于**`n + 1`个元素，我们就只好记录他**真实**的个数，最后结果加上真实的个数即可。参见代码如下：


```
public int leastInterval(char[] tasks, int n) {
    //count the number of each chars' apperence
    Map<Character, Integer> freqMap = new HashMap<>(); 
    for (char task : tasks) {
        map.put(task, map.getOrDefault(task, 0) + 1); 
    }

    //create a max heap to begin processing the tasks
    //Note: here we only need to care about a "number", so we can only offer the frequencies into pq 
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> (b - a)); 
    for (char task : map.keySet()) {
        pq.offer(map.get(task)); 
    }

    //initialize the final result
    int res = 0; 
    while (!pq.isEmpty()) {
        //this is 真实的个数 we talked about above 
        int workTime = 0; 
        List<Integer> tempList = new ArrayList<>(); 
        for (int i = 0; i < n + 1; i++) {
            if (!pq.isEmpty()) {
                tempList.add(pq.poll()); 
                workTime++; 
            }
            //now we decrease the count in the pq, if its still greater than 0, offer back to pq
            for (int num : tempList) {
                if (--num > 0) {
                    pq.offer(num); 
                }
            }
            //finally, we check should we add n + 1 (满装) or workTime(真实的个数)
            res += !pq.isEmpty() ? n + 1: workTime; 
        }
        return res; 
    }
}
```


## Solution 2
更多详情[参考该贴](http://www.cnblogs.com/grandyang/p/7098764.html)。

由于题目中规定了两个相同任务之间至少隔`n`个时间点，那么我们首先应该处理的出现**次数最多**的那个任务，先确定好这些高频任务，然后再来安排那些低频任务。

如果任务**A**的出现频率最高，为k次，那么我们用n个空位将**每两个A**分隔开，然后我们按顺序加入其他低频的任务。

**Example 1：**

`AAAABBBEEFFGG  3`

我们发现任务A出现了4次，频率最高，于是我们在每个A中间加入三个空位，如下：

A---A---A---A

AB--AB--AB--A   (加入B)

ABE-ABE-AB--A   (加入E)

ABEFABE-ABF-A   (加入F，每次尽可能填满或者是均匀填充)

ABEFABEGABFGA   (加入G)

**Example 2：**

`ACCCEEE  2`

我们发现任务C和E都出现了三次，那么我们就将CE看作一个整体，在中间加入一个位置即可：

CE-CE-CE

CEACE-CE   (加入A)

注意最后面那个idle不能省略，不然就不满足相同两个任务之间要隔2个时间点了。

我们仔细观察上面两个例子可以发现，最终结果都被分成了`(max - 1)`块，再加上`最后面的字母`，其中`max`为**最大出现次数**。

比如，`A`出现了`4`次，所以有`A---`模块出现了3次，再加上`最后的A`，每个模块的长度为4。

例子2中，`CE-`出现了`2`次，再加上`最后的CE`，每个模块长度为3。

我们可以发现，**"块"**的次数为**任务最大次数减1**，"块"的长度为`n+1`，最后加上的字母个数为出现次数最多的任务，可能有多个并列。

(1) 我们统计每个大写字母出现的次数，然后排序，这样**出现次数最多的字母**就到了末尾

(2) 然后我们向前遍历，找出出现次数一样多的任务个数，就可以迅速求出总时间长了 

```
public int leastInterval(char[] tasks, int n) { 
    int[] c = new int[26]; 
    for (char task : tasks) {
        c[task - 'A']++; 
    }
    Arrays.sort(c); 
    int i = 25; 
    while (i >= 0 && c[i] == c[25]) i--; 
    return Math.max(tasks.length, (c[25] - 1) * (n + 1) + 25 - i); 
}
```

这段代码可能最不好理解的可能就是最后一句了，那么我们特别来讲解一下:

先看括号中的第二部分，前面分析说了`max`是出现的最大次数，`max-1`是可以分为的"块"，`n+1`是每块中的个数，而后面的`25-i`是还需要补全的个数，

用之前的例子来说明：

`AAAABBBEEFFGG 3`

A出现了4次，最多，max=4，那么可以分为`max-1 = 3`块，如下：

A---A---A---

每块有`n+1=4`个，最后还要加上末尾的一个A，也就是25-24=1个任务，最终结果为13：ABEFABEGABFGA

再来看另一个例子：

`ACCCEEE 2`

C和E都出现了3次，最多，max=3，那么可以分为max-1=2块，如下：

CE-CE-

每块有n+1=3个，最后还要加上末尾的一个CE，也就是25-23=2个任务，最终结果为8：

CEACE-CE

好，那么此时你可能会有疑问，为啥还要跟原任务个数len相比，取较大值呢？我们再来看一个例子：

AAABBB 0

A和B都出现了3次，最多，max=3，那么可以分为max-1=2块，如下：

ABAB

每块有n+1=1个？你会发现有问题，这里明明每块有两个啊，为啥这里算出来n+1=1呢，因为给的n=0，这有没有矛盾呢，没有！

因为`n`表示**相同的任务间需要间隔的个数**，那么既然这里为0了，说明相同的任务可以放在一起，这里就没有任何限制了，

我们只需要执行完所有的任务就可以了，所以我们最终的返回结果一定不能小于任务的总个数len的，这就是要对比取较大值的原因了。
