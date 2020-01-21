package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import model.entities.Conta;
import model.entities.Receita;

public class MovimentacaoList implements Initializable {
	

	
	Conta conta = new Conta (1, null, null, null, null, null, null, null, null, null, null, null, null);
	List<Receita> list1 = service.findByConta(conta);
	for (Receita obj : list1) {
		System.out.println(obj);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
