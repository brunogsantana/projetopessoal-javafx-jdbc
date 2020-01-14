package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Conta;
import model.entities.enums.TipoConta;

public class ContaFormController implements Initializable {

	private Conta entity;
	
	
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
	
	@FXML
	private void onBtSaveAction() {
	System.out.println("onBtSaveAction");	
	}
	
	@FXML
	private void onBtCancelAction() {
	System.out.println("onBtCancelAction");	
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
}
