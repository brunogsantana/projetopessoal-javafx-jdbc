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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Conta;
import model.entities.enums.TipoConta;
import model.exceptions.ValidationException;
import model.services.ContaService;

public class ContaFormController implements Initializable {

	private Conta entity;
	
	private ContaService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
		
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtCpf;
	
	@FXML
	private ChoiceBox<TipoConta> ChoiceBoxTipoConta;
	
	@FXML
	private TextField txtBanco;
	
	@FXML
	private TextField txtNumeroBanco;
	
	@FXML
	private TextField txtNumeroConta;
	
	@FXML
	private TextField txtNumeroAgencia;
	
//	@FXML
//	private TextField txtDataCadastro;
	
	@FXML
	private TextField txtSaldoAtual;
	
	@FXML
	private TextField txtSaldoInicial;
	
	@FXML
	private CheckBox CheckBoxFavorita;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorCpf;
	
	@FXML
	private Label labelErrorTipoConta;
	
	@FXML
	private Label labelErrorBanco;
	
	@FXML
	private Label labelErrorNumeroBanco;
	
	@FXML
	private Label labelErrorNumeroConta;
	
	@FXML
	private Label labelErrorNumeroAgencia;
	
//	@FXML
//	private Label labelErrorDataCadastro;
	
	@FXML
	private Label labelErrorSaldoAtual;
	
	@FXML
	private Label labelErrorSaldoInicial;
	
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	public void setConta (Conta entity) {
		this.entity = entity;
	}
	
	public void setContaService(ContaService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener (DataChangeListener listener) {
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
			}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
			
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	private Conta getFormData() {
		Conta obj = new Conta();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText()==null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}

		
		if(txtCpf.getText()==null || txtCpf.getText().trim().equals("")) {
			exception.addError("cpf", "Field can't be empty");
		}

		
/*		if(txtTipoConta.getText()==null || txtTipoConta.getText().trim().equals("")) {
			exception.addError("tipoConta", "Field can't be empty");
		}
*/
		
		if(txtBanco.getText()==null || txtBanco.getText().trim().equals("")) {
			exception.addError("banco", "Field can't be empty");
		}

		
		if(txtNumeroAgencia.getText()==null || txtNumeroAgencia.getText().trim().equals("")) {
			exception.addError("numeroAgencia", "Field can't be empty");
		}

		
		if(txtNumeroBanco.getText()==null || txtNumeroBanco.getText().trim().equals("")) {
			exception.addError("numeroBanco", "Field can't be empty");
		}

		
		if(txtNumeroConta.getText()==null || txtNumeroBanco.getText().trim().equals("")) {
			exception.addError("numeroBanco", "Field can't be empty");
		}

		
/*		if(txtDataCadastro.getText()==null || txtDataCadastro.getText().trim().equals("")) {
			exception.addError("dataCadastro", "Field can't be empty");
		}
*/
		
		if(txtSaldoAtual.getText()==null || txtSaldoAtual.getText().trim().equals("")) {
			exception.addError("saldoAtual", "Field can't be empty");
		}

		
		if(txtSaldoInicial.getText()==null || txtSaldoInicial.getText().trim().equals("")) {
			exception.addError("saldoInicial", "Field can't be empty");
		}
		
		obj.setName(txtName.getText());
		obj.setCpf(txtCpf.getText());
//		obj.setTipoConta(txtTipoConta.getText());
		obj.setBanco(txtBanco.getText());
		obj.setNumeroAgencia(Utils.tryParseToInt(txtNumeroAgencia.getText()));
		obj.setNumeroBanco(Utils.tryParseToInt(txtNumeroBanco.getText()));
		obj.setNumeroConta(Utils.tryParseToInt(txtNumeroConta.getText()));
//		obj.setDataCadastro(txtDataCadastro.getText());
		obj.setSaldoAtual(Utils.tryParseToDouble(txtSaldoAtual.getText()));
		obj.setSaldoInicial(Utils.tryParseToDouble(txtSaldoInicial.getText()));
//		obj.setFavorita(txtFavorita.getText());
				
		if (exception.getErrors().size()> 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	private void onChoiceBoxTipoContaAction() {
	System.out.println("onChoiceBoxTipoContaAction");	
	}
	
	@FXML
	private void onCheckBoxFavoritaAction() {
	System.out.println("onCheckBoxFavoritaAction");	
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}
		
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
//		Constraints.setTextFieldMaxLength(txtTipoConta, 30);
		Constraints.setTextFieldMaxLength(txtBanco, 30);
		Constraints.setTextFieldInteger(txtNumeroBanco);
		Constraints.setTextFieldMaxLength(txtNumeroBanco, 3);
		Constraints.setTextFieldInteger(txtNumeroAgencia);
		Constraints.setTextFieldInteger(txtNumeroConta);
//		Constraints.setTextFieldtoDate(txtDataCadastro);
		Constraints.setTextFieldDouble(txtSaldoAtual);
		Constraints.setTextFieldDouble(txtSaldoInicial);
//		Constraints.setTextFieldInteger(txtFavorita);
	}
	
	public void updateFormData () {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtCpf.setText(entity.getCpf());
//		txtTipoConta.setText(entity.getTipoConta());
		txtBanco.setText(entity.getBanco());
		txtNumeroBanco.setText(String.valueOf(entity.getNumeroBanco()));
		txtNumeroAgencia.setText(String.valueOf(entity.getNumeroAgencia()));
		txtNumeroConta.setText(String.valueOf(entity.getNumeroConta()));
//		txtDataCadastro.setText(String.valueOf(entity.getDataCadastro()));
		txtSaldoInicial.setText(String.valueOf(entity.getSaldoInicial()));
		txtSaldoAtual.setText(String.valueOf(entity.getSaldoAtual()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
		if (fields.contains("cpf")) {
			labelErrorCpf.setText(errors.get("cpf"));
		}
		if (fields.contains("tipoConta")) {
			labelErrorTipoConta.setText(errors.get("tipoConta"));
		}
		if (fields.contains("banco")) {
			labelErrorBanco.setText(errors.get("banco"));
		}
		if (fields.contains("numeroBanco")) {
			labelErrorNumeroBanco.setText(errors.get("numeroBanco"));
		}
		if (fields.contains("numeroAgencia")) {
			labelErrorNumeroAgencia.setText(errors.get("numeroAgencia"));
		}
		if (fields.contains("numeroConta")) {
			labelErrorNumeroConta.setText(errors.get("numeroConta"));
		}
/*		if (fields.contains("dataCadastro")) {
			labelErrorDataCadastro.setText(errors.get("dataCadastro"));
		}
*/		if (fields.contains("saldoInicial")) {
			labelErrorSaldoInicial.setText(errors.get("saldoInicial"));
		}
		if (fields.contains("saldoAtual")) {
			labelErrorSaldoAtual.setText(errors.get("saldoAtual"));
		}
/*		if (fields.contains("favorita")) {
			labelErrorFavorita.setText(errors.get("favorita"));
		} */
	}
}
