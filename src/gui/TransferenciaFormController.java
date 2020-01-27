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
import model.entities.Conta;
import model.entities.Transferencia;
import model.exceptions.ValidationException;
import model.services.ContaService;
import model.services.TransferenciaService;

public class TransferenciaFormController implements Initializable {

	private Transferencia entity;

	private TransferenciaService service;

	private ContaService contaService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker DPDataOriginalTransferencia;

	@FXML
	private DatePicker DPDataConcluidaTransferencia;

	@FXML
	private TextField txtDescricao;

	@FXML
	private ComboBox<Conta> comboBoxContaOrigem;

	@FXML
	private ComboBox<Conta> comboBoxContaDestino;
	
	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtObs;

	@FXML
	private TextField txtCustoTransferencia;

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorDataOriginalTransferencia;

	@FXML
	private Label labelErrorDataConcluidaTransferencia;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Label labelErrorCustoTransferencia;
	
	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorObs;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Conta> obsListContaOrigem;
	private ObservableList<Conta> obsListContaDestino;

	public void setTransferencia(Transferencia entity) {
		this.entity = entity;
	}

	public void setServices(TransferenciaService service, ContaService contaService) {
		this.service = service;
		this.contaService = contaService;
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

	private Transferencia getFormData() {
		Transferencia obj = new Transferencia();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (DPDataOriginalTransferencia.getValue() == null) {
			exception.addError("dataOriginalTransferencia", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataOriginalTransferencia.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOriginalTransferencia(Date.from(instant));
		}

		if (DPDataConcluidaTransferencia.getValue() == null) {
			exception.addError("dataConcluidaTransferencia", "Field can't be empty");
		} else {
			Instant instant = Instant
					.from(DPDataConcluidaTransferencia.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataConcluidaTransferencia(Date.from(instant));
		}

		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("descricao", "Field can't be empty");
		}
		obj.setDescricao(txtDescricao.getText());

		if (comboBoxContaOrigem.getValue() == null) {
			exception.addError("contaOrigem", "Field can't be empty");
		}
		obj.setContaOrigem(comboBoxContaOrigem.getValue());

		if (comboBoxContaDestino.getValue() == null) {
			exception.addError("contaDestino", "Field can't be empty");
		}
		obj.setContaDestino(comboBoxContaDestino.getValue());

		if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			exception.addError("valor", "Field can't be empty");
		}
		obj.setValor(Utils.tryParseToDouble(txtValor.getText()));

		if (txtCustoTransferencia.getText() == null || txtCustoTransferencia.getText().trim().equals("")) {
			exception.addError("custoTransferencia", "Field can't be empty");
		}
		obj.setCustoTransferencia(Utils.tryParseToDouble(txtCustoTransferencia.getText()));

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
		Utils.formatDatePicker(DPDataOriginalTransferencia, "dd/MM/yyyy");
		Utils.formatDatePicker(DPDataConcluidaTransferencia, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValor);
		Constraints.setTextFieldDouble(txtCustoTransferencia);
		initializeComboBoxContaDestino();
		initializeComboBoxContaOrigem();

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));

		if (entity.getDataOriginalTransferencia() != null) {
			Instant instant = new java.util.Date(entity.getDataOriginalTransferencia().getTime()).toInstant();
			DPDataOriginalTransferencia.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		if (entity.getDataConcluidaTransferencia() != null) {
			Instant instant = new java.util.Date(entity.getDataConcluidaTransferencia().getTime()).toInstant();
			DPDataConcluidaTransferencia.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		txtDescricao.setText(entity.getDescricao());

		if (entity.getContaDestino() == null) {
			comboBoxContaDestino.getSelectionModel().selectFirst();
		} else {
			comboBoxContaDestino.setValue(entity.getContaDestino());
		}

		if (entity.getContaOrigem() == null) {
			comboBoxContaOrigem.getSelectionModel().selectFirst();
		} else {
			comboBoxContaOrigem.setValue(entity.getContaOrigem());
		}

		Locale.setDefault(Locale.US);
		txtValor.setText(String.format("%.2f", entity.getValor()));
		txtCustoTransferencia.setText(String.format("%.2f", entity.getCustoTransferencia()));
		txtObs.setText(entity.getObs());
	}

	public void loadAssociatedObjects() {
		if (contaService == null) {
			throw new IllegalStateException("ContaService was null");
		}
		List<Conta> listContaOrigem = contaService.findAll();
		obsListContaOrigem = FXCollections.observableArrayList(listContaOrigem);
		comboBoxContaOrigem.setItems(obsListContaOrigem);
		
		List<Conta> listContaDestino = contaService.findAll();
		obsListContaDestino = FXCollections.observableArrayList(listContaDestino);
		comboBoxContaDestino.setItems(obsListContaDestino);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("dataOriginalTransferencia")) {
			labelErrorDataOriginalTransferencia.setText(errors.get("dataOriginalTransferencia"));
		} else {
			labelErrorDataOriginalTransferencia.setText("");
		}
		if (fields.contains("dataConcluidaTransferencia")) {
			labelErrorDataConcluidaTransferencia.setText(errors.get("dataConcluidaTransferencia"));
		} else {
			labelErrorDataConcluidaTransferencia.setText("");
		}
		if (fields.contains("descricao")) {
			labelErrorDescricao.setText(errors.get("descricao"));
		} else {
			labelErrorDescricao.setText("");
		}
		if (fields.contains("valor")) {
			labelErrorValor.setText(errors.get("valor"));
		} else {
			labelErrorValor.setText("");
		}	
		if (fields.contains("custoTransferencia")) {
			labelErrorCustoTransferencia.setText(errors.get("custoTransferencia"));
		} else {
			labelErrorCustoTransferencia.setText("");
		}
		if (fields.contains("obs")) {
			labelErrorObs.setText(errors.get("obs"));
		} else {
			labelErrorObs.setText("");
		}
	}

	private void initializeComboBoxContaOrigem() {
		Callback<ListView<Conta>, ListCell<Conta>> factory = lv -> new ListCell<Conta>() {
			@Override
			protected void updateItem(Conta item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxContaOrigem.setCellFactory(factory);
		comboBoxContaOrigem.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxContaDestino() {
		Callback<ListView<Conta>, ListCell<Conta>> factory = lv -> new ListCell<Conta>() {
			@Override
			protected void updateItem(Conta item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxContaDestino.setCellFactory(factory);
		comboBoxContaDestino.setButtonCell(factory.call(null));
	}
}
