/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_university;

import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class Online_University {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Database db = new Database();
        db.setPath_Staffs();
        ArrayList<Staff> s = (ArrayList<Staff>) db.get();
        Staff kanat = Staff.getById("Kanat_Calculus");
        System.out.println(kanat.getCourses());
        

    }

}
