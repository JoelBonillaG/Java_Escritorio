/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataMain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class Main {
    public static void main(String[] args) throws Exception {
     
        HttpClient client = HttpClient.newHttpClient();
        
        String id = "1";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8000/api/student/"+id))
                .header("Accept","application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        String json = response.body();
        
        Gson gson = new Gson();
        
        Student student = gson.fromJson(json, Student.class);
        
        System.out.println(student.getName());
        
       // Type listType = new TypeToken<List<Student>>(){}.getType();
        
        
       
        
    }
}
