package lk.ijse.hms.controller.student;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.controller.ManageStudentFormController;
import lk.ijse.hms.dto.StudentDTO;
import lk.ijse.hms.util.RegexUtil;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :1:00 AM
 */

public class AddStudentFormController {
    @FXML
    private JFXButton btnRegister;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtDOB;

    @FXML
    private JFXComboBox<String> cmbGender;

    private ManageStudentFormController manageStudentFormController;

    private final RegexUtil regexUtil = new RegexUtil();

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private final StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);

    public void init(ManageStudentFormController manageStudentFormController) {
        this.manageStudentFormController = manageStudentFormController;
        btnRegister.setDisable(true);
        cmbGender.getItems().addAll("MALE","FEMALE","OTHER");
        hashMap.put(txtId, Pattern.compile("^S[0-9]{3,}$"));
        hashMap.put(txtName, Pattern.compile("^[A-z ]{3,30}$"));
        hashMap.put(txtAddress, Pattern.compile("^[A-z0-9 /,]{4,20}$"));
        hashMap.put(txtContact, Pattern.compile("^(?:7|0|(?:\\+94))(70|77|78|74|76|72|71)[0-9]{7}$"));
        hashMap.put(txtDOB, Pattern.compile("^[1-9]{1}[0-9]{3}-[0-9]{2}-[0-9]{2}$"));
    }


    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        try {
            if (cmbGender.getSelectionModel().isEmpty()) {
                cmbGender.setFocusColor(Color.RED);
                cmbGender.requestFocus();
                return;
            }
            if (studentBO.saveStudent(new StudentDTO(txtId.getText(), txtName.getText(), txtAddress.getText(), txtContact.getText(), LocalDate.parse(txtDOB.getText()), cmbGender.getValue()))){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully saved..!").show();
                manageStudentFormController.refreshTable();
                txtId.clear();
                txtName.clear();
                txtAddress.clear();
                txtContact.clear();
                txtDOB.clear();
                cmbGender.getSelectionModel().clearSelection();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to Save the student..!").show();
            }
        }catch (DateTimeParseException e){
            txtDOB.setStyle("-fx-text-fill: red");
            txtDOB.requestFocus();
        }catch (DuplicateException e){
            txtId.setStyle("-fx-text-fill: red");
            txtId.requestFocus();
            new Alert(Alert.AlertType.ERROR,"Student Id Already Exists..!").show();
        }
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
    void txtDOBOnAction(ActionEvent event) {
        cmbGender.requestFocus();
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        txtName.requestFocus();
    }

    @FXML
    void txtKeyReleased(KeyEvent keyEvent) {
        regexUtil.validate(hashMap, btnRegister);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = regexUtil.validate(hashMap, btnRegister);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtAddress.requestFocus();
    }

    @FXML
    public void cmbGenderOnAction(ActionEvent actionEvent) {
        cmbGender.setFocusColor(Color.GREEN);
    }

}
