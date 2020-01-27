package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Transferencia;
import model.services.ContaService;
import model.services.TransferenciaService;

public class TransferenciaListController implements Initializable, DataChangeListener {

	private TransferenciaService service;

	@FXML
	private TableView<Transferencia> tableViewTransferencia;

	@FXML
	private TableColumn<Transferencia, Integer> tableColumnId;

	@FXML
	private TableColumn<Transferencia, Date> tableColumnDataOriginalTransferencia;

	@FXML
	private TableColumn<Transferencia, Date> tableColumnDataConcluidaTransferencia;

	@FXML
	private TableColumn<Transferencia, String> tableColumnDescricao;

	@FXML
	private TableColumn<Transferencia, String> tableColumnContaOrigem;

	@FXML
	private TableColumn<Transferencia, String> tableColumnContaDestino;

	@FXML
	private TableColumn<Transferencia, Double> tableColumnValor;
	
	@FXML
	private TableColumn<Transferencia, Double> tableColumnCustoTransferencia;

	@FXML
	private TableColumn<Transferencia, String> tableColumnObs;

	@FXML
	private TableColumn<Transferencia, Transferencia> tableColumnEDIT;

	@FXML
	private TableColumn<Transferencia, Transferencia> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Transferencia> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Transferencia obj = new Transferencia();
		createDialogForm(obj, "/gui/TransferenciaForm.fxml", parentStage);
	}

	public void setTransferenciaService(TransferenciaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDataOriginalTransferencia.setCellValueFactory(new PropertyValueFactory<>("dataOriginalTransferencia"));
		Utils.formatTableColumnDate(tableColumnDataOriginalTransferencia, "dd/MM/yyyy");
		tableColumnDataConcluidaTransferencia.setCellValueFactory(new PropertyValueFactory<>("dataConcluidaTransferencia"));
		Utils.formatTableColumnDate(tableColumnDataConcluidaTransferencia, "dd/MM/yyyy");
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnContaDestino.setCellValueFactory(new PropertyValueFactory<>("contaDestino"));
		tableColumnContaOrigem.setCellValueFactory(new PropertyValueFactory<>("contaOrigem"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		Utils.formatTableColumnDouble(tableColumnValor, 2);
		tableColumnCustoTransferencia.setCellValueFactory(new PropertyValueFactory<>("custoTransferencia"));
		Utils.formatTableColumnDouble(tableColumnCustoTransferencia, 2);
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTransferencia.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Transferencia> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewTransferencia.setItems(obsList);
		initEditButtons();
		initRemoveButtons();


	}

	private void createDialogForm(Transferencia obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TransferenciaFormController controller = loader.getController();
			controller.setTransferencia(obj);
			controller.setServices(new TransferenciaService(), new ContaService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar Transferencia");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Transferencia, Transferencia>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Transferencia obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/TransferenciaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Transferencia, Transferencia>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Transferencia obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Transferencia obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
