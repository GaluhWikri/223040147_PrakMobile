fun main() {
    var nomor = 1
    while (true) {
        print("Nilai ke-$nomor: ")
        val input = readLine()?.trim()

        // Menangani input kosong atau nilai yang tidak valid
        val nilai = input?.toIntOrNull()
        if (nilai == null) {
            println("Nilai harus diisi dan berupa angka.")
        } else {
            println("$nomor $input ${IndeksNilaiMatkul(nilai)}")
        }
        nomor++
    }
}


// Fungsi untuk menentukan indeks berdasarkan nilai
fun IndeksNilaiMatkul(nilai: Int?): String {
    return when {
        nilai == null -> "Nilai harus diisi"
        nilai in 80..100 -> "A"
        nilai in 70..79 -> "AB"
        nilai in 60..69 -> "B"
        nilai in 50..59 -> "BC"
        nilai in 40..49 -> "C"
        nilai in 30..39 -> "D"
        nilai in 0..29 -> "E"
        else -> "Nilai di luar jangkauan"
    }
}
