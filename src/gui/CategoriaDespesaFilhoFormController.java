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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.CategoriaDespesa;
import model.entities.CategoriaDespesaFilho;
import model.exceptions.ValidationException;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;

public class CategoriaDespesaFilhoFormController implements Initializable {

	private CategoriaDespesaFilho entity;

	private CategoriaDespesaFilhoService service;
	
	private CategoriaDespesaService categoriaDespesaService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtCategoriaFilhoDespesa;
	
	@FXML
	private ComboBox<CategoriaDespesa> comboBoxCategoriaDespesa;

	@FXML
	private Label labelErrorCategoriaFilhoDespesa;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setCategoriaDespesaFilho(CategoriaDespesaFilho entity) {

		this.entity = entity;

	}

	private ObservableList<CategoriaDespesa> obsListCategoriaDespesa;

	public void setServices(CategoriaDespesaFilhoService service,
			CategoriaDespesaService categoriaDespesaService) {
		this.service = service;
		this.categoriaDespesaService = categoriaDespesaService;
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

	private CategoriaDespesaFilho getFormData() {

		CategoriaDespesaFilho obj = new CategoriaDespesaFilho();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
	
		if (txtCategoriaFilhoDespesa.getText() == null || txtCategoriaFilhoDespesa.getText().trim().equals("")) {
			exception.addError("categoriaFilhoDespesa", "Field can't be empty");
		}
		obj.setCategoriaFilhoDespesa(txtCategoriaFilhoDespesa.getText());

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
		Constraints.setTextFieldMaxLength(txtCategoriaFilhoDespesa, 30);
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtCategoriaFilhoDespesa.setText(entity.getCategoriaFilhoDespesa());
	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("categoriaFilhoDespesa")) {
			labelErrorCategoriaFilhoDespesa.setText(errors.get("categoriaFilhoDespesa"));
		} else {
			labelErrorCategoriaFilhoDespesa.setText("");
		}
	}
}