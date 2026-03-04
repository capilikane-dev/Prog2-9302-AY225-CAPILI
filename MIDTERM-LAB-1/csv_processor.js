const fs = require('fs');
const path = require('path');

// CSV file name (must be in the same folder as this JS file)
const csvFileName = 'vgchartz-2024.csv';

// Full path of the CSV
const csvFilePath = path.join(__dirname, csvFileName);

// Check if CSV exists
if (!fs.existsSync(csvFilePath)) {
    console.log(`Error: ${csvFileName} not found in this folder.`);
    process.exit(1);
}

// Read CSV
let data;
try {
    data = fs.readFileSync(csvFilePath, 'utf8');
} catch (err) {
    console.log('Error reading file:', err.message);
    process.exit(1);
}

// Parse CSV into records
const lines = data.split(/\r?\n/);
const records = [];

for (let i = 1; i < lines.length; i++) {
    const line = lines[i].trim();
    if (!line) continue;

    const parts = line.split(',');
    if (parts.length < 3) continue;

    const quantity = parseInt(parts[1].trim());
    const price = parseFloat(parts[2].trim());
    if (isNaN(quantity) || isNaN(price)) continue;

    records.push({ quantity, price });
}

if (records.length === 0) {
    console.log('No valid data found in CSV.');
    process.exit(1);
}

// Generate summary
let totalItems = 0;
let totalRevenue = 0;
records.forEach(r => {
    totalItems += r.quantity;
    totalRevenue += r.quantity * r.price;
});

// Print summary
console.log('\nCSV Analytics Summary:');
console.log('-------------------------');
console.log('Total Items Sold:', totalItems);
console.log('Total Revenue: $' + totalRevenue.toFixed(2));

// Export summary CSV
const summaryCSV = `Metric,Value
Total Items Sold,${totalItems}
Total Revenue,${totalRevenue.toFixed(2)}
`;

try {
    fs.writeFileSync('summary_report.csv', summaryCSV);
    console.log('\nSummary exported to summary_report.csv');
} catch (err) {
    console.log('Error writing summary CSV:', err.message);
}