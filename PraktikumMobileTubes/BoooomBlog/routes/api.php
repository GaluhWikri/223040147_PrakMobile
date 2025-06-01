<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ArticleController;
use App\Http\Controllers\CommentController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\UserController; // <-- Pastikan import ini ada

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// Rute yang tidak memerlukan autentikasi
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);

// Rute untuk Artikel (contoh, bisa diproteksi atau tidak tergantung kebutuhan)
// Jika ingin semua operasi artikel (index, show, store, update, delete) diproteksi,
// pindahkan Route::apiResource('articles', ArticleController::class); ke dalam grup middleware di bawah.
// Untuk saat ini, kita asumsikan GET (index, show) artikel bisa publik,
// sedangkan POST, PUT, DELETE artikel memerlukan autentikasi (ini harus diatur di controller atau rute spesifik).
Route::get('/articles', [ArticleController::class, 'index']);
Route::get('/articles/{article}', [ArticleController::class, 'show']);


// Rute yang memerlukan otentikasi (Dilindungi Sanctum)
Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthController::class, 'logout']);

    // Endpoint untuk mendapatkan data user yang sedang login
    Route::get('/user', function (Request $request) {
        return $request->user();
    });

    // Endpoint untuk User (DIPROTEKSI)
    Route::get('/users', [UserController::class, 'index'])->name('users.index');       // Mendapatkan semua user
    Route::get('/users/{user}', [UserController::class, 'show'])->name('users.show'); // Mendapatkan user spesifik by ID

    // Endpoint untuk membuat, mengupdate, menghapus artikel (DIPROTEKSI)
    Route::post('/articles', [ArticleController::class, 'store'])->name('articles.store');
    Route::put('/articles/{article}', [ArticleController::class, 'update'])->name('articles.update');
    Route::patch('/articles/{article}', [ArticleController::class, 'update']); // Seringkali PUT dan PATCH mengarah ke metode yang sama
    Route::delete('/articles/{article}', [ArticleController::class, 'destroy'])->name('articles.destroy');

    // Endpoint untuk Komentar (DIPROTEKSI - contoh)
    // Anda mungkin ingin mengizinkan GET komentar secara publik,
    // tetapi POST, PUT, DELETE komentar memerlukan autentikasi.
    // Route::apiResource('comments', CommentController::class); // Jika semua operasi komentar diproteksi
    // Atau lebih spesifik:
    // Route::post('/articles/{article}/comments', [CommentController::class, 'store']);
    // Route::put('/comments/{comment}', [CommentController::class, 'update']);
    // Route::delete('/comments/{comment}', [CommentController::class, 'destroy']);
});


// Hapus atau komentari rute /users lama yang tidak terproteksi jika ada dan tidak digunakan lagi:
/*
Route::get('/users', function() {
    // Sebaiknya ini juga diproteksi atau hanya untuk admin
    return \App\Models\User::all();
});
*/
