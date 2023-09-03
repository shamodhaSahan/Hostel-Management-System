package lk.ijse.hms.controller.student;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.bo.exception.InUseException;
import lk.ijse.hms.controller.ManageStudentFormController;
import lk.ijse.hms.controller.ManageUserFormController;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.dto.StudentDTO;
import lk.ijse.hms.util.RegexUtil;
import lk.ijse.hms.view.tm.StudentTM;
import lk.ijse.hms.view.tm.UserTM;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :1:29 AM
 */

public class UpdateStudentFormController {

    @FXML
    public JFXTextField txtDOB;
    @FXML
    private Label lblStudentId;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXComboBox<String> cmbGender;

    @FXML
    private JFXButton btnDelete;

    private ManageStudentFormController manageStudentFormController;

    private StudentTM studentTM;

    private final StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private final RegexUtil regexUtil = new RegexUtil();

    public void init(StudentTM studentTM, ManageStudentFormController manageStudentFormController) {
        this.studentTM = studentTM;
        this.manageStudentFormController = manageStudentFormController;
        btnUpdate.setDisable(true);
        cmbGender.getItems().addAll("MALE","FEMALE","OTHER");
        lblStudentId.setText(studentTM.getStudentId());
        txtName.setText(studentTM.getName());
        txtAddress.setText(studentTM.getAddress());
        txtContact.setText(studentTM.getContactNo());
        txtDOB.setText(String.valueOf(studentTM.getDob()));
        cmbGender.setValue(studentTM.getGender());
        hashMap.put(txtName, Pattern.compile("^[A-z ]{3,30}$"));
        hashMap.put(txtAddress, Pattern.compile("^[A-z0-9 /,]{4,20}$"));
        hashMap.put(txtContact, Pattern.compile("^(?:7|0|(?:\\+94))(70|77|78|74|76|72|71)[0-9]{7}$"));
        hashMap.put(txtDOB, Pattern.compile("^[1-9]{1}[0-9]{3}-[0-9]{2}-[0-9]{2}$"));
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        StudentDTO studentDTO = new StudentDTO(lblStudentId.getText(), txtName.getText(), txtAddress.getText(), txtContact.getText(), LocalDate.parse(txtDOB.getText()), cmbGender.getValue());
        try {
            if (studentBO.deleteStudent(studentDTO)) {
                new Alert(Alert.AlertType.INFORMATION, "delete successful..!").show();
                manageStudentFormController.refreshTable();
                ((Stage) btnDelete.getScene().getWindow()).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete student..!").show();
            }
        }catch (InUseException e){
            new Alert(Alert.AlertType.WARNING, "student have UnPaid reservation..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (cmbGender.getSelectionModel().isEmpty()) {
            cmbGender.setFocusColor(Color.RED);
            cmbGender.requestFocus();
            return;
        }
        try {
            String studentId = lblStudentId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String contactNo = txtContact.getText();
            LocalDate dob = LocalDate.parse(txtDOB.getText());
            String gender = cmbGender.getValue();
            if (studentTM.getName().equals(name) && studentTM.getAddress().equals(address) && studentTM.getContactNo().equals(contactNo) && studentTM.getDob().equals(dob) && studentTM.getGender().equals(gender)){
                new Alert(Alert.AlertType.WARNING, "Nothing to update..!").show();
                btnUpdate.setDisable(true);
                return;
            }
            if (studentBO.updateStudent(new StudentDTO(studentId, name, address, contactNo, dob, gender))){
                manageStudentFormController.refreshTable();
                new Alert(Alert.AlertType.INFORMATION, "Update Successful..!").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to update the student..!").show();
            }
        }catch (DateTimeParseException e){
            txtDOB.setStyle("-fx-text-fill: red");
            txtDOB.requestFocus();
        }
    }

    @FXML
    void cmbGenderOnAction(ActionEvent event) {
        cmbGender.setFocusColor(Color.GREEN);
    }

    @FXML
    void txtAddressOnAction(ActionEvent event) {
        txtContact.requestFocus();
    }

    @FXML
    void txtContactOnAction(ActionEvent event) {
        txtDOB.requestFocus();
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtAddress.requestFocus();
    }

    @FXML
    public void txtDOBOnAction(ActionEvent actionEvent) {
        cmbGender.requestFocus();
    }

    @FXML
    public void txtKeyReleased(KeyEvent keyEvent) {
        regexUtil.validate(hashMap, btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = regexUtil.validate(hashMap, btnUpdate);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
