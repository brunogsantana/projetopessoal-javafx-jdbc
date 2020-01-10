package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Conta;

public class ContaListController implements Initializable {

	@FXML
	private TableView<Conta> tableViewConta;
	
	@FXML
	private TableColumn<Conta, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Conta, String> tableColumnHolder;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction () {
		System.out.println("onBtNewAction");
	}
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {	
		initializeNodes();
	}


	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnHolder.setCellValueFactory(new PropertyValueFactory<>("holder"));
		
		Stage stage=(Stage) Main.getMainScene().getWindow();
		tableViewConta.prefHeightProperty().bind(stage.heightProperty());
	}

	
	
}
