package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.CategoriaDespesa;
import model.entities.CategoriaDespesaFilho;
import model.exceptions.ValidationException;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;

public class CategoriaDespesaFormController implements Initializable {

	private CategoriaDespesa entity;

	private CategoriaDespesaService service;

	private CategoriaDespesaFilhoService categoriaDespesaFilhoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtCategoriaPaiDespesa;

	@FXML
	private ComboBox<CategoriaDespesaFilho> comboBoxCategoriaFilhoDespesa;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Label labelErrorCategoriaPaiDespesa;

	@FXML
	private Label labelErrorCategoriaFilhoDespesa;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setCategoriaDespesa(CategoriaDespesa entity) {
		this.entity = entity;
	}

	private ObservableList<CategoriaDespesaFilho> obsListCategoriaDespesaFilhos;

	public void setServices(CategoriaDespesaService service,
			CategoriaDespesaFilhoService categoriaDespesaFilhoService) {
		this.service = service;
		this.categoriaDespesaFilhoService = categoriaDespesaFilhoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListeners.add(listener);

	}

	@FXML
	private void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {

		for (DataChangeListener listener : dataChangeListeners) {

			listener.onDataChanged();

		}

	}

	private CategoriaDespesa getFormData() {

		CategoriaDespesa obj = new CategoriaDespesa();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("descricao", "Field can't be empty");
		}
		obj.setDescricao(txtDescricao.getText());

		if (txtCategoriaPaiDespesa.getText() == null || txtCategoriaPaiDespesa.getText().trim().equals("")) {
			exception.addError("categoriaPaiDespesa", "Field can't be empty");
		}
		obj.setCatPaiDespesa(txtCategoriaPaiDespesa.getText());

		if (comboBoxCategoriaFilhoDespesa.getValue() == null) {
			exception.addError("categoriaFilhoDespesa", "Field can't be empty");
		}
		obj.setCatFilhoDespesa(comboBoxCategoriaFilhoDespesa.getValue());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;

	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtDescricao, 30);
		Constraints.setTextFieldMaxLength(txtCategoriaPaiDespesa, 30);
		initializecomboBoxCategoriaFilhoDespesa();
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtDescricao.setText(entity.getDescricao());
		txtCategoriaPaiDespesa.setText(entity.getCatPaiDespesa());

		if (entity.getCatFilhoDespesa() == null) {
			comboBoxCategoriaFilhoDespesa.getSelectionModel().selectFirst();
		} else {
			comboBoxCategoriaFilhoDespesa.setValue(entity.getCatFilhoDespesa());
		}
	}

	public void loadAssociatedObjects() {
		if (categoriaDespesaFilhoService == null) {
			throw new IllegalStateException("CategoriaDespesaFilhoService was null");
		}
		List<CategoriaDespesaFilho> listCatDespesaFilhos = categoriaDespesaFilhoService.findAll();
		obsListCategoriaDespesaFilhos = FXCollections.observableArrayList(listCatDespesaFilhos);
		comboBoxCategoriaFilhoDespesa.setItems(obsListCategoriaDespesaFilhos);

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("descricao")) {
			labelErrorDescricao.setText(errors.get("descricao"));
		} else {
			labelErrorDescricao.setText("");
		}

		if (fields.contains("categoriaPaiDespesa")) {
			labelErrorCategoriaPaiDespesa.setText(errors.get("categoriaPaiDespesa"));
		} else {
			labelErrorCategoriaPaiDespesa.setText("");
		}

		if (fields.contains("categoriaFilhoDespesa")) {
			labelErrorCategoriaFilhoDespesa.setText(errors.get("categoriaFilhoDespesa"));
		} else {
			labelErrorCategoriaFilhoDespesa.setText("");
		}
	}

	private void initializecomboBoxCategoriaFilhoDespesa() {
		Callback<ListView<CategoriaDespesaFilho>, ListCell<CategoriaDespesaFilho>> factory = lv -> new ListCell<CategoriaDespesaFilho>() {
			@Override
			protected void updateItem(CategoriaDespesaFilho item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCategoriaFilhoDespesa());
			}
		};

		comboBoxCategoriaFilhoDespesa.setCellFactory(factory);
		comboBoxCategoriaFilhoDespesa.setButtonCell(factory.call(null));
	}
}