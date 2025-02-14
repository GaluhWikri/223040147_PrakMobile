//fun main() {
//    val umur = 25
//
//    if (umur >= 18) {
//        println("Anda sudah dewasa")
//    } else {
//        println("Anda belum dewasa")
//    }
//}


fun main() {
    val umur = 25

    when (umur) {
        in 0..17 -> println("Anda masih anak-anak")
        else -> println("Anda sudah dewasa")
    }
}