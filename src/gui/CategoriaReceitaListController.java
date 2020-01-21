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
import model.entities.CategoriaReceita;
import model.services.CategoriaReceitaService;

public class CategoriaReceitaListController implements Initializable, DataChangeListener {

	private CategoriaReceitaService service;

	@FXML
	private TableView<CategoriaReceita> tableViewCategoriaReceita;

	@FXML
	private TableColumn<CategoriaReceita, Integer> tableColumnId;

	@FXML
	private TableColumn<CategoriaReceita, String> tableColumnDescricao;

	@FXML
	private TableColumn<CategoriaReceita, String> tableColumnCategoriaReceita;

	@FXML
	private TableColumn<CategoriaReceita, CategoriaReceita> tableColumnEDIT;

	@FXML
	private TableColumn<CategoriaReceita, CategoriaReceita> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<CategoriaReceita> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaReceita obj = new CategoriaReceita();
		createDialogForm(obj, "/gui/CategoriaReceitaForm.fxml", parentStage);
	}

	public void setCategoriaReceitaService(CategoriaReceitaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnCategoriaReceita.setCellValueFactory(new PropertyValueFactory<>("catReceita"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaReceita.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<CategoriaReceita> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaReceita.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(CategoriaReceita obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaReceitaFormController controller = loader.getController();
			controller.setCategoriaReceita(obj);
			controller.setCategoriaReceitaService(new CategoriaReceitaService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Adicionar CategoriaReceita");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<CategoriaReceita, CategoriaReceita>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(CategoriaReceita obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CategoriaReceitaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<CategoriaReceita, CategoriaReceita>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(CategoriaReceita obj, boolean empty) {
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

	private void removeEntity(CategoriaReceita obj) {
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
