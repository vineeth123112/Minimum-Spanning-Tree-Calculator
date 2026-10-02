async function runAlgorithm() {
    const input = document.getElementById("testNumber");

    const testNumber = input.value;

    const response = await fetch("/api/subway", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: testNumber
    });

    const result = await response.text();

    if (Number(result) ==-1){
        document.getElementById("output").textContent = "Impossible";
    } else {
        document.getElementById("output").textContent = result;
    }
}