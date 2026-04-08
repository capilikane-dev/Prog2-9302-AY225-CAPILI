// ============================================================
// Student   : CAPILI, JUSTIN KANE F.
// Course    : Math 101 – Linear Algebra, UPHSD Molino Campus
// Assignment: Assignment 01 – Determinant of a 3×3 Matrix
// Date      : April 8, 2026
// Description: Hardcodes the assigned 3×3 matrix and computes
//              its determinant via cofactor expansion along
//              row 1. Prints each 2×2 minor, each cofactor
//              term, and the final determinant with labels
//              matching the required console output format.
// ============================================================

public class DeterminantSolver {

    public static void main(String[] args) {
        // Hardcoded assigned matrix for CAPILI, JUSTIN KANE F.
        // Source: Section 4 – Student Assignments (row-major order)
        int[][] matrix = {
            {4, 2, 5},
            {1, 3, 2},
            {6, 4, 1}
        };

        // Print the top banner with student info and the original matrix
        System.out.println("==================================================");
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: CAPILI, JUSTIN KANE F.");
        System.out.println("  Assigned Matrix:");
        System.out.println("==================================================");
        printMatrix(matrix);
        System.out.println("==================================================");
        System.out.println();

        // Compute and display the determinant with every expansion step shown
        int det = solveDeterminant(matrix);

        // Print the final result; flag the matrix as singular if det is zero
        System.out.println("==================================================");
        if (det == 0) {
            System.out.println("  The matrix is SINGULAR — it has no inverse.");
        }
        System.out.println("  \u2713 DETERMINANT = " + det);
        System.out.println("==================================================");
    }

    // Displays the 3x3 matrix rows in the | a  b  c | style expected by the rubric
    public static void printMatrix(int[][] m) {
        for (int[] row : m) {
            System.out.printf("  | %d  %d  %d |%n", row[0], row[1], row[2]);
        }
    }

    // Returns the determinant of a 2x2 sub-matrix whose four corners are supplied.
    // Calculation: (top-left × bottom-right) − (top-right × bottom-left)
    public static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    // Expands along the first row, printing each minor and cofactor step,
    // then assembles and returns the final determinant value.
    public static int solveDeterminant(int[][] m) {
        System.out.println("Expanding along Row 1 (cofactor expansion):");
        System.out.println();

        // Minor M11 — delete row 1 and col 1; remaining cells form the 2×2 minor
        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        System.out.printf("  Step 1 - Minor M\u2081\u2081: det([%d,%d],[%d,%d]) = (%d\u00d7%d) - (%d\u00d7%d) = %d - %d = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1],
            m[1][1]*m[2][2], m[1][2]*m[2][1], minor11);

        // Minor M12 — delete row 1 and col 2; note the sign flips for this term
        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        System.out.printf("  Step 2 - Minor M\u2081\u2082: det([%d,%d],[%d,%d]) = (%d\u00d7%d) - (%d\u00d7%d) = %d - %d = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0],
            m[1][0]*m[2][2], m[1][2]*m[2][0], minor12);

        // Minor M13 — delete row 1 and col 3; sign is positive again
        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
        System.out.printf("  Step 3 - Minor M\u2081\u2083: det([%d,%d],[%d,%d]) = (%d\u00d7%d) - (%d\u00d7%d) = %d - %d = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0],
            m[1][0]*m[2][1], m[1][1]*m[2][0], minor13);

        System.out.println();

        // Multiply each minor by its pivot element and apply the +−+ sign pattern
        int c11 =  m[0][0] * minor11;
        int c12 = -m[0][1] * minor12;
        int c13 =  m[0][2] * minor13;

        System.out.printf("  Cofactor C\u2081\u2081 = (+1) \u00d7 %d \u00d7 %d = %3d%n", m[0][0], minor11, c11);
        System.out.printf("  Cofactor C\u2081\u2082 = (-1) \u00d7 %d \u00d7 %d = %3d%n", m[0][1], minor12, c12);
        System.out.printf("  Cofactor C\u2081\u2083 = (+1) \u00d7 %d \u00d7 %d = %3d%n", m[0][2], minor13, c13);
        System.out.println();

        // Sum the three cofactor terms for the determinant
        int det = c11 + c12 + c13;
        System.out.printf("  det(M) = %d + (%d) + %d%n", c11, c12, c13);
        System.out.println();

        return det;
    }
}