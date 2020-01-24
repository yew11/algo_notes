package pointersAndSlindingWindow;

class KPointers {
    public boolean findPair(int[] A, int[] B, int target) {
        int i = 0;
        int j = B.length - 1;
        while (j >= 0 && i < A.length) {
            int curTarget = A[i] + B[j];
            if (curTarget == target) {
                return true;
            } else if (curTarget < target) {
                i++;
            } else {
                j--;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        KPointers kp = new KPointers();
        int[] A = {1, 3, 5};
        int[] B = {3, 4, 6};
        System.out.println(kp.findPair(A, B, 10));
    }
}
