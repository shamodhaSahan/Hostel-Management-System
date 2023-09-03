package lk.ijse.hms.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.view.tm.RoomTM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :7:35 PM
 */

public class DashboardFormController {

    @FXML
    public TableView<RoomTM> tblRoom;
    @FXML
    private Label lblTotalStudent;

    @FXML
    private Label lblTotalReservation;

    @FXML
    private Label lblTotalNonPaidReservation;

    @FXML
    private BarChart barChart;

    private final StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);

    private final RoomBO roomBO = BoFactory.getInstance().getBO(BoTypes.ROOM);

    public void initialize(){
        loadLabel();
        loadBarChart();
        loadTable();
    }

    private void loadTable() {
        tblRoom.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("roomTypeId"));
        tblRoom.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblRoom.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("keyMoney"));
        tblRoom.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblRoom.setItems(FXCollections.observableArrayList(roomBO.getAllRoom().stream().map(roomDTO -> new RoomTM(roomDTO.getRoomTypeId(), roomDTO.getType(), roomDTO.getKeyMoney(), roomDTO.getQty())).collect(Collectors.toList())));
    }


    private void loadBarChart() {
        XYChart.Series<String, Long> series = new XYChart.Series();
        for (int i=1;i<16;i++) {
            LocalDate date = calculateDate(-(15-i));
            long count = roomBO.getAllReservationCountByDate(date);
            series.getData().add(new XYChart.Data<>(date.toString(), count));
        }
        barChart.getData().addAll(series);
    }

    private LocalDate calculateDate(int n) {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,n);
        String result=dateFormat.format(calendar.getTime());
        return LocalDate.parse(result);
    }

    private void loadLabel() {
        lblTotalStudent.setText(String.valueOf(studentBO.getAllStudent().size()));
        lblTotalReservation.setText(String.valueOf(roomBO.getAllReservation().size()));
        lblTotalNonPaidReservation.setText(String.valueOf(roomBO.getAllUnPaidReservation().size()));
    }
}

