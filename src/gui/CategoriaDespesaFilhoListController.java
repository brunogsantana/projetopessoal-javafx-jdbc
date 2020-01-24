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
import model.entities.CategoriaDespesaFilho;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;

public class CategoriaDespesaFilhoListController implements Initializable, DataChangeListener {

	private CategoriaDespesaFilhoService service;

	@FXML
	private TableView<CategoriaDespesaFilho> tableViewCategoriaDespesaFilho;

	@FXML
	private TableColumn<CategoriaDespesaFilho, Integer> tableColumnId;

	@FXML
	private TableColumn<CategoriaDespesaFilho, String> tableColumnCategoriaFilhoDespesa;

	@FXML
	private TableColumn<CategoriaDespesaFilho, CategoriaDespesaFilho> tableColumnEDIT;

	@FXML
	private TableColumn<CategoriaDespesaFilho, CategoriaDespesaFilho> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<CategoriaDespesaFilho> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaDespesaFilho obj = new CategoriaDespesaFilho();
		createDialogForm(obj, "/gui/CategoriaDespesaFilhoForm.fxml", parentStage);
	}

	public void setCategoriaDespesaFilhoService(CategoriaDespesaFilhoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnCategoriaFilhoDespesa.setCellValueFactory(new PropertyValueFactory<>("categoriaFilhoDespesa"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaDespesaFilho.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<CategoriaDespesaFilho> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaDespesaFilho.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(CategoriaDespesaFilho obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaDespesaFilhoFormController controller = loader.getController();
			controller.setCategoriaDespesaFilho(obj);
			controller.setServices(new CategoriaDespesaFilhoService(),new CategoriaDespesaService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar CategoriaDespesaFilho");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<CategoriaDespesaFilho, CategoriaDespesaFilho>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(CategoriaDespesaFilho obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/CategoriaDespesaFilhoForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<CategoriaDespesaFilho, CategoriaDespesaFilho>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(CategoriaDespesaFilho obj, boolean empty) {
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

	private void removeEntity(CategoriaDespesaFilho obj) {
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
