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
import model.entities.Receita;
import model.services.ReceitaService;

public class ReceitaListController implements Initializable, DataChangeListener {

	private ReceitaService service;

	@FXML
	private TableView<Receita> tableViewReceita;

	@FXML
	private TableColumn<Receita, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Receita, Date> tableColumnDataOriginalReceita;

	@FXML
	private TableColumn<Receita, Date> tableColumnDataConcluidaReceita;
	
	@FXML
	private TableColumn<Receita, String> tableColumnDescricao;
	
	@FXML
	private TableColumn<Receita, Integer> tableColumnCodigoCategoriaReceita;
	
	@FXML
	private TableColumn<Receita, String> tableColumnCategoriaReceita;
	
	@FXML
	private TableColumn<Receita, String> tableColumnStatusReceita;

	@FXML
	private TableColumn<Receita, Double> tableColumnValor;
	
	@FXML
	private TableColumn<Receita, String> tableColumnObs;
	
	@FXML
	private TableColumn<Receita, Receita> tableColumnEDIT;

	@FXML
	private TableColumn<Receita, Receita> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Receita> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Receita obj = new Receita();
		createDialogForm(obj, "/gui/ReceitaForm.fxml", parentStage);
	}

	public void setReceitaService(ReceitaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDataOriginalReceita.setCellValueFactory(new PropertyValueFactory<>("dataOriginalReceita"));
		Utils.formatTableColumnDate(tableColumnDataOriginalReceita, "dd/MM/yyyy");
		tableColumnDataConcluidaReceita.setCellValueFactory(new PropertyValueFactory<>("dataConcluidaReceita"));
		Utils.formatTableColumnDate(tableColumnDataConcluidaReceita, "dd/MM/yyyy");
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnCodigoCategoriaReceita.setCellValueFactory(new PropertyValueFactory<>("codigoCategoriaReceita"));
		tableColumnCategoriaReceita.setCellValueFactory(new PropertyValueFactory<>("categoriaReceita"));
		tableColumnStatusReceita.setCellValueFactory(new PropertyValueFactory<>("statusReceita"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		Utils.formatTableColumnDouble(tableColumnValor, 2);
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewReceita.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Receita> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewReceita.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Receita obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			ReceitaFormController controller = loader.getController();
//			controller.setReceita(obj);
//			controller.setReceitaService(new ReceitaService());
//			controller.subscribeDataChangeListener(this);
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Adicionar Receita");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Receita, Receita>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Receita obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ReceitaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Receita, Receita>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Receita obj, boolean empty) {
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

	private void removeEntity(Receita obj) {
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
