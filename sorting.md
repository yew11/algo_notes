# Sorting

## Categories of sorting algorithms
### Comparison based: 
  * **[MergeSort](#mergesort)** 
  * **[QuickSort](#quicksort)**
  * **[HeapSort](#heapsort)**
  * InsertSort
  * _more..._
### Non-Comparison based  
  * BuckSort 
  * CounSort 
  
_TODO (add algorithms)_
## MergeSort, QuickSort and HeapSort 
### MergeSort
1. Stable sort, order matters. 
2. Generally used to compare **Objects** in Java. 
3. Time: Guarantee O(nlogn) 
4. Space: Stack O(logn) + Heap O(n) 
5. Used in `Collections.sort()`

##### What are the steps to solve mergeSort:
1. Repeatedly divide the array from `mid` into smaller halves. 
2. Now we have a lot of single elements. We try to compare and to merge them one by one. 
3. Don't forget to initialize a helper array to temporarily store the answer. 

```
  public int[] mergeSort(int[] array) {
    if (array == null || array.length == 0) return array;
    int[] helper = new int[array.length];
    mergesort(array, helper, 0, array.length - 1);
    return array; 
  }
  private void mergesort(int[] array, int[] helper, int left, int right) {
    if (left >= right) return; 
    int mid = left + (right - left) / 2; 
    mergesort(array, helper, left, mid);
    mergesort(array, helper, mid + 1, right);
    merge(array, helper, left, mid, right);
  }
  private void merge(int[] array, int[] helper, int left, int mid, int right) {
    for (int i = left; i <= right; i++) {
      helper[i] = array[i];
    }
    int leftIdx = left; 
    int rightIdx = mid + 1; 
    while (leftIdx <= mid && rightIdx <= right) {
      if (helper[leftIdx] <= helper[rightIdx]) {
        array[left] = helper[leftIdx];
        left++;
        leftIdx++;
      } else {
        array[left] = helper[rightIdx];
        left++;
        rightIdx++;
      }
    }
    while (leftIdx <= mid) {
      array[left++] = helper[leftIdx++];
    }
    while (rightIdx <= right) {
      array[left++] = helper[rightIdx++];
    }
  }
```

### QuickSort
1. Non-stable sort, but generally speaking is faster than mergeSort in practice. 
2. Orders are not guranteed, which means it is not ideal if we want to maintain the originally relationship between each object. 
3. Time: Worst Case O(n^2) (already sorted), On average O(nlogn)
4. Space: O(n), on stack (from recursion)
5. Used in `Arrays.sort()`

##### What are the steps to solve quickSort:
1. Find a pivot in the array
2. Based on pivot, everything smaller than pivot will be on the left, everything larger will be on the right
3. keep partitioning the array until the whole array is sorted 

```
  public int[] quickSort(int[] array) {
    if (array == null || array.length == 0) {
      return array;
    }
    quick(array, 0, array.length - 1);
    return array;
  }
  
  private void quick(int[] array, int left, int right) {
    if (left >= right) return; 
    int pivot = partition(array, left, right);
    quick(array, left, pivot - 1);
    quick(array, pivot + 1, right);
  }
  
  private int partition(int[] array, int left, int right) {
    int index = left; 
    int pivot = array[right];
    for (int i = left; i < right; i++) {
      if (array[i] <= pivot) {
        swap(array, i, index);
        index++;
      }
    }
    swap(array, index, right);
    return index; 
  }

    private void swap(int[] array, int left, int right) {
    int temp = array[left];
    array[left] = array[right];
    array[right] = temp;
  }
```

### HeapSort
1. Non-stable sorting algorithm 
2. Guranteed Time O(nlogn)
3. Space O(1) 

##### What are the steps to solve heapSort:
1. heapify to a max heap
2. `poll()` the first from max heap and swap it with the last element in the element. 
3. `percolateDown()` to maintain the max heap property. 
4. some important properties to know heap. 
  * if `parent` index is `i`, then its `leftChild` index is `2*i +1` and `rightChild` index is `2*i + 2`
  * if we know the `child` index `i`, then the `parent` index is `(i - 1)/2` 

```
public void heapSort(int[] array) {
  //step1. heapify the array 
  heapify(array);
  
  //for each element, poll() it and push it to the end of array
  for (int i = array.length -1; i >= 0; i--) {
    swap(array, 0, i); 
    //maintain max heap property, and decrease the heap size by 1. 
    percolateDown(array, 0, i); 
  }
} 

private void heapify(int[] array) {
  int len = array.length; 
  for (int i = len / 2 - 1; i >= 0; i--) {
    percolateDown(array, i, len); 
  }
}

private void percolateDown(int[] array, int index, int len) {
  //until meeting the   
  while (index <= (len - 2) / 2) {
    int leftChild = 2 * index + 1; 
    int rightChild = 2 * index + 2; 
    int swap = leftChild;
    //compare left and right to find the smaller among the two. 
    if (rightChild < size && array[leftChild] < array[rightChild]) {
      swap = rightChild; 
    }
    //swap if necessary 
    if (array[index] < array[swap]) {
      swap(array, index, swap);  
    } else {
      break; 
    }
    index = swap; 
  }
}
```

 
