package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Conta;
import model.services.ContaService;

public class ContaListController implements Initializable {
	
	private ContaService service;
	

	@FXML
	private TableView<Conta> tableViewConta;
	
	@FXML
	private TableColumn<Conta, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Conta, String> tableColumnName;
	
	@FXML
	private TableColumn<Conta, Integer> tableColumnCPF;

	@FXML
	private TableColumn<Conta, String> tableColumnBanco;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroBanco;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroConta;

	@FXML
	private TableColumn<Conta, Integer> tableColumnNumeroAgencia;

	@FXML
	private TableColumn<Conta, Date> tableColumnDataCadastro;
	
	@FXML
	private TableColumn<Conta, Double> tableColumnSaldoAtual;
	
	@FXML
	private TableColumn<Conta, Boolean> tableColumnFavorita;
	
	
	@FXML
	private Button btNew;
	
	private ObservableList<Conta> obsList;
		
	@FXML
	public void onBtNewAction () {
		System.out.println("onBtNewAction");
	}
	
	public void setContaService (ContaService service) {
		this.service=service;
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {	
		initializeNodes();
	}


	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnBanco.setCellValueFactory(new PropertyValueFactory<>("banco"));
		tableColumnNumeroBanco.setCellValueFactory(new PropertyValueFactory<>("numeroBanco"));
		tableColumnNumeroConta.setCellValueFactory(new PropertyValueFactory<>("numeroConta"));
		tableColumnNumeroAgencia.setCellValueFactory(new PropertyValueFactory<>("numeroAgencia"));
		tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
		tableColumnSaldoAtual.setCellValueFactory(new PropertyValueFactory<>("saldoAtual"));
		tableColumnFavorita.setCellValueFactory(new PropertyValueFactory<>("favorita"));
	
		
		Stage stage=(Stage) Main.getMainScene().getWindow();
		tableViewConta.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Conta> list =service.findAll();
		obsList=FXCollections.observableArrayList(list);
		tableViewConta.setItems(obsList);
		
		
	}
	
}
