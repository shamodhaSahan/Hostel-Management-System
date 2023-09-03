package lk.ijse.hms.controller.user;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.LoginBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.controller.ManageUserFormController;
import lk.ijse.hms.dto.UserDTO;
import lk.ijse.hms.util.RegexUtil;
import lk.ijse.hms.view.tm.RoomTM;
import lk.ijse.hms.view.tm.UserTM;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :11:39 AM
 */

public class UpdateUserFormController {

    @FXML
    private Label lblUserId;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXButton btnDelete;

    private UserDTO userDTO;

    private UserDetailsFormController userDetailsFormController;

    private ManageUserFormController manageUserFormController;

    private boolean isFromUserDetails;

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private final LoginBO loginBO = BoFactory.getInstance().getBO(BoTypes.LOGIN);

    private final RegexUtil regexUtil = new RegexUtil();

    public void init(UserDTO userDTO, UserDetailsFormController userDetailsFormController) {
        this.userDTO = userDTO;
        this.userDetailsFormController = userDetailsFormController;
        isFromUserDetails = true;
        fillAllFields(userDTO);
    }

    public void init(UserDTO userDTO, ManageUserFormController manageUserFormController) {
        this.userDTO = userDTO;
        this.manageUserFormController = manageUserFormController;
        isFromUserDetails = false;
        fillAllFields(userDTO);
    }

    private void fillAllFields(UserDTO userDTO){
        lblUserId.setText(userDTO.getUserId());
        txtName.setText(userDTO.getName());
        txtContact.setText(userDTO.getTelNo());
        txtEmail.setText(userDTO.getEmail());
        txtUserName.setText(userDTO.getUserName());
        txtPassword.setText(userDTO.getPassword());
        btnUpdate.setDisable(true);
        hashMap.put(txtName, Pattern.compile("^[A-z ]{3,30}$"));
        hashMap.put(txtContact, Pattern.compile("^(?:7|0|(?:\\+94))(70|77|78|74|76|72|71)[0-9]{7}$"));
        hashMap.put(txtEmail, Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"));
        hashMap.put(txtUserName, Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"));
        hashMap.put(txtPassword, Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"));
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (loginBO.deleteUser(userDTO)) {
            new Alert(Alert.AlertType.INFORMATION, "delete successful..!").show();
            if (isFromUserDetails){
                userDetailsFormController.mainFormController.logOutOnAction(event);
                ((Stage)userDetailsFormController.btnUpdate.getScene().getWindow()).close();
            }else {
                manageUserFormController.refreshTable();
            }
            ((Stage)btnDelete.getScene().getWindow()).close();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to delete user..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            userDTO = new UserDTO(lblUserId.getText(), txtName.getText(), txtContact.getText(), txtEmail.getText(), txtUserName.getText(), txtPassword.getText());
            if (loginBO.updateUser(userDTO)){
                if (isFromUserDetails){
                    userDetailsFormController.init(userDTO, userDetailsFormController.mainFormController);
                }else {
                    manageUserFormController.refreshTable();
                }
                new Alert(Alert.AlertType.INFORMATION, "Update Successful..!").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to update the user..!").show();
            }
        }catch (DuplicateException e){
            new Alert(Alert.AlertType.WARNING, "username Already exists by another user..!").show();
        }
    }

    @FXML
    void txtContactOnAction(ActionEvent event) {
        txtEmail.requestFocus();
    }

    @FXML
    void txtEmailOnAction(ActionEvent event) {
        txtUserName.requestFocus();
    }

    @FXML
    void txtKeyReleased(KeyEvent keyEvent) {
        regexUtil.validate(hashMap, btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = regexUtil.validate(hashMap, btnUpdate);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtContact.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        if (!btnUpdate.isDisable())
            btnUpdateOnAction(event);
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
}
