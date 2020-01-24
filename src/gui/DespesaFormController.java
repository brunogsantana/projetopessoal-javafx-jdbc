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
import javafx.beans.binding.ListBinding;
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
import model.entities.CategoriaDespesa;
import model.entities.CategoriaDespesaFilho;
import model.entities.Conta;
import model.entities.Despesa;
import model.exceptions.ValidationException;
import model.services.CategoriaDespesaFilhoService;
import model.services.CategoriaDespesaService;
import model.services.ContaService;
import model.services.DespesaService;

public class DespesaFormController implements Initializable {

	private Despesa entity;

	private DespesaService service;

	private ContaService contaService;

	private CategoriaDespesaService categoriaDespesaService;
	private CategoriaDespesaFilhoService categoriaDespesaFilhoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	ObservableList<String> comboBoxStatus = FXCollections.observableArrayList("Pago", "A Pagar");

	ObservableList<String> comboBoxMeioPag = FXCollections.observableArrayList("Dinheiro", "Debito", "Credito",
			"Ticket", "Cheque", "Outros");

	ObservableList<Integer> comboBoxParcela = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker DPDataOriginalDespesa;

	@FXML
	private DatePicker DPDataConcluidaDespesa;

	@FXML
	private DatePicker DPDataPagamentoParcela;

	@FXML
	private TextField txtDescricao;

	@FXML
	private ComboBox<CategoriaDespesa> comboBoxCategoriaPaiDespesa;
	
	@FXML
	private ComboBox<CategoriaDespesa> comboBoxCategoriaFilhoDespesa1;

	@FXML
	private ComboBox<CategoriaDespesaFilho> comboBoxCategoriaFilhoDespesa;

	@FXML
	private ComboBox<String> comboBoxStatusDespesa;

	@FXML
	private ComboBox<String> comboBoxMeioPagamento;

	@FXML
	private ComboBox<Integer> comboBoxQtdParcela;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtValorParcela;

	@FXML
	private TextField txtObs;

	@FXML
	private ComboBox<Conta> comboBoxConta;

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorDataOriginalDespesa;

	@FXML
	private Label labelErrorDataConcluidaDespesa;

	@FXML
	private Label labelErrorDataPagamentoParcela;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Label labelErrorCategoriaPaiDespesa;

	@FXML
	private Label labelErrorCategoriaFilhoDespesa;

	@FXML
	private Label labelErrorStatusDespesa;

	@FXML
	private Label labelErrorMeioPagamento;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorQtdParcela;

	@FXML
	private Label labelErrorValorParcela;

	@FXML
	private Label labelErrorObs;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Conta> obsListConta;
	private ObservableList<CategoriaDespesa> obsListCatDespesa;
	private ObservableList<CategoriaDespesa> obsListCatDespesa1;
	private ObservableList<CategoriaDespesaFilho> obsListCatDespesaFilho;

	public void setDespesa(Despesa entity) {
		this.entity = entity;
	}

	public void setServices(DespesaService service, ContaService contaService,
			CategoriaDespesaService categoriaDespesaService,
			CategoriaDespesaFilhoService categoriaDespesaFilhoService) {
		this.service = service;
		this.contaService = contaService;
		this.categoriaDespesaService = categoriaDespesaService;
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

	private Despesa getFormData() {
		Despesa obj = new Despesa();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (DPDataOriginalDespesa.getValue() == null) {
			exception.addError("dataOriginalDespesa", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataOriginalDespesa.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOriginalDespesa(Date.from(instant));
		}

		if (DPDataConcluidaDespesa.getValue() == null) {
			exception.addError("dataConcluidaDespesa", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataConcluidaDespesa.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataConcluidaDespesa(Date.from(instant));
		}

		if (DPDataPagamentoParcela.getValue() == null) {
			exception.addError("dataPagamentoParcela", "Field can't be empty");
		} else {
			Instant instant = Instant.from(DPDataPagamentoParcela.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPagamentoParcela(Date.from(instant));
		}

		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("descricao", "Field can't be empty");
		}
		obj.setDescricao(txtDescricao.getText());

		if (comboBoxCategoriaPaiDespesa.getValue() == null) {
			exception.addError("categoriaDespesaPai", "Field can't be empty");
		}
		obj.setCategoriaDespesaPai(comboBoxCategoriaPaiDespesa.getValue());

		if (comboBoxCategoriaFilhoDespesa.getValue() == null) {
			exception.addError("categoriaDespesaFilho", "Field can't be empty");
		}
		obj.setCategoriaDespesaFilho(comboBoxCategoriaFilhoDespesa.getValue());

		if (comboBoxStatusDespesa.getValue() == null || comboBoxStatusDespesa.getValue().trim().equals("")) {
			exception.addError("statusDespesa", "Field can't be empty");
		}
		obj.setStatusDespesa(comboBoxStatusDespesa.getValue());

		if (comboBoxMeioPagamento.getValue() == null || comboBoxMeioPagamento.getValue().trim().equals("")) {
			exception.addError("meioPagamento", "Field can't be empty");
		}
		obj.setMeioPagamento(comboBoxMeioPagamento.getValue());

		if (comboBoxQtdParcela.getValue() == null) {
			exception.addError("qtdParcela", "Field can't be empty");
		}
		obj.setQtdParcela(comboBoxQtdParcela.getValue());

		if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			exception.addError("valor", "Field can't be empty");
		}
		obj.setValor(Utils.tryParseToDouble(txtValor.getText()));

		if (txtValorParcela.getText() == null || txtValorParcela.getText().trim().equals("")) {
			exception.addError("valorParcela", "Field can't be empty");
		}
		obj.setValorParcela(Utils.tryParseToDouble(txtValorParcela.getText()));

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
		Utils.formatDatePicker(DPDataOriginalDespesa, "dd/MM/yyyy");
		Utils.formatDatePicker(DPDataConcluidaDespesa, "dd/MM/yyyy");
		Utils.formatDatePicker(DPDataPagamentoParcela, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValor);
		Constraints.setTextFieldDouble(txtValorParcela);
		comboBoxStatusDespesa.setItems(comboBoxStatus);
		comboBoxMeioPagamento.setItems(comboBoxMeioPag);
		comboBoxQtdParcela.setItems(comboBoxParcela);
		initializecomboBoxCategoriaPaiDespesa();
		initializecomboBoxCategoriaFilhoDespesa();
		initializeComboBoxConta();

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));

		if (entity.getDataOriginalDespesa() != null) {
			Instant instant = new java.util.Date(entity.getDataOriginalDespesa().getTime()).toInstant();
			DPDataOriginalDespesa.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		if (entity.getDataConcluidaDespesa() != null) {
			Instant instant = new java.util.Date(entity.getDataConcluidaDespesa().getTime()).toInstant();
			DPDataConcluidaDespesa.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		if (entity.getDataPagamentoParcela() != null) {
			Instant instant = new java.util.Date(entity.getDataPagamentoParcela().getTime()).toInstant();
			DPDataPagamentoParcela.setValue(instant.atZone(ZoneId.systemDefault()).toLocalDate());
		}

		txtDescricao.setText(entity.getDescricao());

		if (entity.getCategoriaDespesaPai() == null) {
			comboBoxCategoriaPaiDespesa.getSelectionModel().selectFirst();
		} else {
			comboBoxCategoriaPaiDespesa.setValue(entity.getCategoriaDespesaPai());
		}

		if (entity.getCategoriaDespesaFilho() == null) {
			comboBoxCategoriaFilhoDespesa.getSelectionModel().selectFirst();
		} else {
			comboBoxCategoriaFilhoDespesa.setValue(entity.getCategoriaDespesaPai().getCatFilhoDespesa());
		}

		if (entity.getStatusDespesa() == null) {
			comboBoxStatusDespesa.getSelectionModel().selectFirst();
		} else {
			comboBoxStatusDespesa.setValue(entity.getStatusDespesa());
		}

		if (entity.getMeioPagamento() == null) {
			comboBoxMeioPagamento.getSelectionModel().selectFirst();
		} else {
			comboBoxMeioPagamento.setValue(entity.getMeioPagamento());
		}

		if (entity.getQtdParcela() == null) {
			comboBoxQtdParcela.getSelectionModel().selectFirst();
		} else {
			comboBoxQtdParcela.setValue(entity.getQtdParcela());
		}

		Locale.setDefault(Locale.US);
		txtValor.setText(String.format("%.2f", entity.getValor()));
		txtValorParcela.setText(String.format("%.2f", entity.getValorParcela()));
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

		if (categoriaDespesaService == null) {
			throw new IllegalStateException("CategoriaDespesaService was null");
		}
		List<CategoriaDespesa> listCatDespesa = categoriaDespesaService.findAll();
		obsListCatDespesa = FXCollections.observableArrayList(listCatDespesa);
		comboBoxCategoriaPaiDespesa.setItems(obsListCatDespesa);

		if (categoriaDespesaFilhoService == null) {
			throw new IllegalStateException("CategoriaDespesaFilhoService was null");
		}
		List<CategoriaDespesaFilho> listCatDespesaFilho = categoriaDespesaFilhoService.findAll();
		obsListCatDespesaFilho = FXCollections.observableArrayList(listCatDespesaFilho);
		comboBoxCategoriaFilhoDespesa.setItems(obsListCatDespesaFilho);
		
		
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("dataOriginalDespesa")) {
			labelErrorDataOriginalDespesa.setText(errors.get("dataOriginalDespesa"));
		} else {
			labelErrorDataOriginalDespesa.setText("");
		}
		if (fields.contains("dataConcluidaDespesa")) {
			labelErrorDataConcluidaDespesa.setText(errors.get("dataConcluidaDespesa"));
		} else {
			labelErrorDataConcluidaDespesa.setText("");
		}
		if (fields.contains("dataPagamentoParcela")) {
			labelErrorDataPagamentoParcela.setText(errors.get("dataPagamentoParcela"));
		} else {
			labelErrorDataPagamentoParcela.setText("");
		}
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
		if (fields.contains("statusDespesa")) {
			labelErrorStatusDespesa.setText(errors.get("statusDespesa"));
		} else {
			labelErrorStatusDespesa.setText("");
		}
		if (fields.contains("meioPagamento")) {
			labelErrorMeioPagamento.setText(errors.get("meioPagamento"));
		} else {
			labelErrorMeioPagamento.setText("");
		}
		if (fields.contains("qtdParcela")) {
			labelErrorQtdParcela.setText(errors.get("qtdParcela"));
		} else {
			labelErrorQtdParcela.setText("");
		}
		if (fields.contains("valor")) {
			labelErrorValor.setText(errors.get("valor"));
		} else {
			labelErrorValor.setText("");
		}
		if (fields.contains("valorParcela")) {
			labelErrorValorParcela.setText(errors.get("valorParcela"));
		} else {
			labelErrorValorParcela.setText("");
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

	private void initializecomboBoxCategoriaPaiDespesa() {
		Callback<ListView<CategoriaDespesa>, ListCell<CategoriaDespesa>> factory = lv -> new ListCell<CategoriaDespesa>() {
			@Override
			protected void updateItem(CategoriaDespesa item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCatPaiDespesa());
			}
		};

		comboBoxCategoriaPaiDespesa.setCellFactory(factory);
		comboBoxCategoriaPaiDespesa.setButtonCell(factory.call(null));
	}

//	private void initializecomboBoxCategoriaFilhoDespesa() {
//		Callback<ListView<CategoriaDespesaFilho>, ListCell<CategoriaDespesaFilho>> factory = lv -> new ListCell<CategoriaDespesaFilho>() {
//			@Override
//			protected void updateItem(CategoriaDespesaFilho item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? "" : item.getCategoriaFilhoDespesa());
//			}
//		};
//
//		comboBoxCategoriaFilhoDespesa.setCellFactory(factory);
//		comboBoxCategoriaFilhoDespesa.setButtonCell(factory.call(null));
//	}

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
