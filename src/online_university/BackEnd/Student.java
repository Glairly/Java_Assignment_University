/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_university.BackEnd;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author USER
 */
public class Student extends Person {

    final String role = "Student";
    private String Mark = "X";
    private String Score = "";
    protected ArrayList<Course> course = new ArrayList<Course>();

    public Student() {
        super();
    }

    public Student(String username, String password) {
        super(username, password);
    }

    public Student(String name, String lname, String age, String stuId, String id, String password, String email) {
        super(name, lname, age, stuId, id, password, email);
    }

    public String getMark() {
        return Mark;
    }

    public ArrayList<Course> getCourse() {
        return course;
    }

    public void setMark(String Mark) {
        this.Mark = Mark;
    }

    public void setCourses(ArrayList<Course> course) {
        this.course = course;
    }

    public String getRole() {
        return role;
    }

    public void addCourse(Course course) {
        this.course.add(course);
    }

    public void updateCourse() {
        ArrayList<Course> allC = API.getAllCourse();
        ArrayList removeC = new ArrayList();
        for (Course c : this.course) {
            boolean isExist = false;
            for (Course cc : allC) {
                if (c.toString().equals(cc.toString())) {
                    for (Pair<Student, Grading> s : cc.getStudents()) {
                        if (s.getKey().toString().equals(this.toString())) {
                            isExist = true;
                            c.setStudents(cc.getStudents());
                            break;
                        }
                    }
                }
            }
            if (!isExist) {
                removeC.add(c);
            }
        }
        for (var c : removeC) {
            this.course.remove(c);
        }
    }

    public void removeCourse(Course course) {
        for (Course c : this.course) {
            if (c.toString().equals(course.toString())) {
                this.course.remove(c);
                return;
            }
        }
    }

    public void setMarkByCourse(Course course) {
        for (Course c : this.course) {
            if (c.toString().equals(course.toString())) {
                var st = c.getStudent(this);
                this.setMark(st.getValue().getGrade());
                return;
            }
        }
    }

    public static Student getById(String id) {
        return (Student) Person.getById(id);
    }

    public static int getIndex(String id) {
        return Person.getIndex(id, new Student(), new Database("students"));
    }

    @Override
    public String toString() {
        String s = super.toString();
        return "Student" + s; //To change body of generated methods, choose Tools | Templates.
    }

}
