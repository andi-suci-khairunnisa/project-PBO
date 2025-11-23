// ======== Data Dummy Awal (bisa diganti dari backend nanti) ========
let pekerjaanDilamar = 3;
let pekerjaanAktif = 2;
let rating = 4.8;

// Array untuk menampung daftar pekerjaan terbaru
let daftarPekerjaan = [
    { nama: "Tukang Bangunan", lokasi: "Jakarta", jarak: "2 km", upah: "Rp 80.000 / hari" },
    { nama: "Asisten Rumah Tangga", lokasi: "Depok", jarak: "3 km", upah: "Rp 90.000 / hari" }
];

// ======== Fungsi Render ke Halaman ========
function renderDashboard() {
    // Update statistik
    document.getElementById("pekerjaanDilamar").innerText = pekerjaanDilamar;
    document.getElementById("pekerjaanAktif").innerText = pekerjaanAktif;
    document.getElementById("ratingUser").innerText = rating.toFixed(1);

    // Tampilkan pekerjaan terbaru
    const jobContainer = document.querySelector(".latest-jobs .job-list");
    jobContainer.innerHTML = ""; // kosongkan dulu

    daftarPekerjaan.forEach(job => {
        const card = document.createElement("div");
        card.classList.add("job-card");
        card.innerHTML = `
            <h4>${job.nama}</h4>
            <p>${job.lokasi}, ${job.jarak}</p>
            <p>${job.upah}</p>
            <button>Lihat Detail</button>
        `;
        jobContainer.appendChild(card);
    });
}

// ======== Fungsi Simulasi Penambahan Data ========
function tambahPekerjaan(nama, lokasi, jarak, upah) {
    daftarPekerjaan.push({ nama, lokasi, jarak, upah });
    renderDashboard();
}

function lamarPekerjaan() {
    pekerjaanDilamar++;
    renderDashboard();
}

function ubahRating(nilaiBaru) {
    rating = nilaiBaru;
    renderDashboard();
}

function openLogoutPopup() {
    document.getElementById("logoutPopup").style.display = "flex";
}
function closeLogoutPopup() {
    document.getElementById("logoutPopup").style.display = "none";
}
function confirmLogout() {
    window.location.href = "/logout"; // arahkan ke endpoint logout
}

// ======== Jalankan Pertama Kali ========
document.addEventListener("DOMContentLoaded", renderDashboard);

