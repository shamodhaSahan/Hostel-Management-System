package lk.ijse.hms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.controller.student.AddStudentFormController;
import lk.ijse.hms.controller.student.UpdateStudentFormController;
import lk.ijse.hms.view.tm.StudentTM;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/4/2023
 * Time :11:50 PM
 */

public class ManageStudentFormController {
    @FXML
    private TableView<StudentTM> tblStudent;

    @FXML
    private JFXButton btnUpdateDelete;

    @FXML
    private TextField txtSearchStudent;

    private final StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);


    public void initialize(){
        btnUpdateDelete.setDisable(true);
        setCellFactory();
        refreshTable();
    }

    public void refreshTable() {
        tblStudent.setItems(FXCollections.observableArrayList(studentBO.getAllStudent().stream().map(studentDTO -> new StudentTM(
                studentDTO.getStudentId(),
                studentDTO.getName(),
                studentDTO.getAddress(),
                studentDTO.getContactNo(),
                studentDTO.getDob(),
                studentDTO.getGender()))
                .collect(Collectors.toList()))
        );
        txtSearchStudent.textProperty().addListener((observableValue, pre, curr) ->{
            if (!Objects.equals(pre, curr)){
                tblStudent.getItems().clear();
                tblStudent.setItems(FXCollections.observableArrayList(studentBO.searchBookByText(curr).stream().map(studentDTO -> new StudentTM(
                        studentDTO.getStudentId(),
                        studentDTO.getName(),
                        studentDTO.getAddress(),
                        studentDTO.getContactNo(),
                        studentDTO.getDob(),
                        studentDTO.getGender()))
                        .collect(Collectors.toList()))
                );
            }

        } );

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observableValue, pre, curr) -> {
            if (curr!=pre || curr!=null){
                btnUpdateDelete.setDisable(false);
            }
        });
    }

    private void setCellFactory() {
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dob"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("gender"));
    }

    @FXML
    void btnAddStudentOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/student/AddStudentForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent load = fxmlLoader.load();
            AddStudentFormController addStudentFormController = fxmlLoader.getController();
            addStudentFormController.init(this);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("assets/Add_user_icon.png"));
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Student Registration Form");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"forms error ..!").show();
        }
    }

    @FXML
    void btnUpdateDeleteOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/student/UpdateStudentForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent load = fxmlLoader.load();
            UpdateStudentFormController controller = fxmlLoader.getController();
            controller.init(tblStudent.getSelectionModel().getSelectedItem(),this);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("assets/update_icon.jpg"));
            stage.setTitle("Update and Delete Student details");
            stage.setScene(new Scene(load));
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"forms error ..!").show();
        }
    }

}
