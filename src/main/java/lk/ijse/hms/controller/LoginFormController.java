package lk.ijse.hms.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.LoginBO;
import lk.ijse.hms.bo.exception.NotFoundException;
import lk.ijse.hms.controller.user.AddUserFormController;
import lk.ijse.hms.dto.UserDTO;

import java.io.IOException;
import java.net.URL;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :7:37 PM
 */

public class LoginFormController {

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXPasswordField pfPassword;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private FontAwesomeIconView fxEyeIcon;

    private final LoginBO loginBO = BoFactory.getInstance().getBO(BoTypes.LOGIN);

    private boolean isHide = true;

    public void initialize(){
        txtPassword.setVisible(false);
    }

    @FXML
    void btnCreateAccountOnAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/user/AddUserForm.fxml"));
            Parent load = fxmlLoader.load();
            AddUserFormController addUserFormController = fxmlLoader.getController();
            addUserFormController.init(this);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("assets/Add_user_icon.png"));
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create User Account");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        String password = (isHide?pfPassword:txtPassword).getText();
        if (userName.isEmpty()){
            txtUserName.setFocusColor(Color.RED);
            txtUserName.requestFocus();
        }else if (password.isEmpty()){
            (isHide?pfPassword:txtPassword).setFocusColor(Color.RED);
            (isHide?pfPassword:txtPassword).requestFocus();
        }else {
            UserDTO user = new UserDTO();
            user.setUserName(userName);
            user.setPassword(password);
            try {
                UserDTO userDTO = loginBO.verifyUser(user);
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/MainForm.fxml"));
                Parent load = fxmlLoader.load();
                MainFormController mainFormController = fxmlLoader.getController();
                mainFormController.init(userDTO);
                Stage stage = new Stage();
                stage.getIcons().add(new Image("assets/logo.png"));
                stage.setTitle("Dashboard");
                stage.setScene(new Scene(load));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.centerOnScreen();
                stage.show();
                Stage window = (Stage) txtUserName.getScene().getWindow();
                window.close();
            }catch (NotFoundException e){
                (isHide?pfPassword:txtPassword).setFocusColor(Color.RED);
                txtUserName.setFocusColor(Color.RED);
                txtUserName.requestFocus();
                new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR,"ui not found...!").show();
            }
        }
    }

    @FXML
    void close(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void eyeIconOnMouseClick(MouseEvent event) {
        if (isHide){
            isHide = false;
            fxEyeIcon.setIcon(FontAwesomeIcon.EYE);
            txtPassword.setText(pfPassword.getText());
            pfPassword.setVisible(false);
            txtPassword.setVisible(true);
        }else {
            isHide = true;
            fxEyeIcon.setIcon(FontAwesomeIcon.EYE_SLASH);
            pfPassword.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            pfPassword.setVisible(true);
        }
    }

    @FXML
    public void passwordRelease(KeyEvent keyEvent) {
        (isHide?pfPassword:txtPassword).setFocusColor(Color.valueOf("#00b000"));
    }

    @FXML
    public void userNameRelease(KeyEvent keyEvent) {
        txtUserName.setFocusColor(Color.valueOf("#00b000"));
    }

    public void init(UserDTO userDTO) {
        txtUserName.setText(userDTO.getUserName());
        (isHide?pfPassword:txtPassword).setText(userDTO.getPassword());
    }

    @FXML
    public void txtUserNameOnAction(ActionEvent actionEvent) {
        if (isHide) pfPassword.requestFocus();
        else txtPassword.requestFocus();
    }

    @FXML
    public void passwordOnAction(ActionEvent actionEvent) {
        btnLoginOnAction(actionEvent);
    }
}
