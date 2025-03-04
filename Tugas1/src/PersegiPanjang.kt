import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    print("Masukkan panjang: ")
    val panjang = scanner.nextDouble()
    print("Masukkan lebar: ")
    val lebar = scanner.nextDouble()

    val persegiPanjang = PersegiPanjang(panjang, lebar)
    println("\nHasil Perhitungan Persegi Panjang:")
    println("Panjang: $panjang")
    println("Lebar: $lebar")
    println("Luas: ${persegiPanjang.hitungLuas()}")
    println("Keliling: ${persegiPanjang.hitungKeliling()}")
}

class PersegiPanjang(private val panjang: Double, private val lebar: Double) {
    fun hitungLuas(): Double {
        return panjang * lebar
    }

    fun hitungKeliling(): Double {
        return 2 * (panjang + lebar)
    }
}


