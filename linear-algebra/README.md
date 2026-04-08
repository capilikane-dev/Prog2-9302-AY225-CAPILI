Math 101 – Linear Algebra
Assignment 01: Determinant of a 3×3 Matrix
UPHSD Molino Campus

---

Student Information
Name: CAPILI, JUSTIN KANE F.
Section: CS (uphsd-cs-capili-justinkane)
Course: Math 101 – Linear Algebra

---

Assignment Title
Assignment 01 – Determining the Determinant of a 3×3 Matrix Using Cofactor Expansion

---

Given Matrix

| 4  2  5 |
| 1  3  2 |
| 6  4  1 |

Row-major order: [4, 2, 5 | 1, 3, 2 | 6, 4, 1]

---

How to Run

Java Program
javac DeterminantSolver.java
java DeterminantSolver

JavaScript Program
node linear-algebra/DeterminantSolver.js

---

Sample Output

Student: CAPILI, JUSTIN KANE F.
Course : Math 101 - Linear Algebra
Assignment: Determinant of a 3x3 Matrix

Matrix:
| 4  2  5 |
| 1  3  2 |
| 6  4  1 |

Determinant = -68

---

Final Answer

det(A) = -68

---

Manual Computation (Cofactor Expansion)

det = 4(3×1 − 2×4) − 2(1×1 − 2×6) + 5(1×4 − 3×6)
det = 4(3 − 8) − 2(1 − 12) + 5(4 − 18)
det = 4(−5) − 2(−11) + 5(−14)
det = −20 + 22 − 70
det = −68

---

Conclusion

The determinant of the matrix is −68. Both Java and JavaScript programs give the same correct result.
