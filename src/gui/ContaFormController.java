package gui;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContaFormController implements Initializable {

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtCpf;
	
	@FXML
	private TextField txtTipoConta;
	
	@FXML
	private TextField txtBanco;
	
	@FXML
	private TextField txtNumeroBanco;
	
	@FXML
	private TextField txtNumeroConta;
	
	@FXML
	private TextField txtNumeroAgencia;
	
	@FXML
	private TextField txtDataCadastro;
	
	@FXML
	private TextField txtSaldoAtual;
	
	@FXML
	private TextField txtSaldoInicial;
	
	@FXML
	private TextField txtFavorita;
	
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
	
	@FXML
	private Label labelErrorDataCadastro;
	
	@FXML
	private Label labelErrorSaldoAtual;
	
	@FXML
	private Label labelErrorSaldoInicial;
	
	@FXML
	private Label labelErrorFavorita;
	
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	@FXML
	private void onBtSaveAction() {
	System.out.println("onBtSaveAction");	
	}
	
	@FXML
	private void onBtCancelAction() {
	System.out.println("onBtCancelAction");	
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}
		
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
		Constraints.setTextFieldMaxLength(txtTipoConta, 30);
		Constraints.setTextFieldMaxLength(txtBanco, 30);
		Constraints.setTextFieldInteger(txtNumeroBanco);
		Constraints.setTextFieldMaxLength(txtNumeroBanco, 3);
		Constraints.setTextFieldInteger(txtNumeroAgencia);
		Constraints.setTextFieldInteger(txtNumeroConta);
//		Constraints.setTextFieldtoDate(txtDataCadastro);
		Constraints.setTextFieldDouble(txtSaldoAtual);
		Constraints.setTextFieldDouble(txtSaldoInicial);
		Constraints.setTextFieldInteger(txtFavorita);

		
	}
}
