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

// Hardcoded assigned matrix for CAPILI, JUSTIN KANE F.
// Source: Section 4 – Student Assignments (row-major order)
const matrix = [
  [4, 2, 5],
  [1, 3, 2],
  [6, 4, 1]
];

// Displays the 3x3 matrix rows in the | a  b  c | style expected by the rubric
const printMatrix = (m) => {
  m.forEach(row => {
    console.log(`  | ${row[0]}  ${row[1]}  ${row[2]} |`);
  });
};

// Returns the determinant of a 2x2 sub-matrix whose four corners are supplied.
// Calculation: (top-left × bottom-right) − (top-right × bottom-left)
const computeMinor = (a, b, c, d) => (a * d) - (b * c);

// Expands along the first row, printing each minor and cofactor step,
// then assembles and returns the final determinant value.
const solveDeterminant = (m) => {
  console.log("Expanding along Row 1 (cofactor expansion):");
  console.log();

  // Minor M11 — delete row 1 and col 1; remaining cells form the 2×2 minor
  const minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
  console.log(
    `  Step 1 - Minor M\u2081\u2081: det([${m[1][1]},${m[1][2]}],[${m[2][1]},${m[2][2]}])` +
    ` = (${m[1][1]}\u00d7${m[2][2]}) - (${m[1][2]}\u00d7${m[2][1]})` +
    ` = ${m[1][1]*m[2][2]} - ${m[1][2]*m[2][1]} = ${minor11}`
  );

  // Minor M12 — delete row 1 and col 2; note the sign flips for this term
  const minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
  console.log(
    `  Step 2 - Minor M\u2081\u2082: det([${m[1][0]},${m[1][2]}],[${m[2][0]},${m[2][2]}])` +
    ` = (${m[1][0]}\u00d7${m[2][2]}) - (${m[1][2]}\u00d7${m[2][0]})` +
    ` = ${m[1][0]*m[2][2]} - ${m[1][2]*m[2][0]} = ${minor12}`
  );

  // Minor M13 — delete row 1 and col 3; sign is positive again
  const minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
  console.log(
    `  Step 3 - Minor M\u2081\u2083: det([${m[1][0]},${m[1][1]}],[${m[2][0]},${m[2][1]}])` +
    ` = (${m[1][0]}\u00d7${m[2][1]}) - (${m[1][1]}\u00d7${m[2][0]})` +
    ` = ${m[1][0]*m[2][1]} - ${m[1][1]*m[2][0]} = ${minor13}`
  );

  console.log();

  // Multiply each minor by its pivot element and apply the +−+ sign pattern
  const c11 =  m[0][0] * minor11;
  const c12 = -m[0][1] * minor12;
  const c13 =  m[0][2] * minor13;

  console.log(`  Cofactor C\u2081\u2081 = (+1) \u00d7 ${m[0][0]} \u00d7 ${minor11} = ${c11 >= 0 ? ' ' : ''}${c11}`);
  console.log(`  Cofactor C\u2081\u2082 = (-1) \u00d7 ${m[0][1]} \u00d7 ${minor12} = ${c12 >= 0 ? ' ' : ''}${c12}`);
  console.log(`  Cofactor C\u2081\u2083 = (+1) \u00d7 ${m[0][2]} \u00d7 ${minor13} = ${c13 >= 0 ? ' ' : ''}${c13}`);
  console.log();

  // Sum the three cofactor terms for the determinant
  const det = c11 + c12 + c13;
  console.log(`  det(M) = ${c11} + (${c12}) + ${c13}`);
  console.log();

  return det;
};

// ── Main execution ────────────────────────────────────────────
// Print the top banner with student info and the original matrix
console.log("==================================================");
console.log("  3x3 MATRIX DETERMINANT SOLVER");
console.log("  Student: CAPILI, JUSTIN KANE F.");
console.log("  Assigned Matrix:");
console.log("==================================================");
printMatrix(matrix);
console.log("==================================================");
console.log();

// Run the step-by-step cofactor expansion
const det = solveDeterminant(matrix);

// Display the final answer; flag as singular if the determinant is zero
console.log("==================================================");
if (det === 0) {
  console.log("  The matrix is SINGULAR — it has no inverse.");
}
console.log(`  \u2713 DETERMINANT = ${det}`);
console.log("==================================================");