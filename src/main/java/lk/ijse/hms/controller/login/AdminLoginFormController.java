package lk.ijse.hms.controller.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.hms.controller.MainFormController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/9/2023
 * Time :12:51 AM
 */

public class AdminLoginFormController {

    @FXML
    private JFXTextField txtAdmin;

    @FXML
    private JFXPasswordField txtPassword;

    private MainFormController mainFormController;
    private final Admin admin1 = new Admin("admin", "1234");
    private final Admin admin2 = new Admin("shamodha", "shamodha2000");
    @FXML
    void loginOnAction(ActionEvent event) {
        try {
            if (!((txtAdmin.getText().equals(admin1.getAdmin()) && txtPassword.getText().equals(admin1.getPassword())) ||
                    (txtAdmin.getText().equals(admin2.getAdmin()) && txtPassword.getText().equals(admin2.getPassword())))){
                new Alert(Alert.AlertType.ERROR, "wrong admin password").show();
                return;
            }
            mainFormController.goUserOnAction();
            ((Stage)txtAdmin.getScene().getWindow()).close();
        }catch (NullPointerException e){}
    }

    public void init(MainFormController mainFormController) {
        this.mainFormController = mainFormController;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Admin{
        String admin;
        String password;
    }
}
