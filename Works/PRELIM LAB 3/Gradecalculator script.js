function computeGrade(){

    let attendance = parseFloat(document.getElementById("attendance").value);
    let lab1 = parseFloat(document.getElementById("lab1").value);
    let lab2 = parseFloat(document.getElementById("lab2").value);
    let lab3 = parseFloat(document.getElementById("lab3").value);

    // VALIDATION
    if (
        isNaN(attendance) || isNaN(lab1) || isNaN(lab2) || isNaN(lab3) ||
        attendance < 0 || attendance > 100 ||
        lab1 < 0 || lab1 > 100 ||
        lab2 < 0 || lab2 > 100 ||
        lab3 < 0 || lab3 > 100
    ) {
        alert("Please enter valid grades between 0 and 100.");
        return;
    }

    // COMPUTATIONS
    let labAverage = (lab1 + lab2 + lab3) / 3;
    let classStanding = (attendance * 0.40) + (labAverage * 0.60);

    let requiredPass = (75 - (classStanding * 0.70)) / 0.30;
    let requiredExcellent = (100 - (classStanding * 0.70)) / 0.30;

    let remark, remarkClass;

    if (requiredPass <= 0) {
        remark = "🎉 Congratulations! You have already PASSED the Prelim period.";
        remarkClass = "pass";
    } 
    else if (requiredPass > 100) {
        remark = "😢 Sorry, passing the Prelim period is no longer possible.";
        remarkClass = "fail";
    } 
    else {
        remark = "📘 You still need to do well in the Prelim Exam to pass.";
        remarkClass = "info";
    }

    document.getElementById("result").style.display = "block";
    document.getElementById("output").innerHTML = `
        <b>Lab Work Average:</b> ${labAverage.toFixed(2)}<br>
        <b>Class Standing:</b> ${classStanding.toFixed(2)}<br><br>

        <b>Required Prelim Exam (Passing - 75):</b> ${requiredPass.toFixed(2)}<br>
        <b>Required Prelim Exam (Excellent - 100):</b> ${requiredExcellent.toFixed(2)}<br><br>

        <span class="${remarkClass}">${remark}</span>
    `;
}
