package lk.ijse.hms.controller.room;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.controller.ManageRoomFormController;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.util.RegexUtil;
import lk.ijse.hms.view.tm.RoomTM;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :9:46 AM
 */

public class AddRoomFormController {

    @FXML
    private JFXTextField txtRoomTypeId;

    @FXML
    private JFXComboBox<String> cmbType;

    @FXML
    private JFXTextField txtKeyMoney;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXButton btnAdd;

    private ManageRoomFormController manageRoomFormController;

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private final RegexUtil regexUtil = new RegexUtil();

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void init(ManageRoomFormController manageRoomFormController) {
        this.manageRoomFormController = manageRoomFormController;
        cmbType.getItems().addAll("Non-AC", "Non-AC / FOOD", "AC", "AC / FOOD");
        hashMap.put(txtRoomTypeId, Pattern.compile("^RM-[0-9]{4}$"));
        hashMap.put(txtKeyMoney, Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$"));
        hashMap.put(txtQty, Pattern.compile("^[1-9][0-9]*$"));
    }

    @FXML
    public void txtRoomTypeIdOnAction(ActionEvent actionEvent) {
        cmbType.requestFocus();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (cmbType.getSelectionModel().isEmpty()){
            cmbType.setFocusColor(Color.RED);
            cmbType.requestFocus();
            return;
        }
        RoomDTO roomDTO = new RoomDTO(txtRoomTypeId.getText(), cmbType.getValue(), Double.parseDouble(txtKeyMoney.getText()), Integer.parseInt(txtQty.getText()));
        try {
            if (roomBO.saveRoom(roomDTO)){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully saved..!").show();
                manageRoomFormController.refreshTable();
                txtRoomTypeId.clear();
                cmbType.setValue(null);
                txtKeyMoney.clear();
                txtQty.clear();
                txtRoomTypeId.requestFocus();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to Save the room..!").show();
            }
        }catch (DuplicateException e){
            new Alert(Alert.AlertType.ERROR,"Room Id Already Exists..!").show();
            txtRoomTypeId.setFocusColor(Color.RED);
            txtRoomTypeId.selectAll();
            txtRoomTypeId.requestFocus();
        }
    }

    @FXML
    public void cmbTypeOnAction(ActionEvent actionEvent) {
        cmbType.setFocusColor(Color.GREEN);
        txtKeyMoney.requestFocus();
    }

    @FXML
    void txtKeyMoneyOnAction(ActionEvent event) {
        txtQty.requestFocus();
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        if (!btnAdd.isDisable())
            btnAddOnAction(event);
    }

    @FXML
    public void txtKeyReleased(KeyEvent keyEvent) {
        regexUtil.validate(hashMap, btnAdd);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = regexUtil.validate(hashMap, btnAdd);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
