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
import model.entities.Conta;
import model.entities.enums.TipoConta;
import model.services.ContaService;

public class ContaListController implements Initializable, DataChangeListener {

	private ContaService service;

	@FXML
	private TableView<Conta> tableViewConta;

	@FXML
	private TableColumn<Conta, Integer> tableColumnId;

	@FXML
	private TableColumn<Conta, String> tableColumnName;

	@FXML
	private TableColumn<Conta, Integer> tableColumnCPF;

	@FXML
	private TableColumn<Conta, TipoConta> tableColumnTipoConta;

	@FXML
	private TableColumn<Conta, String> tableColumnBanco;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroBanco;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroConta;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroAgencia;

	@FXML
	private TableColumn<Conta, Date> tableColumnDataCadastro;

	@FXML
	private TableColumn<Conta, Double> tableColumnSaldoAtual;
	
	@FXML
	private TableColumn<Conta, Double> tableColumnSaldoInicial;

	@FXML
	private TableColumn<Conta, Boolean> tableColumnFavorita;

	@FXML
	private TableColumn<Conta, Conta> tableColumnEDIT;

	@FXML
	private TableColumn<Conta, Conta> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Conta> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Conta obj = new Conta();
		createDialogForm(obj, "/gui/ContaForm.fxml", parentStage);
	}

	public void setContaService(ContaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnTipoConta.setCellValueFactory(new PropertyValueFactory<>("tipoConta"));
		tableColumnBanco.setCellValueFactory(new PropertyValueFactory<>("banco"));
		tableColumnNumeroBanco.setCellValueFactory(new PropertyValueFactory<>("numeroBanco"));
		tableColumnNumeroConta.setCellValueFactory(new PropertyValueFactory<>("numeroConta"));
		tableColumnNumeroAgencia.setCellValueFactory(new PropertyValueFactory<>("numeroAgencia"));
		tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
		tableColumnSaldoInicial.setCellValueFactory(new PropertyValueFactory<>("saldoInicial"));
		tableColumnSaldoAtual.setCellValueFactory(new PropertyValueFactory<>("saldoAtual"));
		tableColumnFavorita.setCellValueFactory(new PropertyValueFactory<>("favorita"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewConta.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Conta> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewConta.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Conta obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ContaFormController controller = loader.getController();
			controller.setConta(obj);
			controller.setContaService(new ContaService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar Conta");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Conta, Conta>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Conta obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ContaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Conta, Conta>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Conta obj, boolean empty) {
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

	private void removeEntity(Conta obj) {
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
