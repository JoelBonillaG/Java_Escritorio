<?php

use App\Http\Controllers\StudentController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

// Route::get('/user', function (Request $request) {
//     return $request->user();
// })->middleware('auth:sanctum');

Route::get("/student", [StudentController::class, "index"]);


Route::get("/student/getByCardID/{cardID}", [StudentController::class, "getStudentByCardID"]);


Route::post("/student", [StudentController::class, "store"]);

Route::get("/student/{student}", [StudentController::class, "show"]);

Route::put("/student/{cardID}", [StudentController::class, "update"]);

Route::delete("/student/{cardID}", [StudentController::class, "destroy"]);