<?php

namespace App\Http\Controllers;

use App\Models\Student;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class StudentController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $students = Student::all();

        return response()->json($students);

    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $student = Student::create($request->all());

        return response()->json([
            'message' => 'Estudiante creado correctamente',
        ], 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $student = Student::find($id);

        return response()->json($student);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $cardID)
    {
        Log::info('Datos recibidos para actualización:', $request->all());
        $student = Student::where("cardID", $cardID)->first();

        $student->update($request->all());
        if (!$student) {
            return response()->json([
                'message' => 'Estudiante no encontrado',
            ], 404);
        }
        return response()->json([
            'message' => 'Estudiante actualizado correctamente',
        ], 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($cardID)
    {

        $student = Student::where("cardID", $cardID)->first();
        $student->delete();
        return response()->json("Estudiante eliminado correctamente", 200);
    }

    public function getStudentByCardID($cardID)
    {

        $students = Student::where("cardID", "like", "%$cardID%")->get();

        return response()->json($students);
    }


}