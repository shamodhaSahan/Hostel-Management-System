package lk.ijse.hms.controller.room;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.exception.InUseException;
import lk.ijse.hms.controller.ManageRoomFormController;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.util.RegexUtil;
import lk.ijse.hms.view.tm.RoomTM;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :9:45 AM
 */

public class UpdateRoomFormController {
    @FXML
    private Label lblRoomTypeId;

    @FXML
    private JFXComboBox<String> cmbType;

    @FXML
    private JFXTextField txtKeyMoney;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDelete;

    private ManageRoomFormController manageRoomFormController;

    private RoomTM roomTM;

    private final RegexUtil regexUtil = new RegexUtil();

    private final LinkedHashMap<TextField, Pattern> hashMap = new LinkedHashMap<>();

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void init(RoomTM roomTM, ManageRoomFormController manageRoomFormController) {
        this.manageRoomFormController = manageRoomFormController;
        this.roomTM = roomTM;
        cmbType.getItems().addAll("Non-AC", "Non-AC / FOOD", "AC", "AC / FOOD");
        lblRoomTypeId.setText(roomTM.getRoomTypeId());
        cmbType.getSelectionModel().select(roomTM.getType());
        txtKeyMoney.setText(String.valueOf(roomTM.getKeyMoney()));
        txtQty.setText(String.valueOf(roomTM.getQty()));
        btnUpdate.setDisable(true);
        Pattern keyMoneyPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]*$");
        hashMap.put(txtKeyMoney, keyMoneyPattern);
        hashMap.put(txtQty, qtyPattern);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        RoomDTO roomDTO = new RoomDTO(lblRoomTypeId.getText(), roomTM.getType(), roomTM.getKeyMoney(), roomTM.getQty());
        try {
            if (roomBO.deleteRoom(roomDTO)) {
                new Alert(Alert.AlertType.INFORMATION, "delete successful..!").show();
                manageRoomFormController.refreshTable();
                ((Stage) btnDelete.getScene().getWindow()).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete room..!").show();
            }
        }catch (InUseException e){
            new Alert(Alert.AlertType.WARNING, "This room have UnPaid reservation..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String type = cmbType.getValue();
        double keyMoney = Double.parseDouble(txtKeyMoney.getText());
        int qty = Integer.parseInt(txtQty.getText());
        if (roomTM.getType().equals(type) && roomTM.getKeyMoney() == keyMoney && roomTM.getQty() == qty){
            new Alert(Alert.AlertType.WARNING, "Nothing to update..!").show();
            btnUpdate.setDisable(true);
            return;
        }
        if (roomBO.updateBook(new RoomDTO(lblRoomTypeId.getText(), type, keyMoney, qty))){
            manageRoomFormController.refreshTable();
            new Alert(Alert.AlertType.INFORMATION, "Update Successful..!").show();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "Fail to Update room..!").show();
        }
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
