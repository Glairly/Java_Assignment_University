/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_university.BackEnd;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public class Grading implements Serializable {

    private String Grade = "X", Other_score = "0", MidExam = "0", FinalExam = "0";
    private String Attending_Count = "0";

    public Grading() {
    }

    public void Attending() {
        int count = Integer.parseInt(Attending_Count);
        count++;
        this.setAttending_Count("" + count);
        System.out.println(this.getAttending_Count());
    }

    public String getAttending_Count() {
        return Attending_Count;
    }

    public void setAttending_Count(String Attending_Count) {
        this.Attending_Count = Attending_Count;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
        calculateGrade();
    }

    public String getOther_score() {
        return Other_score;
    }

    public void setOther_score(String Other_score) {
        this.Other_score = Other_score;
        calculateGrade();
    }

    public String getMidExam() {
        return MidExam;
    }

    public void setMidExam(String MidExam) {
        this.MidExam = MidExam;
        calculateGrade();
    }

    public String getFinalExam() {
        return FinalExam;
    }

    public void setFinalExam(String FinalExam) {
        this.FinalExam = FinalExam;
        calculateGrade();
    }

    public void calculateGrade() {
        return ;
//        int total = 0;
//        total += Integer.parseInt(this.FinalExam) + Integer.parseInt(this.MidExam) + Integer.parseInt(this.Other_score);
//        System.out.println(total);
//        if (total >= 80) {
//            this.setGrade("A");
//        } else if (total >= 65) {
//            this.setGrade("B");
//        } else if (total >= 30) {
//            this.setGrade("C");
//        } else {
//            this.setGrade("X");
//        }
    }

    @Override
    public String toString() {
        return "Grading{Score : " + this.Other_score + " Grade : " + this.Grade + "}"; //To change body of generated methods, choose Tools | Templates.
    }

    public String getScore() {
        return Other_score;
    }

    public void setScore(String Score) {
        this.Other_score = Score;
    }
}
