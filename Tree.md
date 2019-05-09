+ [Tree Traversals]()
  + [Pre-Order]()
  + [In-Order]()
  + [Post-Order]()

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

Modifying Tree Structures

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
