/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataMain;

/**
 *
 * @author ASUS
 */
public class Student {
    

    
    private String cardID;

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }




    private String name;
    
    private String lastName;

    public Student(String cardID, String name, String lastName) {
        this.cardID = cardID;
        this.name = name;
        this.lastName = lastName;
    }

    
    
    public Student(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    
}
