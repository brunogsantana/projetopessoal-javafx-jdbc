package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.ContaService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVisaoGeral;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private Label labelMovimentacaoCaixa;
	
	@FXML
	private Label labelLancamentos;
	
	@FXML
	private Label labelFluxo;
	
	@FXML
	private Label labelAPagarReceber;
	
	@FXML
	private Label labelPagasRecebidas;
	
	@FXML
	private Label labelExtratoDeContas;
	
	@FXML
	private Label labelCartaoDeCredito;
	
	@FXML
	private Label labelMetas;
	
	@FXML
	private Label labelOrcameto;
	
	@FXML
	private Label labelEconomia;
	
	@FXML
	private Label labelRelatorio;
	
	@FXML
	private Label labelInvestimento;
	
	@FXML
	private Label labelCadastro;
	
	@FXML
	private Label labelCategoria;
	
	@FXML
	private Label labelContas;
	
	@FXML
	private Label labelDocumentos;
	
	@FXML
	private Label labelRegrasDePreenchimento;
	
	@FXML
	private Label labelImportarLancamentos;
	
	@FXML
	private Label labelFechamentoPosicao;
	
	@FXML
	public void onMenuItemAboutClick() {
		loadView("/gui/About.fxml");
	}
	
	@FXML
	public void onMenuItemVisaoGeralClick() {
		System.out.println("onMenuVisaoGeralClick");
		loadView("/gui/Return.fxml");
	}
	
	@FXML
	public void onLabelMovimentacaoCaixaClick() {
		System.out.println("On onLabelMovimentacaoCaixaClick");
	}
	
	@FXML
	public void onLabelLancamentosClick() {

	}
	
	@FXML
	public void onLabelFluxoClick() {

	}
	
	@FXML
	public void onLabelAPagarReceberClick() {

	}
	
	@FXML
	public void onLabelPagasRecebidasClick() {

	}
	
	@FXML
	public void onLabelExtratoDeContasClick() {

	}
	
	@FXML
	public void onLabelCartaoDeCreditoClick() {

	}
	
	@FXML
	public void onLabelMetasClick() {

	}
	
	@FXML
	public void onLabelOrcametoClick() {

	}
	
	@FXML
	public void onLabelEconomiaClick() {

	}
	
	@FXML
	public void onLabelRelatorioClick() {

	}
	
	@FXML
	public void onLabelInvestimentoClick() {

	}
	
	@FXML
	public void onLabelCadastroClick() {

	}
	
	@FXML
	public void onLabelCategoriaClick() {

	}
	
	@FXML
	public void onLabelContasClick() {
		loadView2("/gui/ContaList.fxml");
	}
	
	@FXML
	public void onLabelDocumentosClick() {

	}
	
	@FXML
	public void onLabelRegrasDePreenchimentoClick() {

	}
	
	@FXML
	public void onLabelImportarLancamentosClick() {

	}
	
	@FXML
	public void onLabelFechamentoPosicaoClick() {

	}
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

	private synchronized void loadView (String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene= Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu=mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
					
		} 
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	
	private synchronized void loadView2 (String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene= Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu=mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			ContaListController controller = loader.getController();
			controller.setContaService(new ContaService());
			controller.updateTableView();
			
		} 
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	
}
