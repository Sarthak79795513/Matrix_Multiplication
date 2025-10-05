import javax.swing.*;
import java.awt.*;

public class MatrixGUI extends JFrame {

    private JTextArea resultArea;

    public MatrixGUI() {
        setTitle("Mini Project: DAA Matrix Multiplication");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header ---
        JLabel header = new JLabel("Mini Project: DAA Matrix Multiplication", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180)); // Steel blue
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // --- Result Area ---
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(new Color(30, 30, 40));
        resultArea.setForeground(Color.GREEN);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // --- Start input & computation ---
        SwingUtilities.invokeLater(this::getInputAndCompute);
    }

    private void getInputAndCompute() {
        try {
            // Matrix dimensions
            int rowsA = Integer.parseInt(JOptionPane.showInputDialog("Enter rows for Matrix 1:"));
            int colsA = Integer.parseInt(JOptionPane.showInputDialog("Enter columns for Matrix 1:"));
            int rowsB = Integer.parseInt(JOptionPane.showInputDialog("Enter rows for Matrix 2:"));
            int colsB = Integer.parseInt(JOptionPane.showInputDialog("Enter columns for Matrix 2:"));

            if (colsA != rowsB) {
                JOptionPane.showMessageDialog(this,
                        "Matrix multiplication not possible! Columns of Matrix 1 must equal rows of Matrix 2.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Matrix inputs
            int[][] A = new int[rowsA][colsA];
            int[][] B = new int[rowsB][colsB];

            for (int i = 0; i < rowsA; i++)
                for (int j = 0; j < colsA; j++)
                    A[i][j] = Integer.parseInt(JOptionPane.showInputDialog("Matrix 1 [" + i + "][" + j + "] = "));

            for (int i = 0; i < rowsB; i++)
                for (int j = 0; j < colsB; j++)
                    B[i][j] = Integer.parseInt(JOptionPane.showInputDialog("Matrix 2 [" + i + "][" + j + "] = "));

            // Sequential multiplication
            long startSeq = System.nanoTime();
            int[][] resultSeq = MatrixOperations.multiplySequential(A, B);
            long endSeq = System.nanoTime();
            double timeSeq = (endSeq - startSeq) / 1e6;

            // Multithreaded multiplication
            long startThread = System.nanoTime();
            int[][] resultThread = MatrixOperations.multiplyThreadPerRow(A, B);
            long endThread = System.nanoTime();
            double timeThread = (endThread - startThread) / 1e6;

            // Display results & analysis
            StringBuilder sb = new StringBuilder();
            sb.append("Result Matrix (Sequential):\n")
                    .append(MatrixOperations.matrixToString(resultSeq))
                    .append(String.format("Execution Time: %.3f ms\n\n", timeSeq));

            sb.append("Result Matrix (Multithreaded Row-wise):\n")
                    .append(MatrixOperations.matrixToString(resultThread))
                    .append(String.format("Execution Time: %.3f ms\n\n", timeThread));

            sb.append("Analysis:\n");
            if (timeThread < timeSeq)
                sb.append("Multithreaded method is faster.\n");
            else if (timeThread > timeSeq)
                sb.append("Sequential method is faster.\n");
            else
                sb.append("Both methods have similar performance.\n");

            resultArea.setText(sb.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input! " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatrixGUI().setVisible(true));
    }
}
