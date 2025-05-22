/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataMain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class StudentApi {

    private final String baseURL = "http://localhost:8000/api/student";

    private Gson gson = new Gson();

    //Get all students
    public ArrayList<Student> getStudents() {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseURL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = (String) response.body();

            Gson gson = new Gson();

            Type listType = new TypeToken<List<Student>>() {
            }.getType();

            ArrayList<Student> students = gson.fromJson(json, listType);

            return students;

        } catch (Exception e) {

            return null;
        }

    }
    //POST PUT DELETE

    public String insertStudent(Student student) {

        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = this.gson.toJson(student);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(this.baseURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {

                return "Estudiante ingresado con exito";
            } else {

                return "No se pudo insertar al estudiante";
            }

        } catch (Exception e) {

            return "Ha sucedido el siguiente error:" + e.getMessage();
        }

    }

    public String updateStudent(Student student) {

        try {
            String newUrl = this.baseURL + "/" + student.getCardID();

            String json = this.gson.toJson(student);

            HttpClient client = HttpClient.newHttpClient();

                   System.out.println("Actualizando estudiante:");
        System.out.println("Cédula: " + student.getCardID());
        System.out.println("Nombre: " + student.getName());
        System.out.println("Apellido: " + student.getLastName());
           
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(newUrl))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                return "Estudiante actualizado";
            } else {

                return "No se pudo actualizar al estudiante";
            }

        } catch (Exception e) {
            return "Ha sucedido el siguiente error:" + e.getMessage();
        }

    }

    public String deleteStudent(String cardID) {

        try {
            String newUrl = this.baseURL + "/" + cardID;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(newUrl))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                return "Estudiante eliminado";
            } else {

                return "No se pudo eliminar al estudiante";
            }

        } catch (Exception e) {

            return "Ha sucedido el siguiente error:" + e.getMessage();
        }

    }
    
    public ArrayList<Student> getStudentsByCardID(String cardID) throws  Exception{
    
        String newUrl=this.baseURL+"/getByCardID/"+cardID;
        
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(newUrl))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        
        String json = response.body();
        
        Gson gson = new Gson();
        
        Type listType = new TypeToken<List<Student>>(){}.getType();
        
        ArrayList<Student> students = gson.fromJson(json, listType);
        
        return students;
        
    
    }

}
