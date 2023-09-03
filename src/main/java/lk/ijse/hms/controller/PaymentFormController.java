package lk.ijse.hms.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.view.tm.ReservationDetailTM;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :7:32 PM
 */

public class PaymentFormController {

    @FXML
    public JFXTextField txtKeyMoney;

    @FXML
    private TableView<ReservationDetailTM> tblUnPaid;

    @FXML
    private JFXTextField txtReservationId;

    @FXML
    private JFXTextField txtStudentId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtRoomTypeId;

    @FXML
    private JFXTextField txtType;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXButton btnPay;

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void initialize(){
        btnPay.setDisable(true);
        setCellFactory();
        loadTable();
    }

    private void loadTable() {
        List<ReservationDetailTM> paidPaymentTMList = roomBO.getAllUnPaidReservation().stream().map(customDTO -> new ReservationDetailTM(
                        customDTO.getReservationId(),
                        customDTO.getStudentId(),
                        customDTO.getName(),
                        customDTO.getRoomTypeId(),
                        customDTO.getType(),
                        customDTO.getKeyMoney(),
                        "UNPAID",
                        customDTO.getDate()
        )).collect(Collectors.toList());
        ObservableList<ReservationDetailTM> unPaidPaymentTMS = FXCollections.observableArrayList(paidPaymentTMList);
        tblUnPaid.setItems(unPaidPaymentTMS);

        tblUnPaid.getSelectionModel().selectedItemProperty().addListener((observableValue, pre, curr) -> {
            if (curr!=pre || curr!=null){
                btnPay.setDisable(false);
                ReservationDetailTM tm = tblUnPaid.getSelectionModel().getSelectedItem();
                if (tm == null){
                    btnPay.setDisable(true);
                    return;
                }
                txtReservationId.setText(tm.getReservationId());
                txtStudentId.setText(tm.getStudentId());
                txtName.setText(tm.getName());
                txtRoomTypeId.setText(tm.getRoomTypeId());
                txtType.setText(tm.getType());
                txtDate.setText(String.valueOf(tm.getDate()));
                txtKeyMoney.setText(String.valueOf(tm.getKeyMoney()));
            }
        });
    }

    private void setCellFactory() {
        tblUnPaid.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tblUnPaid.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tblUnPaid.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblUnPaid.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("RoomTypeId"));
        tblUnPaid.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblUnPaid.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("keyMoney"));
        tblUnPaid.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    void btnPayOnAction(ActionEvent event) {
        if (tblUnPaid.getSelectionModel().isEmpty()){
            return;
        }
        if(roomBO.payReservationByReservationId(tblUnPaid.getSelectionModel().getSelectedItem().getReservationId(), CustomDTO.Status.PAID)){
            txtReservationId.setText(null);
            txtStudentId.setText(null);
            txtName.setText(null);
            txtRoomTypeId.setText(null);
            txtType.setText(null);
            txtDate.setText(null);
            txtKeyMoney.setText(null);
            tblUnPaid.getItems().removeAll(tblUnPaid.getSelectionModel().getSelectedItem());
            tblUnPaid.refresh();
            new Alert(Alert.AlertType.INFORMATION,"Payment Successful..!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Payment UnSuccessful..!").show();
        }
    }
}
