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
import model.entities.Despesa;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;
import model.services.ContaService;
import model.services.DespesaService;

public class DespesaListController implements Initializable, DataChangeListener {

	private DespesaService service;

	@FXML
	private TableView<Despesa> tableViewDespesa;

	@FXML
	private TableColumn<Despesa, Integer> tableColumnId;

	@FXML
	private TableColumn<Despesa, Date> tableColumnDataOriginalDespesa;

	@FXML
	private TableColumn<Despesa, Date> tableColumnDataConcluidaDespesa;

	@FXML
	private TableColumn<Despesa, Date> tableColumnDataPagamentoParcela;

	@FXML
	private TableColumn<Despesa, String> tableColumnDescricao;

	@FXML
	private TableColumn<Despesa, Integer> tableColumnCodigoCategoriaDespesa;

	@FXML
	private TableColumn<Despesa, String> tableColumnCategoriaPaiDespesa;

	@FXML
	private TableColumn<Despesa, String> tableColumnCategoriaFilhoDespesa;

	@FXML
	private TableColumn<Despesa, String> tableColumnStatusDespesa;

	@FXML
	private TableColumn<Despesa, String> tableColumnMeioPagamento;

	@FXML
	private TableColumn<Despesa, Integer> tableColumnQtdParcela;

	@FXML
	private TableColumn<Despesa, Double> tableColumnValor;

	@FXML
	private TableColumn<Despesa, Double> tableColumnValorParcela;

	@FXML
	private TableColumn<Despesa, String> tableColumnObs;

	@FXML
	private TableColumn<Despesa, String> tableColumnContaId;

	@FXML
	private TableColumn<Despesa, Despesa> tableColumnEDIT;

	@FXML
	private TableColumn<Despesa, Despesa> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Despesa> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Despesa obj = new Despesa();
		createDialogForm(obj, "/gui/DespesaForm.fxml", parentStage);
	}

	public void setDespesaService(DespesaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDataOriginalDespesa.setCellValueFactory(new PropertyValueFactory<>("dataOriginalDespesa"));
		Utils.formatTableColumnDate(tableColumnDataOriginalDespesa, "dd/MM/yyyy");
		tableColumnDataConcluidaDespesa.setCellValueFactory(new PropertyValueFactory<>("dataConcluidaDespesa"));
		Utils.formatTableColumnDate(tableColumnDataConcluidaDespesa, "dd/MM/yyyy");
		tableColumnDataPagamentoParcela.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoParcela"));
		Utils.formatTableColumnDate(tableColumnDataPagamentoParcela, "dd/MM/yyyy");
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnCategoriaPaiDespesa.setCellValueFactory(new PropertyValueFactory<>("categoriaDespesaPai"));
		tableColumnCategoriaFilhoDespesa.setCellValueFactory(new PropertyValueFactory<>("categoriaDespesaFilho"));
		tableColumnStatusDespesa.setCellValueFactory(new PropertyValueFactory<>("statusDespesa"));
		tableColumnMeioPagamento.setCellValueFactory(new PropertyValueFactory<>("meioPagamento"));
		tableColumnQtdParcela.setCellValueFactory(new PropertyValueFactory<>("qtdParcela"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		Utils.formatTableColumnDouble(tableColumnValor, 2);
		tableColumnValorParcela.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));
		Utils.formatTableColumnDouble(tableColumnValorParcela, 2);
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		tableColumnContaId.setCellValueFactory(new PropertyValueFactory<>("conta"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDespesa.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Despesa> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDespesa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Despesa obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DespesaFormController controller = loader.getController();
			controller.setDespesa(obj);
			controller.setServices(new DespesaService(), new ContaService(), new CategoriaDespesaService(), new CategoriaDespesaFilhoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar Despesa");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Despesa, Despesa>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Despesa obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/DespesaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Despesa, Despesa>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Despesa obj, boolean empty) {
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

	private void removeEntity(Despesa obj) {
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
