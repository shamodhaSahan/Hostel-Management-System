package lk.ijse.hms.controller.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.LoginBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.controller.LoginFormController;
import lk.ijse.hms.controller.ManageUserFormController;
import lk.ijse.hms.dto.UserDTO;
import lk.ijse.hms.util.RegexUtil;
import lk.ijse.hms.view.tm.UserTM;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :11:39 AM
 */

public class AddUserFormController {
    @FXML
    private JFXButton btnRegister;

    @FXML
    private JFXTextField txtId;

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
    private ManageUserFormController manageUserFormController;

    private LoginFormController loginFormController;

    private final LoginBO loginBO = BoFactory.getInstance().getBO(BoTypes.LOGIN);

    private final RegexUtil regexUtil = new RegexUtil();

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private boolean isCreateNewUser;

    public void init(ManageUserFormController manageUserFormController) {
        this.manageUserFormController = manageUserFormController;
        this.isCreateNewUser = false;
    }

    public void init(LoginFormController loginFormController) {
        this.loginFormController = loginFormController;
        this.isCreateNewUser = true;
    }

    public void initialize(){
        clearUserField();
        hashMap.put(txtName, Pattern.compile("^[A-z ]{3,30}$"));
        hashMap.put(txtContact, Pattern.compile("^(?:7|0|(?:\\+94))(70|77|78|74|76|72|71)[0-9]{7}$"));
        hashMap.put(txtEmail, Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"));
        hashMap.put(txtUserName, Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"));
        hashMap.put(txtPassword, Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"));
    }

    private void clearUserField(){
        btnRegister.setDisable(true);
        txtId.setText(loginBO.getNextUserId());
        txtName.clear();
        txtContact.clear();
        txtEmail.clear();
        txtUserName.clear();
        txtPassword.clear();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        UserDTO userDTO = new UserDTO(txtId.getText(), txtName.getText(), txtContact.getText(), txtEmail.getText(), txtUserName.getText(), txtPassword.getText());
        try {
            if (loginBO.saveUser(userDTO)) {
                if (isCreateNewUser) {
                    loginFormController.init(userDTO);
                    ((Stage)btnRegister.getScene().getWindow()).close();
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully saved..!\nusername password will be automatically added...").show();
                } else {
                    manageUserFormController.refreshTable();
                    clearUserField();
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully saved..!").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to Save the user..!").show();
            }
        }catch (DuplicateException e){
            txtUserName.setStyle("-fx-text-fill: red");
            txtUserName.selectAll();
            txtUserName.requestFocus();
            new Alert(Alert.AlertType.ERROR,"username Already Exists..!").show();
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
    void txtNameOnAction(ActionEvent event) {
        txtContact.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        if (!btnRegister.isDisable())
            btnRegisterOnAction(event);
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    public void txtKeyReleased(KeyEvent keyEvent) {
        regexUtil.validate(hashMap, btnRegister);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = regexUtil.validate(hashMap, btnRegister);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
