+ [Tree Traversals]()
  + [Pre-Order]()
  + [In-Order]()
  + [Post-Order]()
+ [Pure Recursion vs. Backtracking]()
  + [Find all visible nodes](#find-all-visible-nodes)
  + [Sum of all paths from root to leaf nodes in binary tree](#sum-of-all-paths-from-root-to-leaf-nodes-in-binary-tree)
 # Tree Traversal (pre, in, post-order)
 
 Recursive way is easy, let's us focus on the **iterative** way. 
 
 Traverse tree iteratively is generally not hard, it is very convenient to use a `Stack` data structure to simulate the recursion process. 
 Drawing out the simulation steps is very helpful. 
 
 **Example:**
 ```
      1 
    /   \
   2     3
  /     / \
 4     6   5 
  \ 
   7
```

### (1) Pre-Order Traversal 

pre-order:   `1 2 4 7 3 6 5`

```
Stack<TreeNode> stack; 
public List<Integer> preOrder(TreeNode root) {
  stack.push(root); 
  while (!stack.isEmpty()) {
    TreeNode cur = stack.pop(); 
    res.add(cur.val); 
    if (cur.right != null) stack.push(cur.right); 
    if (cur.left != null) stack.push(cur.left); 
  }
  return res; 
}
```
 
### (2) In-Order Traversal 

in-order sequence: `4 7 2 1 6 3 5` 
```
Stack<TreeNode> stack; 
public List<Integer> inOrder(TreeNode root) {
  pushLeft(root, stack); 
  while (!stack.isEmpty()) {
    TreeNode cur = stack.pop(); 
    res.add(cur.val); 
    pushLeft(cur.right, stack); 
  }
  return res; 
}

private void pushLeft(TreeNode cur, Stack<TreeNode> stk) {
  while (cur != null) {
    stk.push(cur); 
    cur = cur.right; 
  }
}
```
# Pure Recursion vs Backtracking 

1. Pure Recursion 
  - not using any side effect, each subtree is a smaller input on the same problem 
  - **从下往上反值**
  - 三部曲
  
  (1) What do we return 

  (2) What is the problem, what is the "task"

  (3) What is the subproblem's input 

  `[(1)return type] (2)theProblem((3)TreeNode rootOfSubtree)`
  
2. Backtracking 
 - with side effect, always recording necessary information of the current DFS path 
 - **从上往下传值（当前DFS信息）**
  - Doesn't need to be the whole list of path
  - Must be a property of the path 
  
### Find all visible nodes 

A node is visible only if its value is the largest one on the path from root to itself.

```
      2
    /  \ 
   7    1 
  / \  / \
  6  5 3 4 
```
all visible nodes: [2, 7, 3, 4]

Backtracking 
```
public void visibleNode(TreeNode root) {
  dfs(root, null); 
}

//max: max value of the path from root to cur(NOT INCLUDING CUR) 
private void dfs(TreeNode root, TreeNode max) {
  if (root == null) return null; 
  if (max == null || root.val > max.val) {
    root = max; 
    print(root); 
  }
  dfs(cur.left, max); 
  dfs(cur.right, max); 
}
```

### Sum of all paths from root to leaf nodes in binary tree

Intuitive: Backtracking
  1. find all the paths from root to leaf 
  2. what do we need to pass down 

```
int total = 0; 
//Invariant - curPathValue: value from root to cur(NOT INCLUDING CUR) 
public void sum(TreeNode cur, int curPathvalue) {
  if (cur == null) return; 
  curPathvalue = curPathvalue * 10 + cur.val; 
  if (cur.left == null && cur.right == null) {
    totol += curPathvalue;
    return; 
  }
  sum(cur.left, curPathvalue); 
  sum(cur.right, curPathvalue); 
}
```
 
# Modifying Tree Structures

### Remove all zero subtrees 

Recursion 定义：
1. **`Input`**: TreeNode root.
2. **`Problem`**: Change the subtree structure. 
3. **`Return`**: Return the changed structure, which is also the `root`.

```
public TreeNode deleteAllZero(TreeNode root) {
  if (root == null) return null; 
  root.left = deleteAllZero(root.left); 
  root.right = deleteAllZero(root.right); 
  
  //what is the change to the current root 
  //
  if (root.left == null && root.right == null && root.val == 0) {
    return null; 
  }
  return root; 
  
}
```

### Combine two Trees 

```
public TreeNode combineTree(TreeNode t1, TreeNode t2) {
  if (t1 == null && t2 == null) {
    return null;
  }
  if (t1 == null) return t2; 
  if (t2 == null) return t1; 
  
  //t1 != null && t2 != null  
  TreeNode root = new TreeNode(t1.val || t2.val); 
  root.left = combineTree(t1.left, t2.left); 
  root.right = combineTree(t2.right, t2.right);
  return root; 
} 
```
