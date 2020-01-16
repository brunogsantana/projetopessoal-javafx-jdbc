package gui;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Lancamento;
import model.entities.enums.TipoLancamento;
import model.services.LancamentoService;

public class LancamentoListController implements Initializable, DataChangeListener {

	private LancamentoService service;

	@FXML
	private TableView<Lancamento> tableViewLancamento;

	@FXML
	private TableColumn<Lancamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Lancamento, String> tableColumnName;

	@FXML
	private TableColumn<Lancamento, Integer> tableColumnCPF;

	@FXML
	private TableColumn<Lancamento, TipoLancamento> tableColumnTipoLancamento;

	@FXML
	private TableColumn<Lancamento, String> tableColumnBanco;

	@FXML
	private TableColumn<Lancamento, Integer> tableColumnNumeroBanco;

	@FXML
	private TableColumn<Lancamento, Integer> tableColumnNumeroLancamento;

	@FXML
	private TableColumn<Lancamento, Integer> tableColumnNumeroAgencia;

	@FXML
	private TableColumn<Lancamento, Date> tableColumnDataCadastro;

	@FXML
	private TableColumn<Lancamento, Double> tableColumnSaldoAtual;
	
	@FXML
	private TableColumn<Lancamento, Double> tableColumnSaldoInicial;

	@FXML
	private TableColumn<Lancamento, Boolean> tableColumnFavorita;

	@FXML
	private TableColumn<Lancamento, Lancamento> tableColumnEDIT;

	@FXML
	private TableColumn<Lancamento, Lancamento> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Lancamento> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Lancamento obj = new Lancamento();
		createDialogForm(obj, "/gui/LancamentoForm.fxml", parentStage);
	}

	public void setLancamentoService(LancamentoService service) {
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
		tableColumnTipoLancamento.setCellValueFactory(new PropertyValueFactory<>("tipoLancamento"));
		tableColumnBanco.setCellValueFactory(new PropertyValueFactory<>("banco"));
		tableColumnNumeroBanco.setCellValueFactory(new PropertyValueFactory<>("numeroBanco"));
		tableColumnNumeroLancamento.setCellValueFactory(new PropertyValueFactory<>("numeroLancamento"));
		tableColumnNumeroAgencia.setCellValueFactory(new PropertyValueFactory<>("numeroAgencia"));
		tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
		tableColumnSaldoInicial.setCellValueFactory(new PropertyValueFactory<>("saldoInicial"));
		tableColumnSaldoAtual.setCellValueFactory(new PropertyValueFactory<>("saldoAtual"));
		tableColumnFavorita.setCellValueFactory(new PropertyValueFactory<>("favorita"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewLancamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Lancamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewLancamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Lancamento obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			LancamentoFormController controller = loader.getController();
//			controller.setLancamento(obj);
//			controller.setLancamentoService(new LancamentoService());
//			controller.subscribeDataChangeListener(this);
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Adicionar Lancamento");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Lancamento, Lancamento>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Lancamento obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/LancamentoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Lancamento, Lancamento>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Lancamento obj, boolean empty) {
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

	private void removeEntity(Lancamento obj) {
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
