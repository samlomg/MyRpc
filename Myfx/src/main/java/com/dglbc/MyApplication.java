package com.dglbc;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = getGridPaneLogin();
        StackPane root = new StackPane();
        root.getChildren().add(gridPane);
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(MyApplication.class.getResource("/css/Login.css").toExternalForm());
        //设置舞台！
        primaryStage.setTitle("Hello,World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static GridPane getGridPaneLogin(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text scenetitle = new Text("Welcome");
        scenetitle.setId("welcome-text");
//        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //创建Label对象，放到第0列，第1行
        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        //创建文本输入框，放到第1列，第1行
        TextField userTextField = new TextField();
        userTextField.setPromptText("Your UserName");
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Your password");
        grid.add(pwBox, 1, 2);

        Label message = new Label("");
        grid.add(message, 0, 5,2,1); //colspan行合并多少个（水平），rowspan是列合并（垂直）


        JFXButton btn = new JFXButton("Sign in");
        btn.getStyleClass().add("button-raised");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (userTextField.getText().equals("") && pwBox.getText().equals("")){
                    message.setText("Your password has been confirmed");
                    message.setTextFill(Color.rgb(21, 117, 84));
                    ObservableList<Stage> stage = FXRobotHelper.getStages();
                    Scene scene = null;
                    try {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("/sample/fxml_tableview.fxml")));
                        stage.get(0).setScene(scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    message.setText("Your password is incorrect!");
                    message.setTextFill(Color.rgb(210, 39, 30));
                }
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);//将按钮控件作为子节点
        grid.add(hbBtn, 1, 4);//将HBox pane放到grid中的第1列，第4行

        final Text actiontarget=new Text();//增加用于显示信息的文本
        actiontarget.setId("actiontarget");
        grid.add(actiontarget, 1, 6);

        return grid;
    }

}
