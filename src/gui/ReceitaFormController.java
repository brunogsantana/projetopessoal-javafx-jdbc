package gui;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.CategoriaReceita;
import model.entities.Conta;
import model.entities.Receita;
import model.exceptions.ValidationException;
import model.services.CategoriaReceitaService;
import model.services.ContaService;
import model.services.ReceitaService;

public class ReceitaFormController implements Initializable {

	private Receita entity;

	private ReceitaService service;

	private ContaService contaService;

	private CategoriaReceitaService categoriaReceitaService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	ObservableList<String> comboBoxStatus = FXCollections.observableArrayList("Recebido", "A Receber");

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker DPDataOriginalReceita;

	@FXML
	private DatePicker DPDataConcluidaReceita;

	@FXML
	private TextField txtDescricao;

	@FXML
	private ComboBox<CategoriaReceita> comboBoxCategoriaReceita;

	@FXML
	private ComboBox<String> comboBoxStatusReceita;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtObs;

	@FXML
	private ComboBox<Conta> comboBoxConta;

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorDataOriginalReceita;

	@FXML
	private Label labelErrorDataConcluidaReceita;

	@FXML
	private Label labelErrorDescricao;

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

	private ObservableList<Conta> obsListConta;

	private ObservableList<CategoriaReceita> obsListCatReceita;

	public void setReceita(Receita entity) {
		this.entity = entity;
	}

	public void setServices(ReceitaService service, ContaService contaService,
			CategoriaReceitaService categoriaReceitaService) {
		this.service = service;
		this.contaService = contaService;
		this.categoriaReceitaService = categoriaReceitaService;
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

		if (comboBoxCategoriaReceita.getValue() == null) {
			exception.addError("categoriaReceita", "Field can't be empty");
		}
		obj.setCategoriaReceita(comboBoxCategoriaReceita.getValue());

		if (comboBoxStatusReceita.getValue() == null || comboBoxStatusReceita.getValue().trim().equals("")) {
			exception.addError("statusReceita", "Field can't be empty");
		}
		obj.setStatusReceita(comboBoxStatusReceita.getValue());

		if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			exception.addError("valor", "Field can't be empty");
		}
		obj.setValor(Utils.tryParseToDouble(txtValor.getText()));

		if (txtObs.getText() == null || txtObs.getText().trim().equals("")) {
			exception.addError("obs", "Field can't be empty");
		}
		obj.setObs(txtObs.getText());
		obj.setConta(comboBoxConta.getValue());

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
		Utils.formatDatePicker(DPDataOriginalReceita, "dd/MM/yyyy");
		Utils.formatDatePicker(DPDataConcluidaReceita, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValor);
		comboBoxStatusReceita.setItems(comboBoxStatus);
		initializecomboBoxCategoriaReceita();
		initializeComboBoxConta();

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

		if (entity.getCategoriaReceita() == null) {
			comboBoxCategoriaReceita.getSelectionModel().selectFirst();
		} else {
			comboBoxCategoriaReceita.setValue(entity.getCategoriaReceita());
		}

		if (entity.getStatusReceita() == null) {
			comboBoxStatusReceita.getSelectionModel().selectFirst();
		} else {
			comboBoxStatusReceita.setValue(entity.getStatusReceita());
		}

		Locale.setDefault(Locale.US);
		txtValor.setText(String.format("%.2f", entity.getValor()));
		txtObs.setText(entity.getObs());

		if (entity.getConta() == null) {
			comboBoxConta.getSelectionModel().selectFirst();
		} else {
			comboBoxConta.setValue(entity.getConta());
		}
	}

	public void loadAssociatedObjects() {
		if (contaService == null) {
			throw new IllegalStateException("ContaService was null");
		}
		List<Conta> listConta = contaService.findAll();
		obsListConta = FXCollections.observableArrayList(listConta);
		comboBoxConta.setItems(obsListConta);

		if (categoriaReceitaService == null) {
			throw new IllegalStateException("CategoriaReceitaService was null");
		}
		List<CategoriaReceita> listCatReceita = categoriaReceitaService.findAll();
		obsListCatReceita = FXCollections.observableArrayList(listCatReceita);
		comboBoxCategoriaReceita.setItems(obsListCatReceita);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("dataOriginalReceita")) {
			labelErrorDataOriginalReceita.setText(errors.get("dataOriginalReceita"));
		} else {
			labelErrorDataOriginalReceita.setText("");
		}
		if (fields.contains("dataConcluidaReceita")) {
			labelErrorDataConcluidaReceita.setText(errors.get("dataConcluidaReceita"));
		} else {
			labelErrorDataConcluidaReceita.setText("");
		}
		if (fields.contains("descricao")) {
			labelErrorDescricao.setText(errors.get("descricao"));
		} else {
			labelErrorDescricao.setText("");
		}
		if (fields.contains("categoriaReceita")) {
			labelErrorCategoriaReceita.setText(errors.get("categoriaReceita"));
		} else {
			labelErrorCategoriaReceita.setText("");
		}
		if (fields.contains("statusReceita")) {
			labelErrorStatusReceita.setText(errors.get("statusReceita"));
		} else {
			labelErrorStatusReceita.setText("");
		}
		if (fields.contains("valor")) {
			labelErrorValor.setText(errors.get("valor"));
		} else {
			labelErrorValor.setText("");
		}
		if (fields.contains("obs")) {
			labelErrorObs.setText(errors.get("obs"));
		} else {
			labelErrorObs.setText("");
		}
	}

	private void initializeComboBoxConta() {
		Callback<ListView<Conta>, ListCell<Conta>> factory = lv -> new ListCell<Conta>() {
			@Override
			protected void updateItem(Conta item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxConta.setCellFactory(factory);
		comboBoxConta.setButtonCell(factory.call(null));
	}

	private void initializecomboBoxCategoriaReceita() {
		Callback<ListView<CategoriaReceita>, ListCell<CategoriaReceita>> factory = lv -> new ListCell<CategoriaReceita>() {
			@Override
			protected void updateItem(CategoriaReceita item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCatReceita());
			}
		};

		comboBoxCategoriaReceita.setCellFactory(factory);
		comboBoxCategoriaReceita.setButtonCell(factory.call(null));
	}

}
