/**
 * CSV Analytics Report Generator
 * Prompts user for CSV file path, validates it, performs analytics,
 * and exports a summary_report.csv
 */

const fs = require('fs');
const path = require('path');
const readline = require('readline');

// ─── Module: File Validator ───────────────────────────────────────────────────

function validateFilePath(filePath) {
  if (!fs.existsSync(filePath)) {
    return { valid: false, error: 'File does not exist.' };
  }
  const stats = fs.statSync(filePath);
  if (!stats.isFile()) {
    return { valid: false, error: 'Path is not a file.' };
  }
  if (!filePath.toLowerCase().endsWith('.csv')) {
    return { valid: false, error: 'File is not a CSV file (.csv extension required).' };
  }
  try {
    fs.accessSync(filePath, fs.constants.R_OK);
  } catch {
    return { valid: false, error: 'File is not readable (permission denied).' };
  }
  return { valid: true };
}

// ─── Module: CSV Parser ───────────────────────────────────────────────────────

function parseCSV(filePath) {
  const content = fs.readFileSync(filePath, 'utf-8');
  const lines = content.trim().split('\n').filter(line => line.trim() !== '');

  if (lines.length < 2) {
    throw new Error('CSV file must have a header row and at least one data row.');
  }

  const headers = lines[0].split(',').map(h => h.trim().replace(/^"|"$/g, ''));
  const records = [];

  for (let i = 1; i < lines.length; i++) {
    const values = lines[i].split(',').map(v => v.trim().replace(/^"|"$/g, ''));
    if (values.length !== headers.length) continue; // skip malformed rows
    const record = {};
    headers.forEach((header, idx) => {
      record[header] = values[idx];
    });
    records.push(record);
  }

  if (records.length === 0) {
    throw new Error('No valid data records found in CSV file.');
  }

  return { headers, records };
}

// ─── Module: Analytics Engine ─────────────────────────────────────────────────

function detectNumericColumns(headers, records) {
  return headers.filter(header =>
    records.every(r => r[header] !== '' && !isNaN(Number(r[header])))
  );
}

function computeAnalytics(headers, records) {
  const totalRows = records.length;
  const numericCols = detectNumericColumns(headers, records);
  const analytics = { totalRows, columns: headers, numericSummaries: {} };

  for (const col of numericCols) {
    const values = records.map(r => Number(r[col]));
    const sum = values.reduce((a, b) => a + b, 0);
    const mean = sum / values.length;
    const sorted = [...values].sort((a, b) => a - b);
    const min = sorted[0];
    const max = sorted[sorted.length - 1];
    const mid = Math.floor(sorted.length / 2);
    const median = sorted.length % 2 === 0
      ? (sorted[mid - 1] + sorted[mid]) / 2
      : sorted[mid];
    const variance = values.reduce((acc, v) => acc + Math.pow(v - mean, 2), 0) / values.length;
    const stdDev = Math.sqrt(variance);

    analytics.numericSummaries[col] = {
      sum: sum.toFixed(2),
      mean: mean.toFixed(2),
      median: median.toFixed(2),
      min: min.toFixed(2),
      max: max.toFixed(2),
      stdDev: stdDev.toFixed(2),
      count: values.length
    };
  }

  // Categorical column value counts
  const categoricalCols = headers.filter(h => !numericCols.includes(h));
  analytics.categoricalSummaries = {};
  for (const col of categoricalCols) {
    const counts = {};
    records.forEach(r => {
      const val = r[col] || '(empty)';
      counts[val] = (counts[val] || 0) + 1;
    });
    analytics.categoricalSummaries[col] = counts;
  }

  return analytics;
}

// ─── Module: Display Formatter ────────────────────────────────────────────────

function displayResults(analytics, filePath) {
  const divider = '='.repeat(60);
  const subDivider = '-'.repeat(60);

  console.log('\n' + divider);
  console.log('         CSV ANALYTICS REPORT');
  console.log(divider);
  console.log(`  Source File : ${filePath}`);
  console.log(`  Total Rows  : ${analytics.totalRows}`);
  console.log(`  Columns     : ${analytics.columns.join(', ')}`);
  console.log(divider);

  if (Object.keys(analytics.numericSummaries).length > 0) {
    console.log('\n  NUMERIC COLUMN SUMMARIES\n' + subDivider);
    for (const [col, stats] of Object.entries(analytics.numericSummaries)) {
      console.log(`\n  Column: ${col}`);
      console.log(`    Count   : ${stats.count}`);
      console.log(`    Sum     : ${stats.sum}`);
      console.log(`    Mean    : ${stats.mean}`);
      console.log(`    Median  : ${stats.median}`);
      console.log(`    Min     : ${stats.min}`);
      console.log(`    Max     : ${stats.max}`);
      console.log(`    Std Dev : ${stats.stdDev}`);
    }
  }

  if (Object.keys(analytics.categoricalSummaries).length > 0) {
    console.log('\n\n  CATEGORICAL COLUMN SUMMARIES\n' + subDivider);
    for (const [col, counts] of Object.entries(analytics.categoricalSummaries)) {
      console.log(`\n  Column: ${col}`);
      const sorted = Object.entries(counts).sort((a, b) => b[1] - a[1]);
      sorted.forEach(([val, count]) => {
        const pct = ((count / analytics.totalRows) * 100).toFixed(1);
        console.log(`    ${val.padEnd(25)} : ${count} (${pct}%)`);
      });
    }
  }

  console.log('\n' + divider + '\n');
}

// ─── Module: CSV Report Exporter ──────────────────────────────────────────────

function exportSummaryReport(analytics, outputPath) {
  const rows = [];

  // Header
  rows.push(['Section', 'Column', 'Metric', 'Value']);

  // General info
  rows.push(['General', 'All', 'Total Rows', analytics.totalRows]);
  rows.push(['General', 'All', 'Total Columns', analytics.columns.length]);
  rows.push(['General', 'All', 'Columns', analytics.columns.join(' | ')]);

  // Numeric summaries
  for (const [col, stats] of Object.entries(analytics.numericSummaries)) {
    rows.push(['Numeric', col, 'Count', stats.count]);
    rows.push(['Numeric', col, 'Sum', stats.sum]);
    rows.push(['Numeric', col, 'Mean', stats.mean]);
    rows.push(['Numeric', col, 'Median', stats.median]);
    rows.push(['Numeric', col, 'Min', stats.min]);
    rows.push(['Numeric', col, 'Max', stats.max]);
    rows.push(['Numeric', col, 'Std Dev', stats.stdDev]);
  }

  // Categorical summaries
  for (const [col, counts] of Object.entries(analytics.categoricalSummaries)) {
    for (const [val, count] of Object.entries(counts)) {
      const pct = ((count / analytics.totalRows) * 100).toFixed(1);
      rows.push(['Categorical', col, val, `${count} (${pct}%)`]);
    }
  }

  const csvContent = rows.map(row =>
    row.map(cell => `"${String(cell).replace(/"/g, '""')}"`).join(',')
  ).join('\n');

  fs.writeFileSync(outputPath, csvContent, 'utf-8');
  console.log(`  ✔ Summary report exported to: ${outputPath}\n`);
}

// ─── Main Program ─────────────────────────────────────────────────────────────

function main() {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  console.log('\n╔══════════════════════════════════════════╗');
  console.log('║     CSV ANALYTICS REPORT GENERATOR      ║');
  console.log('╚══════════════════════════════════════════╝\n');

  function askFilePath() {
    rl.question('Enter dataset file path: ', function (inputPath) {
      const filePath = inputPath.trim();

      // Validate
      const validation = validateFilePath(filePath);
      if (!validation.valid) {
        console.log(`  ✖ Error: ${validation.error}`);
        console.log('  Please try again.\n');
        askFilePath(); // loop
        return;
      }

      console.log('  ✔ File found and readable. Processing...\n');

      try {
        // Parse CSV
        const { headers, records } = parseCSV(filePath);
        console.log(`  Loaded ${records.length} records with ${headers.length} columns.`);

        // Compute analytics
        const analytics = computeAnalytics(headers, records);

        // Display formatted results
        displayResults(analytics, filePath);

        // Export summary report
        const outputDir = path.dirname(filePath);
        const outputPath = path.join(outputDir, 'summary_report.csv');
        exportSummaryReport(analytics, outputPath);

      } catch (err) {
        console.log(`  ✖ Failed to process file: ${err.message}`);
      }

      rl.close();
    });
  }

  askFilePath();
}

main();
