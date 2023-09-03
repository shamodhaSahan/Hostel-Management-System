package lk.ijse.hms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.controller.room.AddRoomFormController;
import lk.ijse.hms.controller.room.UpdateRoomFormController;
import lk.ijse.hms.view.tm.RoomTM;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/4/2023
 * Time :11:57 PM
 */

public class ManageRoomFormController {

    @FXML
    private TableView<RoomTM> tblRoom;

    @FXML
    private JFXButton btnUpdateDelete;

    @FXML
    private TextField txtSearchRoom;

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void initialize(){
        btnUpdateDelete.setDisable(true);
        setCellFactory();
        refreshTable();
    }

    public void refreshTable() {
        tblRoom.setItems(FXCollections.observableArrayList(roomBO.getAllRoom().stream().map(roomDTO -> new RoomTM(roomDTO.getRoomTypeId(), roomDTO.getType(), roomDTO.getKeyMoney(), roomDTO.getQty())).collect(Collectors.toList())));

        txtSearchRoom.textProperty().addListener((observableValue, pre, curr) ->{
            if (!Objects.equals(pre, curr)){
                tblRoom.getItems().clear();
                List<RoomTM> searchResult = roomBO.searchMembersByText(curr).stream().map(roomDTO -> new RoomTM(roomDTO.getRoomTypeId(), roomDTO.getType(), roomDTO.getKeyMoney(), roomDTO.getQty())).collect(Collectors.toList());
                tblRoom.setItems(FXCollections.observableArrayList(searchResult));
            }

        } );

        tblRoom.getSelectionModel().selectedItemProperty().addListener((observableValue, pre, curr) -> {
            btnUpdateDelete.setDisable(curr == pre && curr == null);
        });
    }

    private void setCellFactory() {
        tblRoom.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("roomTypeId"));
        tblRoom.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblRoom.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("keyMoney"));
        tblRoom.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    @FXML
    void btnAddRoomOnAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/room/AddRoomForm.fxml"));
            Parent load = fxmlLoader.load();
            AddRoomFormController addRoomFormController = fxmlLoader.getController();
            addRoomFormController.init(this);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("assets/door_plus_icon.png"));
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Room");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"forms error ..!").show();
        }
    }

    @FXML
    void btnUpdateDeleteOnAction(ActionEvent event) {
        if (tblRoom.getSelectionModel().isEmpty()){
            btnUpdateDelete.setDisable(true);
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/room/UpdateRoomForm.fxml"));
            Parent load = fxmlLoader.load();
            UpdateRoomFormController updateRoomFormController = fxmlLoader.getController();
            updateRoomFormController.init(tblRoom.getSelectionModel().getSelectedItem(), this);
            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.getIcons().add(new Image("assets/door-edit-icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update and Delete Room details");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"forms error ..!").show();
        }
    }

}
