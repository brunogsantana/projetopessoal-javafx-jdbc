package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.CategoriaDespesa;
import model.entities.Receita;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;
import model.services.CategoriaReceitaService;
import model.services.ContaService;
import model.services.ReceitaService;

public class CategoriaDespesaListController implements Initializable, DataChangeListener {

	private CategoriaDespesaService service;

	@FXML
	private TableView<CategoriaDespesa> tableViewCategoriaDespesa;

	@FXML
	private TableColumn<CategoriaDespesa, Integer> tableColumnId;

	@FXML
	private TableColumn<CategoriaDespesa, String> tableColumnDescricao;

	@FXML
	private TableColumn<CategoriaDespesa, String> tableColumnCategoriaPaiDespesa;
	
	@FXML
	private TableColumn<CategoriaDespesa, String> tableColumnCategoriaFilhoDespesa;

	@FXML
	private TableColumn<CategoriaDespesa, CategoriaDespesa> tableColumnEDIT;

	@FXML
	private TableColumn<CategoriaDespesa, CategoriaDespesa> tableColumnREMOVE;

	@FXML
	private Button btNew;
	
	private ObservableList<CategoriaDespesa> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaDespesa obj = new CategoriaDespesa();
		createDialogForm(obj, "/gui/CategoriaDespesaForm.fxml", parentStage);
	}
	
	public void setCategoriaDespesaService(CategoriaDespesaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnCategoriaPaiDespesa.setCellValueFactory(new PropertyValueFactory<>("catPaiDespesa"));
		tableColumnCategoriaFilhoDespesa.setCellValueFactory(new PropertyValueFactory<>("catFilhoDespesa"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaDespesa.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<CategoriaDespesa> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaDespesa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
			
	}

	private void createDialogForm(CategoriaDespesa obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaDespesaFormController controller = loader.getController();
			controller.setCategoriaDespesa(obj);
			controller.setServices(new CategoriaDespesaService(), new CategoriaDespesaFilhoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar CategoriaDespesa");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<CategoriaDespesa, CategoriaDespesa>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(CategoriaDespesa obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CategoriaDespesaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<CategoriaDespesa, CategoriaDespesa>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(CategoriaDespesa obj, boolean empty) {
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

	private void removeEntity(CategoriaDespesa obj) {
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
