package lk.ijse.hms.controller.reservation;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.controller.ManageReservationFormController;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.dto.StudentDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/7/2023
 * Time :9:18 AM
 */

public class AddReservationFormController {

    @FXML
    private Label lblReservation;

    @FXML
    private JFXComboBox<String> cmbStudentId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private JFXComboBox<String> cmbRoomTypeId;

    @FXML
    private JFXTextField txtType;

    @FXML
    private JFXTextField txtKeyMoney;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXComboBox<String> cmbPaymentStatus;

    private ManageReservationFormController manageReservationFormController;

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    private final StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);

    public void init(ManageReservationFormController manageReservationFormController) {
        this.manageReservationFormController = manageReservationFormController;
        lblReservation.setText(roomBO.getNextReservationId());
        cmbStudentId.getItems().addAll(studentBO.getAllStudent().stream().map(studentDTO -> studentDTO.getStudentId()).collect(Collectors.toList()));
        cmbRoomTypeId.getItems().addAll(roomBO.getAllRoom().stream().map(roomDTO -> roomDTO.getRoomTypeId()).collect(Collectors.toList()));
        cmbPaymentStatus.getItems().addAll("PAID", "UNPAID");
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        if(cmbRoomTypeId.getSelectionModel().isEmpty()){
            cmbRoomTypeId.requestFocus();
            return;
        }
        if (cmbPaymentStatus.getSelectionModel().isEmpty()){
            cmbPaymentStatus.requestFocus();
            return;
        }
        if (Integer.parseInt(txtQty.getText()) < 1){
            new Alert(Alert.AlertType.WARNING,"Room Quantity is over..!").show();
            return;
        }
        String reservationId = lblReservation.getText();
        String studentId = cmbStudentId.getValue();
        String name = txtName.getText();
        String roomTypeId = cmbRoomTypeId.getValue();
        String type = txtType.getText();
        Double keyMoney = Double.parseDouble(txtKeyMoney.getText());
        CustomDTO.Status status = cmbPaymentStatus.getValue()=="PAID"? CustomDTO.Status.PAID: CustomDTO.Status.UNPAID;
        Date date = Date.valueOf(LocalDate.now());
        CustomDTO customDTO = new CustomDTO(reservationId, studentId, name, roomTypeId, type,keyMoney, status, date);
        if (roomBO.addReservation(customDTO)){
            cmbStudentId.setValue(null);
            cmbRoomTypeId.setValue(null);
            manageReservationFormController.refreshTable();
            new Alert(Alert.AlertType.INFORMATION,"Reservation added successful..!").show();
        }else {
            new Alert(Alert.AlertType.WARNING,"Reservation not added..!").show();
        }

    }

    @FXML
    void cmbRoomTypeIdOnAction(ActionEvent event) {
        String roomTypeId = cmbRoomTypeId.getSelectionModel().getSelectedItem();
        if (roomTypeId != null){
            RoomDTO roomDTO = roomBO.getRoomById(roomTypeId);
            txtType.setText(roomDTO.getType());
            txtKeyMoney.setText(String.valueOf(roomDTO.getKeyMoney()));
            txtQty.setText(String.valueOf(roomDTO.getQty()));
        }else {
            txtType.clear();
            txtKeyMoney.clear();
            txtQty.clear();
        }
    }

    @FXML
    void cmbStudentIdOnAction(ActionEvent event) {
        String studentId = cmbStudentId.getSelectionModel().getSelectedItem();
        if (studentId != null){
            StudentDTO studentDTO = studentBO.getStudentById(studentId);
            txtName.setText(studentDTO.getName());
            txtAddress.setText(studentDTO.getAddress());
            txtContactNo.setText(studentDTO.getContactNo());
        }else {
            txtName.clear();
            txtAddress.clear();
            txtContactNo.clear();
        }
    }
}
