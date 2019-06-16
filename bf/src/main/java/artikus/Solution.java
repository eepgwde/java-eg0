package artikus;



class Solution {
    
    // The idea.
    int[] A;
    
    protected boolean strictly(int v, int u, int w) {
        return (v > Math.min(u, w) && v < Math.max(u,w));
    }
    
    protected boolean adjacent0(int p, int q) {
        for (int i=0; i<A.length; i++) {
            if (strictly(A[i], A[p], A[q]))
                return false;
        }
        return true;        
    }
    
    protected int distance(int p, int q) {
        // abs is not Math.abs.
        return Math.abs(A[p] - A[q]);
    }
    

    // 
    
    
    
    public int solution(int[] A) {
        this.A = A;
        
        int min = -2;
        for (int i=0; i<A.length-1; i++) {
            for (int j=i+1; j<A.length; j++) {
                // each pair of indices
                System.out.println(i + " " + j);
                if (adjacent0(i, j)) {
                    int d = distance(i,j);
                    System.out.println("d: " + d);
                    if (d > 100000000) d = -1;
                    else if (min == -2) {
                        min = d;
                    } else {
                        if (d < min) min = d;
                    }
                }
                System.out.println("min: " + min);
            }
        }
        return min;        
    }
    
    
}
