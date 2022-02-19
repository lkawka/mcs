package anc.readers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import anc.models.Graph;
import anc.models.Input;

public class InputReader {

    public static Input read(String inputFilePath) {
        try (Scanner scanner = new Scanner(new File(inputFilePath))) {
            int n1 = scanner.nextInt();
            int[][] M1 = new int[n1][n1];
            for (int i = 0; i < n1; ++i)
                for (int j = 0; j < n1; ++j) {
                    M1[i][j] = scanner.nextInt();
                }
            Graph g1 = new Graph(n1, M1);

            int n2 = scanner.nextInt();
            int[][] M2 = new int[n2][n2];
            for (int i = 0; i < n2; ++i)
                for (int j = 0; j < n2; ++j) {
                    M2[i][j] = scanner.nextInt();
                }
            Graph g2 = new Graph(n2, M2);

            return new Input(g1, g2);
        } catch (IOException e) {
            throw new RuntimeException("Error with reading input file: " + e.getMessage());
        }
    }
}
