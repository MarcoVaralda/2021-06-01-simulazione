/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGeni"
    private ComboBox<Genes> cmbGeni; // Value injected by FXMLLoader

    @FXML // fx:id="btnGeniAdiacenti"
    private Button btnGeniAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="txtIng"
    private TextField txtIng; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.setText(this.model.creaGrafo());
    	this.btnGeniAdiacenti.setDisable(false);
        this.btnSimula.setDisable(false);
        this.cmbGeni.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doGeniAdiacenti(ActionEvent event) {
    	Genes scelto = this.cmbGeni.getValue();
    	if(scelto==null) {
    		this.txtResult.setText("Devi inserire un gene dalla tendina!");
    		return;
    	}
    	
    	String result = "\n\nGeni adiacenti a "+scelto+":\n";
    	for(Adiacenza a : this.model.getAdiacenti(scelto))
    		result += a.getG1()+" - "+a.getPeso()+"\n";
    	this.txtResult.appendText(result);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	Genes scelto = this.cmbGeni.getValue();
    	if(scelto==null) {
    		this.txtResult.setText("Devi inserire un gene dalla tendina!");
    		return;
    	}
    	
    	String num = this.txtIng.getText();
    	int n = 0;
    	try {
    		n = Integer.parseInt(num);
    	}
    	catch(NumberFormatException nbe) {
    		this.txtResult.setText("Devi inserire un numero intero di ingegneri!");
    		return;
    	}
    	
    	this.txtResult.appendText(this.model.simula(n, scelto));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGeni != null : "fx:id=\"cmbGeni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGeniAdiacenti != null : "fx:id=\"btnGeniAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtIng != null : "fx:id=\"txtIng\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        this.btnGeniAdiacenti.setDisable(true);
        this.btnSimula.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
}
