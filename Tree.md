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

