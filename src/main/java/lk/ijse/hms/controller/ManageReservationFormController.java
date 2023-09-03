package lk.ijse.hms.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.controller.reservation.AddReservationFormController;
import lk.ijse.hms.view.tm.ReservationDetailTM;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/4/2023
 * Time :11:57 PM
 */

public class ManageReservationFormController {

    @FXML
    private TableView<ReservationDetailTM> tblReservation;

    @FXML
    private TextField txtSearch;

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void initialize(){
        setCellFactory();
        refreshTable();
    }

    public void refreshTable() {
        tblReservation.setItems(FXCollections.observableArrayList(roomBO.getAllReservation().stream().map(
                customDTO -> new ReservationDetailTM(
                        customDTO.getReservationId(),
                        customDTO.getStudentId(),
                        customDTO.getName(),
                        customDTO.getRoomTypeId(),
                        customDTO.getType(),
                        customDTO.getKeyMoney(),
                        customDTO.getStatus().toString(),
                        customDTO.getDate()
                )
        ).collect(Collectors.toList())));

        txtSearch.textProperty().addListener((observableValue, pre, text) ->{
            if (!Objects.equals(pre, text)){
                tblReservation.getItems().clear();
                List<ReservationDetailTM> bookList = roomBO.searchBookByText(text).stream().map(
                        customDTO -> new ReservationDetailTM(
                                customDTO.getReservationId(),
                                customDTO.getStudentId(),
                                customDTO.getName(),
                                customDTO.getRoomTypeId(),
                                customDTO.getType(),
                                customDTO.getKeyMoney(),
                                customDTO.getStatus().toString(),
                                customDTO.getDate()
                        )
                ).collect(Collectors.toList());
                tblReservation.setItems(FXCollections.observableArrayList(bookList));
            }
        } );
    }

    @FXML
    void btnAddNewReservation(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/reservation/AddReservationForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent load = fxmlLoader.load();
            AddReservationFormController addReservationFormController = fxmlLoader.getController();
            addReservationFormController.init(this);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("assets/reservation.png"));
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Reservation Registration");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellFactory() {
        tblReservation.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tblReservation.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tblReservation.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblReservation.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("roomTypeId"));
        tblReservation.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblReservation.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("keyMoney"));
        tblReservation.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));
        tblReservation.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("date"));
    }
}
