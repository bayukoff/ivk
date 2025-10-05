const form = document.getElementById('uploadForm');
const fileInput = document.getElementById('fileInput');
const statusDiv = document.getElementById('status');
const linkContainer = document.getElementById('linkContainer');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    statusDiv.textContent = "Uploading...";
    linkContainer.innerHTML = "";

    const file = fileInput.files[0];
    if (!file) {
        statusDiv.textContent = "Please select a file";
        return;
    }

    const formData = new FormData();
    formData.append('file', file);
    try {
        const response = await fetch('/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error("Upload failed");

        const data = await response.json();
        statusDiv.textContent = "Upload successful!";

        const link = document.createElement('a');
        link.href = data.link;
        link.textContent = "Download your file";
        link.target = "_blank";
        linkContainer.appendChild(link);

    } catch (err) {
        statusDiv.textContent = "Error: " + err.message;
    }
});
