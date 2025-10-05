public class MatrixOperations {

   
    public static int[][] multiplySequential(int[][] A, int[][] B) {
        int n = A.length;       // rows of A
        int m = A[0].length;    // cols of A = rows of B
        int p = B[0].length;    // cols of B

        int[][] C = new int[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < m; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

  
    static class RowMultiplier extends Thread {
        private final int[][] A, B, C;
        private final int row;

        RowMultiplier(int[][] A, int[][] B, int[][] C, int row) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.row = row;
        }

        @Override
        public void run() {
            int m = A[0].length;
            int p = B[0].length;
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < m; k++) {
                    C[row][j] += A[row][k] * B[k][j];
                }
            }
        }
    }

    public static int[][] multiplyThreadPerRow(int[][] A, int[][] B) throws InterruptedException {
        int n = A.length;
        int p = B[0].length;
        int[][] C = new int[n][p];
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new RowMultiplier(A, B, C, i);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        return C;
    }

    // Convert matrix to string
    public static String matrixToString(int[][] M) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : M) {
            for (int val : row) {
                sb.append(val).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    
    public static int[][] parseMatrix(String text, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        String[] lines = text.trim().split("\n");
        if (lines.length != rows)
            throw new IllegalArgumentException("Matrix must have " + rows + " rows.");

        for (int i = 0; i < rows; i++) {
            String[] nums = lines[i].trim().split("\\s+");
            if (nums.length != cols)
                throw new IllegalArgumentException("Each row must have " + cols + " columns.");
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(nums[j]);
            }
        }
        return matrix;
    }
}
