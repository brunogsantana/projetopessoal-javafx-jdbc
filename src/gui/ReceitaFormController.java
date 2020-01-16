package gui;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Receita;
import model.exceptions.ValidationException;
import model.services.ReceitaService;

public class ReceitaFormController implements Initializable {

	private Receita entity;

	private ReceitaService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	ObservableList<String> choiceBoxStatus = FXCollections.observableArrayList("Recebido", "A Receber");
	ObservableList<String> choiceBoxCategoria = FXCollections.observableArrayList("Salario", "Ferias");

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker DPDataOriginalReceita;

	@FXML
	private DatePicker DPDataConcluidaReceita;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtCodigoCategoriaReceita;

	@FXML
	private ChoiceBox<String> choiceBoxCategoriaReceita;

	@FXML
	private ChoiceBox<String> choiceBoxStatusReceita;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtObs;

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorDataOriginalReceita;

	@FXML
	private Label labelErrorDataConcluidaReceita;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Label labelErrorCodigoCategoriaReceita;

	@FXML
	private Label labelErrorCategoriaReceita;

	@FXML
	private Label labelErrorStatusReceita;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorObs;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setReceita(Receita entity) {
		this.entity = entity;
	}

	public void setReceitaService(ReceitaService service) {
		this.service = service;
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

	private Receita getFormData() {
		Receita obj = new Receita();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (DPDataOriginalReceita.getValue() == null) {
			exception.addError("dataOriginalReceita", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataOriginalReceita.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOriginalReceita(Date.from(instant));
		}

		if (DPDataConcluidaReceita.getValue() == null) {
			exception.addError("dataConcluidaReceita", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataConcluidaReceita.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataConcluidaReceita(Date.from(instant));
		}

		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("descricao", "Field can't be empty");
		}
		obj.setDescricao(txtDescricao.getText());

		if (txtCodigoCategoriaReceita.getText() == null || txtCodigoCategoriaReceita.getText().trim().equals("")) {
			exception.addError("codigoCategoriaReceita", "Field can't be empty");
		}
		obj.setCodigoCategoriaReceita(Utils.tryParseToInt(txtCodigoCategoriaReceita.getText()));

		if (choiceBoxCategoriaReceita.getValue() == null || choiceBoxCategoriaReceita.getValue().trim().equals("")) {
			exception.addError("categoriaReceita", "Field can't be empty");
		}
		obj.setCategoriaReceita(choiceBoxCategoriaReceita.getValue());

		if (choiceBoxStatusReceita.getValue() == null || choiceBoxStatusReceita.getValue().trim().equals("")) {
			exception.addError("statusReceita", "Field can't be empty");
		}
		obj.setStatusReceita(choiceBoxStatusReceita.getValue());

		if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			exception.addError("valor", "Field can't be empty");
		}
		obj.setValor(Utils.tryParseToDouble(txtValor.getText()));

		if (txtObs.getText() == null || txtObs.getText().trim().equals("")) {
			exception.addError("obs", "Field can't be empty");
		}
		obj.setObs(txtObs.getText());

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
		Constraints.setTextFieldMaxLength(txtObs, 30);
		Constraints.setTextFieldInteger(txtCodigoCategoriaReceita);
		Constraints.setTextFieldInteger(txtCodigoCategoriaReceita);
		Utils.formatDatePicker(DPDataOriginalReceita, "dd/MM/yyyy");
		Utils.formatDatePicker(DPDataConcluidaReceita, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValor);
		choiceBoxStatusReceita.setItems(choiceBoxStatus);
		choiceBoxCategoriaReceita.setItems(choiceBoxCategoria);

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));

		if (entity.getDataOriginalReceita() != null) {
			Instant instant = new java.util.Date(entity.getDataOriginalReceita().getTime()).toInstant();
			DPDataOriginalReceita.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		if (entity.getDataConcluidaReceita() != null) {
			Instant instant = new java.util.Date(entity.getDataConcluidaReceita().getTime()).toInstant();
			DPDataConcluidaReceita.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		txtDescricao.setText(entity.getDescricao());
		txtCodigoCategoriaReceita.setText(String.valueOf(entity.getCodigoCategoriaReceita()));

		if (entity.getCategoriaReceita() == null) {
			choiceBoxCategoriaReceita.getSelectionModel().selectFirst();
		} else {
			choiceBoxCategoriaReceita.setValue(entity.getCategoriaReceita());
		}

		if (entity.getStatusReceita() == null) {
			choiceBoxStatusReceita.getSelectionModel().selectFirst();
		} else {
			choiceBoxStatusReceita.setValue(entity.getStatusReceita());
		}

		txtValor.setText(String.format("%.2f",entity.getValor()));
//		txtValor.setText(String.valueOf(entity.getValor()));
		txtObs.setText(entity.getObs());

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("dataOriginalReceita")) {
			labelErrorDataOriginalReceita.setText(errors.get("dataOriginalReceita"));
		}
		if (fields.contains("dataConcluidaReceita")) {
			labelErrorDataConcluidaReceita.setText(errors.get("dataConcluidaReceita"));
		}
		if (fields.contains("descricao")) {
			labelErrorDescricao.setText(errors.get("descricao"));
		}
		if (fields.contains("codigoCategoriaReceita")) {
			labelErrorCodigoCategoriaReceita.setText(errors.get("codigoCategoriaReceita"));
		}
		if (fields.contains("categoriaReceita")) {
			labelErrorCategoriaReceita.setText(errors.get("categoriaReceita"));
		}
		if (fields.contains("statusReceita")) {
			labelErrorStatusReceita.setText(errors.get("statusReceita"));
		}
		if (fields.contains("valor")) {
			labelErrorValor.setText(errors.get("valor"));
		}
		if (fields.contains("obs")) {
			labelErrorObs.setText(errors.get("obs"));
		}

	}
}
