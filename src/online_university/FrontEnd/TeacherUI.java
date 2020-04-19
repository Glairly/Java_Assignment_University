package online_university.FrontEnd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
import com.sun.javafx.tk.FontLoader;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.Toolkit;
import javafx.scene.shape.Rectangle;
import java.lang.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import online_university.BackEnd.*;

public class TeacherUI extends Application {

    String userName, passWord;
    Staff User;
    Course nowCourse;
    Timer session_Timer = new Timer();
    coresTimerTask update_Attend = new coresTimerTask() {
        @Override
        public void run() {
            this.setRun(true);
            Session local_ss = null;
            var allSS = API.getAllSession();
            Session ss = nowCourse.getLastestSession();
            if (ss != null) {
                for (Session SS : allSS) {
                    if (SS.toString().equals(ss.toString())) {
                        local_ss = SS;
                    }
                }
            }
            if (local_ss != null) {
                int i = 0;
                if (local_ss.getAttended_Students() != null) {
                    table3_Data2.clear();
                    for (var ii : local_ss.getAttended_Students()) {
                        table3_Data2.add(new Persons("" + (i + 1), ii.getFirstName() + " " + ii.getLastName(), ii.getStudentId(),
                                "0", "0", "0", "X", nowCourse.getStudent(ii).getValue().getAttending_Count()));
                        i++;
                    }
                    System.out.println(local_ss.getAttended_Students());
                } else {
                    System.out.println("No student come yet");
                }
            } else {
                System.out.println("Session has ended");
                session_Timer.cancel();
                session_Timer.purge();
            }
            update();
        }
    };
    Timer update_Student_Timer = new Timer();

    coresTimerTask update_Student = new coresTimerTask() {
        @Override
        public void run() {
            this.setRun(true);
            update_Student_Function();
        }
    };

    void update_Student_Function() {
        table1_Data.clear();
        if (nowCourse.getStudents() != null) {
            for (int i = 0; i < nowCourse.getStudents().size(); i++) {
                var c = nowCourse.getStudents().get(i).getKey();
                var c_grading = nowCourse.getStudents().get(i).getValue();
                table1_Data.add(new Persons("" + (i + 1), c.getFirstName() + " " + c.getLastName(), c.getStudentId(),
                        c_grading.getScore(), c_grading.getMidExam(), c_grading.getFinalExam(), c_grading.getGrade(), c_grading.getAttending_Count()));
            }
        }
    }

    void initData() {
        this.User.updateCourse();
        this.StudentArr = API.getAllStudent();
        CourseArr = this.User.getCourses();
    }

    ArrayList<Course> CourseArr = new ArrayList();
    ArrayList<Student> StudentArr = new ArrayList();

    AnchorPane SubPane = new AnchorPane();
    AnchorPane SesPane = new AnchorPane();
    AnchorPane AddPane = new AnchorPane();
    AnchorPane AsPane = new AnchorPane();
    AnchorPane ViewPane = new AnchorPane();

    Scene SubScene = new Scene(SubPane, 1280, 720);
    Scene SesScene = new Scene(SesPane, 1280, 720);
    Scene AddScene = new Scene(AddPane, 700, 400);
    Scene AsScene = new Scene(AsPane, 700, 400);
    Scene ViewScene = new Scene(ViewPane, 1280, 720);

    Button AddStu = new Button("Add Students");
    Button Logout = new Button("Logout");
    Button DelStu = new Button("Delete Students");
    Button BckBtnSes = new Button("Back to Student List");
    String ss;

    Button Bctsub = new Button("Back to Select Courses");
    Button Endses = new Button("End Sessions");
    Button Listses = new Button("List Students");
    Button Attses = new Button("Attended Students");

    VBox SubjectName = new VBox();
    VBox AddStudentsBtn = new VBox();
    VBox DeleteStudentsDel = new VBox();
    VBox vBoxButtons3 = new VBox();

    //Checkbox[] cb;
    ScrollPane SubScrollPane = new ScrollPane(SubjectName);
    ScrollPane AddCoScrollPane = new ScrollPane();
    ScrollPane AddScrollPane = new ScrollPane(AddStudentsBtn);
    ScrollPane DelScrollPane = new ScrollPane(DeleteStudentsDel);
    ScrollPane ViewScrollPane = new ScrollPane();
    ScrollPane SesListScrollPane = new ScrollPane();
    String ts, tf, tl, ta, ti, te;
    String sn;
    Label lbl4 = new Label("Username : ");
    Label lbl6 = new Label("Firstname : ");
    Label lbl7 = new Label("Lastname : ");
    Label lbl8 = new Label("Age : ");
    Label lbl9 = new Label("ID : ");
    Label lbl10 = new Label("Email : ");
    Label lbl5 = new Label("Select Courses");
    Label lbl = new Label("Add Students");
    Label lbl2 = new Label("Add Courses");
    Label lbl3 = new Label("Students List");
    Label Ong = new Label("On-Going Session");

    ArrayList<Button> Sub = new ArrayList<>();

    Button StartSes = new Button("Start Session");
    Button ConSes = new Button("Continue Session");
    Button anotSes = new Button("Not avaliable");
    final ObservableList<Persons> table1_Data = FXCollections.observableArrayList();
    final ObservableList<Persons> table2_Data = FXCollections.observableArrayList();
    final ObservableList<Persons> table3_Data = FXCollections.observableArrayList();
    final ObservableList<Persons> table3_Data2 = FXCollections.observableArrayList();
    final ObservableList<Persons> table5_Data = FXCollections.observableArrayList();

    TableView<Persons> table = new TableView<Persons>();
    TableView<Persons> table2 = new TableView<Persons>();
    TableView<Persons> table3 = new TableView<Persons>();
    TableView<Persons> table4 = new TableView<Persons>();
    TableView<Persons> table5 = new TableView<Persons>();

    public static void main(String[] args) throws FileNotFoundException {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        stage.setOnCloseRequest(eh -> {
            session_Timer.cancel();
            session_Timer.purge();
            update_Student_Timer.cancel();
            update_Student_Timer.purge();
        });
        SubPane.getStylesheets().add("css/newCascadeStyleSheet.css");
        ViewPane.getStylesheets().add("css/newCascadeStyleSheet.css");
        AddPane.getStylesheets().add("css/newCascadeStyleSheet.css");
        SesPane.getStylesheets().add("css/newCascadeStyleSheet.css");
        AsPane.getStylesheets().add("css/newCascadeStyleSheet.css");
        SubjectName.setSpacing(100.0);
        this.User = (Staff) Authority.login("Staff1", "12345");
        initData();

        ts = this.User.getUserName();
        tf = this.User.getFirstName();
        tl = this.User.getLastName();
        ta = this.User.getAge();
        ti = this.User.getStudentId();
        te = this.User.getEmail();

        Label Tusername = new Label(ts);
        Label Tfirstname = new Label(tf);
        Label Tlastname = new Label(tl);
        Label Tage = new Label(ta);
        Label Tid = new Label(ti);
        Label Temail = new Label(te);
        //Label Tusername2 = new Label(sn + " By "+ts);
        this.setUI();
        Sub = new ArrayList<>();
        if (this.CourseArr != null) {
            for (int i = 0; i < this.CourseArr.size(); i++) {
                Sub.add(new Button(CourseArr.get(i).getFirstName()));
            }
        }
        if (this.CourseArr != null) {
            for (int i = 0; i < this.CourseArr.size(); i++) {
                SubjectName.getChildren().add(Sub.get(i));
                Sub.get(i).setPrefSize(460, 50);
                SubjectName.getStyleClass().add("special-label");

            }
        }
        SubjectName.setStyle("-fx-font-size: 20pt;");

        Tusername.setScaleX(3);
        Tusername.setScaleY(3);
        Tusername.setScaleZ(3);
        Tfirstname.setScaleX(3);
        Tfirstname.setScaleY(3);
        Tfirstname.setScaleZ(3);
        Tlastname.setScaleX(3);
        Tlastname.setScaleY(3);
        Tlastname.setScaleZ(3);
        Tage.setScaleX(3);
        Tage.setScaleY(3);
        Tage.setScaleZ(3);
        Tid.setScaleX(3);
        Tid.setScaleY(3);
        Tid.setScaleZ(3);
        Temail.setScaleX(3);
        Temail.setScaleY(3);
        Temail.setScaleZ(3);

        lbl4.setScaleX(3);
        lbl4.setScaleY(3);
        lbl4.setScaleZ(3);
        lbl5.setScaleX(5);
        lbl5.setScaleY(5);
        lbl5.setScaleZ(5);
        lbl6.setScaleX(3);
        lbl6.setScaleY(3);
        lbl6.setScaleZ(3);
        lbl7.setScaleX(3);
        lbl7.setScaleY(3);
        lbl7.setScaleZ(3);
        lbl8.setScaleX(3);
        lbl8.setScaleY(3);
        lbl8.setScaleZ(3);
        lbl9.setScaleX(3);
        lbl9.setScaleY(3);
        lbl9.setScaleZ(3);
        lbl10.setScaleX(3);
        lbl10.setScaleY(3);
        lbl10.setScaleZ(3);
        Ong.setScaleX(4);
        Ong.setScaleY(4);
        Ong.setScaleZ(4);

        lbl3.getStyleClass().add("special-label");
        AnchorPane.setLeftAnchor(lbl4, 80d);
        AnchorPane.setTopAnchor(lbl4, 10d);
        AnchorPane.setLeftAnchor(lbl6, 83d);
        AnchorPane.setTopAnchor(lbl6, 50d);
        AnchorPane.setLeftAnchor(lbl7, 87d);
        AnchorPane.setTopAnchor(lbl7, 90d);
        AnchorPane.setLeftAnchor(lbl8, 145d);
        AnchorPane.setTopAnchor(lbl8, 130d);
        AnchorPane.setLeftAnchor(lbl9, 163d);
        AnchorPane.setTopAnchor(lbl9, 170d);
        AnchorPane.setLeftAnchor(lbl10, 130d);
        AnchorPane.setTopAnchor(lbl10, 210d);
        AnchorPane.setRightAnchor(lbl5, 590d);
        AnchorPane.setTopAnchor(lbl5, 50d);

        AnchorPane.setRightAnchor(Ong, 180d);
        AnchorPane.setTopAnchor(Ong, 50d);

        AnchorPane.setLeftAnchor(Tusername, 250d);
        AnchorPane.setTopAnchor(Tusername, 10d);
        AnchorPane.setLeftAnchor(Tfirstname, 250d);
        AnchorPane.setTopAnchor(Tfirstname, 50d);
        AnchorPane.setLeftAnchor(Tlastname, 250d);
        AnchorPane.setTopAnchor(Tlastname, 90d);
        AnchorPane.setLeftAnchor(Tage, 250d);
        AnchorPane.setTopAnchor(Tage, 130d);
        AnchorPane.setLeftAnchor(Tid, 250d);
        AnchorPane.setTopAnchor(Tid, 170d);
        AnchorPane.setLeftAnchor(Temail, 250d);
        AnchorPane.setTopAnchor(Temail, 210d);

        SubScrollPane.setPrefSize(500, 400);

        SubjectName.setSpacing(5);
        SubjectName.setAlignment(Pos.CENTER);
        SubjectName.setPadding(new Insets(20, 0, 0, SubScrollPane.getPrefWidth() / 2 - 230));
        SubScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        SubScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        SubScrollPane.setLayoutX((SubPane.getWidth() / 2) - (SubScrollPane.getPrefWidth() / 2));
        SubScrollPane.setLayoutY((SubPane.getHeight() / 2) - (SubScrollPane.getPrefHeight() / 2));
        SubPane.getChildren().add(Tusername);
        SubPane.getChildren().add(Tfirstname);
        SubPane.getChildren().add(Tlastname);
        SubPane.getChildren().add(Tage);
        SubPane.getChildren().add(Tid);
        SubPane.getChildren().add(Temail);

        SubPane.getChildren().add(lbl4);
        SubPane.getChildren().add(lbl6);
        SubPane.getChildren().add(lbl7);
        SubPane.getChildren().add(lbl8);
        SubPane.getChildren().add(lbl9);
        SubPane.getChildren().add(lbl10);
        SubPane.getChildren().add(lbl5);
        //SesPane.getChildren().add(Ong);
        SubScrollPane.setPrefSize(500, 450);
        Button AddSub = new Button("Add Courses");
        SubPane.getChildren().add(AddSub);
        AddSub.setPrefSize(250, 50);
        AddSub.setStyle("-fx-font-size: 20pt;");

        AnchorPane.setRightAnchor(AddSub, 510d);
        AnchorPane.setBottomAnchor(AddSub, 30d);
        Button BckBtn2 = new Button("Back");
        Button OKBt = new Button("Apply");
        //lbl5.getStyleClass().add("white-label");
        lbl5.getStyleClass().add("special-label");
        Ong.getStyleClass().add("going-label");
        Tusername.getStyleClass().add("special-label");
        lbl4.getStyleClass().add("special-label");
        //BckBtn2.setPrefSize(120, 30);
        //BckBtn2.setStyle("-fx-font-size: 15pt;");
        //OKBt.setPrefSize(120, 30);
        //OKBt.setStyle("-fx-font-size: 15pt;");

//        DelScrollPane.setPrefSize(500, 300);
//        DeleteStudentsDel.setPadding(new Insets(0, 0, 0, ((DelScrollPane.getPrefWidth() / 2)) - (75)));
//        DelScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        DelScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        DelScrollPane.setLayoutX((AddPane.getWidth() / 2) - (DelScrollPane.getPrefWidth() / 2));
        DelScrollPane.setLayoutY((AddPane.getHeight() / 2) - (DelScrollPane.getPrefHeight() / 2));
        Stage addsubstage = new Stage();
        AddSub.setOnAction(e -> {
            addsubstage.setScene(AsScene);
            addsubstage.show();
        });
        BckBtn2.setOnAction(e -> addsubstage.close());
//        AnchorPane.setRightAnchor(BckBtn2, 350d);
//        AnchorPane.setBottomAnchor(BckBtn2, 40d);
//        AnchorPane.setRightAnchor(OKBt, 220d);
//        AnchorPane.setBottomAnchor(OKBt, 40d);
        OKBt.setLayoutX(315);
        OKBt.setLayoutY(360);
        BckBtn2.setLayoutX(370);
        BckBtn2.setLayoutY(360);
        lbl2.setScaleX(2.5);
        lbl2.setScaleY(2.5);
        lbl2.setScaleZ(2.5);
        AnchorPane.setRightAnchor(lbl2, 310d);
        AnchorPane.setTopAnchor(lbl2, 10d);
        AsPane.getChildren().add(BckBtn2);
        AsPane.getChildren().add(OKBt);
        AsPane.getChildren().add(lbl2);
        TextField namesub = new TextField();
        namesub.setPrefSize(450, 80);
        AnchorPane.setRightAnchor(namesub, 125d);
        AnchorPane.setTopAnchor(namesub, 150d);
        namesub.setStyle("-fx-font-size: 30pt;");
        //AsPane.getChildren().add(namesub);

        SubPane.getChildren().add(SubScrollPane);
        stage.setScene(SubScene);
        stage.show();

        for (int i = 0; i < CourseArr.size(); i++) {
            Sub.get(i).setOnAction(e -> {
                for (int ii = 0; ii < CourseArr.size(); ii++) {
                    if (Sub.get(ii) != null && Sub.get(ii).toString().equals(e.getSource().toString())) {
                        nowCourse = CourseArr.get(ii);
                        sn = CourseArr.get(ii).getFirstName();
                        Label Subname = new Label(sn);
                        Rectangle rect = new Rectangle(1280, 60);
                        rect.setFill(Color.rgb(63, 81, 181));
                        AnchorPane.setLeftAnchor(rect, 0d);
                        AnchorPane.setTopAnchor(rect, 100d);
                        ViewPane.getChildren().add(rect);
                        ViewPane.getChildren().add(Subname);
                        Subname.setScaleX(3.5);
                        Subname.setScaleY(3.5);
                        Subname.setScaleZ(3.5);
                        Subname.getStyleClass().add("special-label");
                        Subname.setLayoutX(ViewPane.getWidth() / 2 - 20);
                        AnchorPane.setTopAnchor(Subname, 120d);

                        Label Subname2 = new Label(sn);
                        Label Tusername2 = new Label(sn + " By " + ts);
                        Rectangle rect2 = new Rectangle(700, 80);
                        rect2.setFill(Color.rgb(63, 81, 181));
                        AnchorPane.setLeftAnchor(rect2, 0d);
                        AnchorPane.setTopAnchor(rect2, 40d);
                        SesPane.getChildren().add(rect2);
                        //SesPane.getChildren().add(Subname2);
                        Subname2.setScaleX(5);
                        Subname2.setScaleY(5);
                        Subname2.setScaleZ(5);
                        Subname2.getStyleClass().add("special-label");
                        Subname2.setLayoutX(120d);
                        AnchorPane.setTopAnchor(Subname2, 60d);
                        TextArea rect3 = new TextArea();
                        rect3.setStyle("-fx-font-size: 20pt;");
                        rect3.setPrefSize(500, 300);
                        //rect3.setFill(Color.WHITE);
                        AnchorPane.setRightAnchor(rect3, 740d);
                        AnchorPane.setTopAnchor(rect3, 130d);
                        SesPane.getChildren().add(rect3);
                        Tusername2.setLayoutX(230);
                        Tusername2.setScaleX(5);
                        Tusername2.setScaleY(5);
                        Tusername2.setScaleZ(5);

                        AnchorPane.setTopAnchor(Tusername2, 60d);
                        Tusername2.getStyleClass().add("special-label");
                        SesPane.getChildren().add(Tusername2);

                        // System.out.println("Subject is : " + sn);
                        try {
                            initUI(stage);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TeacherUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            });
        }

        initData();
    }

    Button BckBtn3 = new Button("Back to MainMenu");
    Button AddBtn = new Button("Add");

    Button BckBtn = new Button("Close");

    void setUI() {

        ViewPane.getChildren().add(AddStu);
        SubPane.getChildren().add(Logout);
        ViewPane.getChildren().add(DelStu);

        SesPane.getChildren().add(BckBtnSes);
        SesPane.getChildren().add(Bctsub);
        SesPane.getChildren().add(Endses);
        SesPane.getChildren().add(Listses);
        SesPane.getChildren().add(Attses);

        AddPane.getChildren().add(BckBtn);
        AddPane.getChildren().add(AddBtn);
        AddPane.getChildren().add(lbl);

        ViewPane.getChildren().add(BckBtn3);
        ViewPane.getChildren().add(lbl3);

        AddStu.setPrefSize(250, 50);
        Logout.setPrefSize(250, 50);

        DelStu.setPrefSize(250, 50);
        Bctsub.setPrefSize(350, 50);
        BckBtn3.setPrefSize(300, 50);
        BckBtnSes.setPrefSize(300, 50);
        Endses.setPrefSize(250, 50);
        Listses.setPrefSize(150, 25);
        Attses.setPrefSize(210, 25);

        AddStu.setStyle("-fx-font-size: 20pt;");
        Logout.setStyle("-fx-font-size: 20pt;");

        BckBtnSes.setStyle("-fx-font-size: 20pt;");
        DelStu.setStyle("-fx-font-size: 20pt;");
        BckBtn3.setStyle("-fx-font-size: 20pt;");
        Bctsub.setStyle("-fx-font-size: 20pt;");
        Endses.setStyle("-fx-font-size: 20pt;");
        Listses.setStyle("-fx-font-size: 15pt;");
        Attses.setStyle("-fx-font-size: 15pt;");

        AnchorPane.setLeftAnchor(AddStu, 380d);
        AnchorPane.setBottomAnchor(AddStu, 105d);
        AnchorPane.setLeftAnchor(Logout, 30d);
        AnchorPane.setBottomAnchor(Logout, 30d);

        AnchorPane.setRightAnchor(DelStu, 380d);
        AnchorPane.setBottomAnchor(DelStu, 105d);
        AnchorPane.setLeftAnchor(BckBtnSes, 140d);
        AnchorPane.setBottomAnchor(BckBtnSes, 210d);
        AnchorPane.setLeftAnchor(Bctsub, 115d);
        AnchorPane.setBottomAnchor(Bctsub, 140d);
        AnchorPane.setLeftAnchor(Endses, 165d);
        AnchorPane.setBottomAnchor(Endses, 70d);
        AnchorPane.setRightAnchor(Listses, 370d);
        AnchorPane.setBottomAnchor(Listses, 530d);
        AnchorPane.setRightAnchor(Attses, 145d);
        AnchorPane.setBottomAnchor(Attses, 530d);

        StartSes.setStyle("-fx-font-size: 20pt;");
        StartSes.setPrefSize(250, 50);
        AnchorPane.setRightAnchor(StartSes, 60d);
        AnchorPane.setBottomAnchor(StartSes, 30d);
        ConSes.setStyle("-fx-font-size: 20pt;");
        ConSes.setPrefSize(250, 50);
        anotSes.setStyle("-fx-font-size: 20pt;");
        anotSes.setPrefSize(250, 50);
        AnchorPane.setRightAnchor(ConSes, 60d);
        AnchorPane.setBottomAnchor(ConSes, 30d);
        AnchorPane.setRightAnchor(anotSes, 60d);
        AnchorPane.setBottomAnchor(anotSes, 30d);

        lbl.setScaleX(3);
        lbl.setScaleY(3);
        lbl.setScaleZ(3);

        lbl3.setScaleX(5);
        lbl3.setScaleY(5);
        lbl3.setScaleZ(5);

        table.setEditable(true);
        table2.setEditable(true);
        table3.setEditable(true);
        table4.setEditable(true);
        table5.setEditable(true);
        TableColumn NumCol = new TableColumn("No.");
        NumCol.setMinWidth(50);
        NumCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("num"));
        NumCol.getStyleClass().add("tablecolumn");

        TableColumn firstNameCol = new TableColumn("FullName");
        firstNameCol.setMinWidth(225);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("FullName"));
        firstNameCol.getStyleClass().add("tablecolumn");

        TableColumn lastNameCol = new TableColumn("Student Id.");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("email"));
        lastNameCol.getStyleClass().add("tablecolumn");

        TableColumn emailCol = new TableColumn("Total Score");
        emailCol.setMinWidth(150);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("totalScore"));
        emailCol.getStyleClass().add("tablecolumn");
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Persons, String>>() {
            @Override
            public void handle(CellEditEvent<Persons, String> t) {
                if (API.isNumeric(t.getNewValue())) {
                    ((Persons) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setTotalScore(t.getNewValue());
                    nowCourse.getStudent(t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmail()).getValue().setOther_score(t.getNewValue());
                    update();
                }
            }
        }
        );

        TableColumn midCol = new TableColumn("Midterm");
        midCol.getStyleClass().add("tablecolumn");
        midCol.setMinWidth(100);
        midCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("midterm"));
        midCol.setCellFactory(TextFieldTableCell.forTableColumn());
        midCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Persons, String>>() {
            @Override
            public void handle(CellEditEvent<Persons, String> t) {
                if (API.isNumeric(t.getNewValue())) {
                    ((Persons) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setMidterm(t.getNewValue());
                    nowCourse.getStudent(t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmail()).getValue().setMidExam(t.getNewValue());
                    update();
                }
            }
        }
        );

        TableColumn fiCol = new TableColumn("Final");
        fiCol.getStyleClass().add("tablecolumn");
        fiCol.setMinWidth(100);
        fiCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("finals"));
        fiCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fiCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Persons, String>>() {
            @Override
            public void handle(CellEditEvent<Persons, String> t) {
                if (API.isNumeric(t.getNewValue())) {
                    ((Persons) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setFinals(t.getNewValue());
                    nowCourse.getStudent(t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmail()).getValue().setFinalExam("" + t.getNewValue());
                    update();
                }
            }
        });

        TableColumn GradeCol = new TableColumn("Grade");
        GradeCol.getStyleClass().add("tablecolumn");
        GradeCol.setMinWidth(100);
        GradeCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("grade"));

        TableColumn attCol = new TableColumn("Attended");
        attCol.getStyleClass().add("tablecolumn");
        attCol.setMinWidth(100);
        attCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("totalAttend"));
        attCol.setCellFactory(TextFieldTableCell.forTableColumn());
        attCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Persons, String>>() {
            @Override
            public void handle(CellEditEvent<Persons, String> t) {
                if (API.isNumeric(t.getNewValue())) {
                    ((Persons) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setTotalAttend(t.getNewValue());
                    nowCourse.getStudent(t.getTableView().getItems().get(t.getTablePosition().getRow()).getEmail()).getValue().setAttending_Count(t.getNewValue());
                    update();
                }
            }
        });

        TableColumn SelectCol = new TableColumn("Select");
        SelectCol.getStyleClass().add("tablecolumn");
        SelectCol.setMinWidth(110);
        SelectCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("select"));

        TableColumn NumAddCol = new TableColumn("No.");
        NumAddCol.setMinWidth(50);
        NumAddCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("num"));
        NumAddCol.getStyleClass().add("tablecolumn");

        TableColumn firstAddNameCol = new TableColumn("FullName");
        firstAddNameCol.setMinWidth(225);
        firstAddNameCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("FullName"));
        firstAddNameCol.getStyleClass().add("tablecolumn");

        TableColumn lastNameAddCol = new TableColumn("Student Id.");
        lastNameAddCol.setMinWidth(200);
        lastNameAddCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("email"));
        lastNameAddCol.getStyleClass().add("tablecolumn");

        TableColumn AddSelCol = new TableColumn("Select");
        AddSelCol.getStyleClass().add("tablecolumn");
        AddSelCol.setMinWidth(110);
        AddSelCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("select"));

        TableColumn NumSesCol = new TableColumn("No.");
        NumSesCol.setMinWidth(50);
        NumSesCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("num"));
        NumSesCol.getStyleClass().add("tablecolumn");

        TableColumn firstSesNameCol = new TableColumn("FullName");
        firstSesNameCol.setMinWidth(225);
        firstSesNameCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("FullName"));
        firstSesNameCol.getStyleClass().add("tablecolumn");

        TableColumn lastNameSesCol = new TableColumn("Student Id.");
        lastNameSesCol.setMinWidth(200);
        lastNameSesCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("email"));
        lastNameSesCol.getStyleClass().add("tablecolumn");

        TableColumn NumCoCol = new TableColumn("No.");
        NumCoCol.setMinWidth(30);
        NumCoCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("num"));
        NumCoCol.getStyleClass().add("tablecolumn");

        TableColumn CourseNameCol = new TableColumn("Course Name");
        CourseNameCol.setMinWidth(150);
        CourseNameCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("FullName"));
        CourseNameCol.getStyleClass().add("tablecolumn");

        TableColumn DescriptCol = new TableColumn("Description");
        DescriptCol.setMinWidth(200);
        DescriptCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("email"));
        DescriptCol.getStyleClass().add("tablecolumn");

        TableColumn CoteachCol = new TableColumn("Co-Teach");
        CoteachCol.setMinWidth(100);
        CoteachCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("totalScore"));
        CoteachCol.getStyleClass().add("tablecolumn");

        TableColumn CorSelCol = new TableColumn("Select");
        CorSelCol.getStyleClass().add("tablecolumn");
        CorSelCol.setMinWidth(50);
        CorSelCol.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("select"));

        table.setStyle("-fx-font-size: 15pt;");
        table2.setStyle("-fx-font-size: 10pt;");
        table3.setStyle("-fx-font-size: 10pt;");
        table5.setStyle("-fx-font-size: 10pt;");

        ViewScrollPane.setPrefSize(1180, 360);
        SesListScrollPane.setPrefSize(520, 400);

        Attses.setOnAction(e -> table3.setItems(table3_Data2));
        Listses.setOnAction(e -> table3.setItems(table1_Data));

        table.getColumns().addAll(NumCol, firstNameCol, lastNameCol, emailCol, midCol, fiCol, GradeCol, attCol, SelectCol);
        table2.getColumns().addAll(NumAddCol, firstAddNameCol, lastNameAddCol, AddSelCol);
        table3.getColumns().addAll(NumSesCol, firstSesNameCol, lastNameSesCol);
        table5.getColumns().addAll(NumCoCol, CourseNameCol, DescriptCol, CoteachCol, CorSelCol);

        table.setItems(table1_Data);
        table2.setItems(table2_Data);
        table4.setItems(table3_Data2);

        table.setPrefSize(1180, 600);
        // table5.setPrefWidth(NumCoCol.getMinWidth() + CourseNameCol.getMinWidth() + DescriptCol.getMinWidth() + CoteachCol.getMinWidth() +CorSelCol.getMinWidth());
        table2.setPrefWidth(NumCol.getMinWidth() + firstNameCol.getMinWidth() + lastNameCol.getMinWidth() + SelectCol.getMinWidth());
        table3.setPrefWidth(NumCol.getMinWidth() + firstNameCol.getMinWidth() + lastNameCol.getMinWidth() + attCol.getMinWidth());
        //AddPane.getChildren().add(table2);

        AddPane.getChildren().add(AddScrollPane);
        AsPane.getChildren().add(AddCoScrollPane);
        ViewPane.getChildren().add(ViewScrollPane);
        SesPane.getChildren().add(SesListScrollPane);

        ViewPane.getChildren().add(table);
        SesListScrollPane.setContent(table3);
        AddScrollPane.setContent(table2);
        AddCoScrollPane.setContent(table5);
        ViewScrollPane.setContent(table);

        BckBtn.setLayoutX(320);
        BckBtn.setLayoutY(360);
        AddBtn.setLayoutX(370);
        AddBtn.setLayoutY(360);

        lbl.setLayoutX(320);
        lbl.setLayoutY(10);

        AnchorPane.setRightAnchor(BckBtn3, 480d);
        AnchorPane.setBottomAnchor(BckBtn3, 30d);

        AnchorPane.setRightAnchor(lbl3, 590d);
        AnchorPane.setTopAnchor(lbl3, 50d);

        AddScrollPane.setPrefSize(table2.getPrefWidth(), 300);
        AddCoScrollPane.setPrefSize(630, 300);

        AddStudentsBtn.setSpacing(5);
        AddStudentsBtn.setAlignment(Pos.CENTER);
        DeleteStudentsDel.setSpacing(5);
        DeleteStudentsDel.setAlignment(Pos.CENTER);
        vBoxButtons3.setSpacing(8);

        AddStudentsBtn.setPadding(new Insets(0, 0, 0, ((AddScrollPane.getPrefWidth() / 2)) - (75)));
        //AddStudentsBtn.setPadding(new Insets(0, 0, 0, ((AddScrollPane.getPrefWidth() / 2)) - (75)));

        vBoxButtons3.setPadding(new Insets(0, 0, 0, 50));

        AddScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AddScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        AddCoScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AddCoScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        AddScrollPane.setLayoutX((AddPane.getWidth() / 2) - (AddScrollPane.getPrefWidth() / 2));
        AddScrollPane.setLayoutY((AddPane.getHeight() / 2) - (AddScrollPane.getPrefHeight() / 2));

        AddCoScrollPane.setLayoutX((AddPane.getWidth() / 2) - (AddCoScrollPane.getPrefWidth() / 2));
        AddCoScrollPane.setLayoutY((AddPane.getHeight() / 2) - (AddCoScrollPane.getPrefHeight() / 2));

        ViewScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        ViewScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        ViewScrollPane.setLayoutX((ViewPane.getWidth() / 2) - (ViewScrollPane.getPrefWidth() / 2));
        ViewScrollPane.setLayoutY((ViewPane.getHeight() / 2) - (ViewScrollPane.getPrefHeight() / 2));

        SesListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        SesListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        SesListScrollPane.setLayoutX((SesPane.getWidth() / 4) + (SesListScrollPane.getPrefWidth() / 2 + 100));
        SesListScrollPane.setLayoutY((SesPane.getHeight() / 2) - (SesListScrollPane.getPrefHeight() / 2 - 40));

    }

    void initUI(Stage stage) throws FileNotFoundException {
        initData();
        stage.setTitle("Teacher UI");
        stage.setScene(ViewScene);
        stage.show();
        if (nowCourse.getSessions() != null) {
            if (nowCourse.getSessions().get(nowCourse.getSessions().size() - 1).isStatus()) {
                ViewPane.getChildren().remove(StartSes);
                ViewPane.getChildren().remove(anotSes);
                if (!ViewPane.getChildren().contains(ConSes)) {
                    ViewPane.getChildren().add(ConSes);
                }
            } else {
                if (!User.isOn_Session()) {
                    ViewPane.getChildren().remove(anotSes);
                    ViewPane.getChildren().remove(ConSes);
                    if (!ViewPane.getChildren().contains(StartSes)) {
                        ViewPane.getChildren().add(StartSes);
                    }
                } else {
                    ViewPane.getChildren().remove(StartSes);
                    ViewPane.getChildren().remove(ConSes);
                    if (!ViewPane.getChildren().contains(anotSes)) {
                        ViewPane.getChildren().add(anotSes);
                    }
                }
            }
        } else {
            System.out.println("no session");
            if (!User.isOn_Session()) {
                if (!ViewPane.getChildren().contains(StartSes)) {
                    ViewPane.getChildren().add(StartSes);
                }
            } else {
                ViewPane.getChildren().remove(StartSes);
                ViewPane.getChildren().remove(ConSes);
                if (!ViewPane.getChildren().contains(anotSes)) {
                    ViewPane.getChildren().add(anotSes);
                }
            }
        }
        table1_Data.clear();
        table2_Data.clear();
        table3_Data.clear();
        table3_Data2.clear();
        // continue update student list data //
        if (!update_Student.isRun()) {
            update_Student_Timer.schedule(update_Student, 0, 30000);
        } else {
            table1_Data.clear();
            update_Student_Function();
        }

        Bctsub.setOnAction(e -> {
            stage.setScene(SubScene);
            ViewPane.getChildren().add(ConSes);
        });

        // add Student button
        AddBtn.setOnAction(eh -> {
            ArrayList<Persons> arr = new ArrayList<>();
            for (var i : table2_Data) {
                if (i.getSelect().isSelected()) {
                    this.addStu(i.getEmail());
                    arr.add(i);
                }
            }
            if (arr != null) {
                for (Persons i : arr) {
                    i.getSelect().setSelected(false);
                    table2_Data.remove(i);
                }
            }
            for (int i = 0; i < table2_Data.size(); i++) {
                table2_Data.get(i).setNum("" + (i + 1));
            }
            update();
        });

        Stage addstage = new Stage();

        CancelHandlerClass h2 = new CancelHandlerClass();
        Logout.setOnAction(h2);

        AddStu.setOnAction(e -> {
            // init data
            table2_Data.clear();
            if (StudentArr != null) {
                for (int i = 0; i < StudentArr.size(); i++) {
                    if (!nowCourse.isStudentExist(StudentArr.get(i))) {
                        table2_Data.add(new Persons("" + (i + 1), StudentArr.get(i).getFirstName() + " " + StudentArr.get(i).getLastName(), StudentArr.get(i).getStudentId(),
                                "0", "0", "0", "X", "0"));
                    }
                }
            }
            addstage.setScene(AddScene);
            addstage.show();

        });

        DelStu.setOnAction(e -> {
            ArrayList<Persons> arr = new ArrayList();
            for (var i : table1_Data) {
                if (i.getSelect().isSelected()) {
                    arr.add(i);
                }
            }
            for (Persons i : arr) {
                this.delStu(i.getEmail());
            }
        });
        StartSes.setOnAction(e -> {
            // init Sessions
            Session ss = new Session(nowCourse);
            ss.setStatus(true);
            ss.setStaff(User);
            nowCourse.addSession(ss);
            Session.start_Session(ss);
            this.User.setOn_Session(true);
            // init data and timer
            update();
            if (!update_Attend.isRun()) {
                session_Timer.schedule(update_Attend, 0, 500);
            }
            stage.setScene(SesScene);
            SesPane.getChildren().add(Ong);
        });

        Endses.setOnAction(e -> {
            nowCourse.upDateLastestSession();
            nowCourse.getLastestSession().setStatus(false);
            Session.end_Session(nowCourse.getLastestSession());
            this.User.setOn_Session(false);
            nowCourse.AttendingStudent();
            update();

            stage.setScene(ViewScene);
            SesPane.getChildren().remove(Ong);

        });

        ConSes.setOnAction(e -> {
            Session ss = nowCourse.getLastestSession();
            if (!ss.isStatus()) {
                return;
            }
            // timer
            if (!update_Attend.isRun()) {
                session_Timer.schedule(update_Attend, 0, 500);
            }

            stage.setScene(SesScene);
            ViewPane.getChildren().remove(ConSes);
        });
        BckBtnSes.setOnAction(e -> {
            stage.setScene(ViewScene);
            ViewPane.getChildren().add(ConSes);
        });

        BckBtn.setOnAction(e -> addstage.close());

        BckBtn3.setOnAction(e -> {
            stage.setScene(SubScene);

        });

    }

    void delStu(String... stuId) {
        ArrayList<Pair<Student, Grading>> arr = new ArrayList<>();
        for (var i : stuId) {
            for (Pair<Student, Grading> st : nowCourse.getStudents()) {
                if (st.getKey().getStudentId().equals(i)) {
                    arr.add(st);
                }
            }
        }
        for (Pair<Student, Grading> i : arr) {
            i.getKey().getCourse().remove(nowCourse);
            nowCourse.getStudents().remove(i);
        }
        update_Student_Function();
        update();
    }

    void addStu(String... stuId) {
        for (var i : stuId) {
            for (var st : StudentArr) {
                if (st.getStudentId().equals(i)) {
                    nowCourse.addStudent(st);
                    st.addCourse(nowCourse);
                    API.saveToDatabase(st);
                }
            }
        }
        update_Student_Function();
        update();
    }

    void update() {
        API.saveToDatabase(this.nowCourse, this.User);
    }

    public class CancelHandlerClass implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            Stage stage = (Stage) Logout.getScene().getWindow();
            stage.close();
        }
    }

}