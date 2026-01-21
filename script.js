// ===============================
// HARD-CODED LOGIN CREDENTIALS
// ===============================
const VALID_USERNAME = "justin kane";
const VALID_PASSWORD = "2006";

// ===============================
// ELEMENT REFERENCES (CACHE DOM)
// ===============================
const form = document.getElementById("loginForm");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const message = document.getElementById("message");
const timestampDiv = document.getElementById("timestamp");

// ===============================
// FAILED ATTEMPT COUNTER
// ===============================
let failedAttempts = 0;

// ===============================
// ATTENDANCE STORAGE
// ===============================
let attendanceRecords = [];

// ===============================
// REUSED AUDIO CONTEXT (FASTER)
// ===============================
const AudioContext = window.AudioContext || window.webkitAudioContext;
const audioCtx = new AudioContext();

// ===============================
// FAST BEEP FUNCTION
// ===============================
function beep(times = 1) {
    let currentTime = audioCtx.currentTime;

    for (let i = 0; i < times; i++) {
        const oscillator = audioCtx.createOscillator();
        const gainNode = audioCtx.createGain();

        oscillator.type = "square";
        oscillator.frequency.value = 880;

        gainNode.gain.setValueAtTime(0.15, currentTime);

        oscillator.connect(gainNode);
        gainNode.connect(audioCtx.destination);

        oscillator.start(currentTime);
        oscillator.stop(currentTime + 0.12);

        currentTime += 0.16;
    }
}

// ===============================
// LOGIN HANDLER
// ===============================
form.addEventListener("submit", function (e) {
    e.preventDefault();

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (username === VALID_USERNAME && password === VALID_PASSWORD) {
        failedAttempts = 0;

        const now = new Date();
        const timestamp = formatDate(now);

        message.style.color = "green";
        message.textContent = "Login successful! Welcome, " + username;
        timestampDiv.textContent = "Login Time: " + timestamp;

        attendanceRecords.push({
            user: username,
            time: timestamp
        });

        generateAttendanceFile();

    } else {
        failedAttempts++;

        message.style.color = "red";
        message.textContent = "Incorrect username or password!";
        timestampDiv.textContent = "";

        // Max 5 beeps
        beep(Math.min(failedAttempts, 5));
    }
});

// ===============================
// DATE FORMATTER (FAST)
// ===============================
function formatDate(date) {
    return date.toLocaleString();
}

// ===============================
// ATTENDANCE FILE GENERATOR
// ===============================
function generateAttendanceFile() {
    let content = "ATTENDANCE SUMMARY\n\n";

    for (let i = 0; i < attendanceRecords.length; i++) {
        content += `Record ${i + 1}\n`;
        content += `Username: ${attendanceRecords[i].user}\n`;
        content += `Timestamp: ${attendanceRecords[i].time}\n\n`;
    }

    const blob = new Blob([content], { type: "text/plain" });
    const link = document.createElement("a");

    link.href = URL.createObjectURL(blob);
    link.download = "attendance_summary.txt";
    link.click();
}
