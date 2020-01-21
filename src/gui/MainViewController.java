package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.CategoriaReceitaService;
import model.services.ContaService;
import model.services.ReceitaService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVisaoGeral;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private Label labelMovimentacaoCaixa;

	@FXML
	private Label labelReceitas;

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
	private Label labelCategoriaReceita;

	@FXML
	private Label labelContas;

	@FXML
	private Label labelDocumentos;

	@FXML
	private Label labelRegrasDePreenchimento;

	@FXML
	private Label labelImportarReceitas;

	@FXML
	private Label labelFechamentoPosicao;

	@FXML
	public void onMenuItemAboutClick() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemVisaoGeralClick() {
		System.out.println("onMenuVisaoGeralClick");
		loadView("/gui/Return.fxml", x -> {
		});
	}

	@FXML
	public void onLabelMovimentacaoCaixaClick() {
		System.out.println("On onLabelMovimentacaoCaixaClick");
	}

	@FXML
	public void onLabelReceitasClick() {
		loadView("/gui/ReceitaList.fxml", (ReceitaListController controller) -> {
			controller.setReceitaService(new ReceitaService());
			controller.updateTableView();

		});
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
		loadView("/gui/CategoriaReceitaList.fxml", (CategoriaReceitaListController controller) -> {
			controller.setCategoriaReceitaService(new CategoriaReceitaService());
			controller.updateTableView();

		});
	}

	@FXML
	public void onLabelContasClick() {
		loadView("/gui/ContaList.fxml", (ContaListController controller) -> {
			controller.setContaService(new ContaService());
			controller.updateTableView();

		});
	}

	@FXML
	public void onLabelDocumentosClick() {

	}

	@FXML
	public void onLabelRegrasDePreenchimentoClick() {

	}

	@FXML
	public void onLabelImportarReceitasClick() {

	}

	@FXML
	public void onLabelFechamentoPosicaoClick() {

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}

}
