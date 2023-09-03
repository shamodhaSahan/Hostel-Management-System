package lk.ijse.hms.util;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/7/2023
 * Time :5:37 PM
 */

public class RegexUtil {
    public Object validate(LinkedHashMap<TextField, Pattern> map, JFXButton btn) {
        for (TextField key : map.keySet()) {
            Pattern pattern = map.get(key);
            if (!pattern.matcher(key.getText()).matches()){
                addError(key,btn);
                return key;
            }
            removeError(key,btn);
        }
        return true;
    }

    private void removeError(TextField txtField,JFXButton btn) {
        txtField.setStyle("-fx-text-fill: green");
        btn.setDisable(false);
    }

    private void addError(TextField txtField,JFXButton btn) {
        if (txtField.getText().length() > 0)
            txtField.setStyle("-fx-text-fill: red");
        btn.setDisable(true);
    }
}
