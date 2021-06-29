package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
	
	// Eventi
	PriorityQueue<Event> queue = new PriorityQueue<>() ;
	
	// Parametri
	int n; // Numero ing.
	Graph<Genes,DefaultWeightedEdge> grafo;
	int inizio = 1;
	int fine = 36;
	
	// Stato del sistema
	Map<Genes,Integer> geniStudiati; // Gene => Numero ingegneri che lo sudiano
	
	
	public void init(int n, Genes partenza, Graph<Genes,DefaultWeightedEdge> grafo) {
		this.n=n;
		this.grafo=grafo;
		
		this.geniStudiati = new HashMap<>();
		this.geniStudiati.put(partenza, n);
		
		
		// Eventi iniziali
		for(int i=1;i<=n;i++) {
			Event e = new Event(1,i,partenza);
			this.queue.add(e);
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			if(e.getData()<=this.fine)
				processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int random = (int)(Math.random()*100);
		
		if(random<=30) { // Studia lo stesso gene
			this.queue.add(new Event(e.getData()+1, e.getIngegnere(), e.getGene()));
		}
		else { // Cambia gene da studiare
			// Cerco gli adiacenti
			List<Adiacenza> adiacenti = this.getAdiacenti(e.getGene());
			
			// Calcolo il peso totale
			double pesoTot = 0.0;
			for(Adiacenza a : adiacenti)
				pesoTot += a.getPeso();
			
			// Decido quale studiare
			double prob = (Math.random()*pesoTot);
			double probCumulata = 0.0;
			for(Adiacenza a : adiacenti) {
				if(prob<=a.getPeso()+probCumulata) {
					// Studio questo gene
					Event nuovoEvento = new Event(e.getData()+1,e.getIngegnere(),a.getG1());
					this.queue.add(nuovoEvento);
					
					this.geniStudiati.replace(e.getGene(), this.geniStudiati.get(e.getGene())-1);		
					
					if(this.geniStudiati.get(a.getG1())!=null)
						this.geniStudiati.replace(a.getG1(), this.geniStudiati.get(a.getG1())+1);
					else
						this.geniStudiati.put(a.getG1(), 1);
					break;
				}
				else 
					probCumulata += a.getPeso();
			}
		}
	}
	
	public List<Adiacenza> getAdiacenti(Genes scelto) {
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge arco : this.grafo.edgesOf(scelto)) {
			double peso = this.grafo.getEdgeWeight(arco);
			Genes vicino = Graphs.getOppositeVertex(this.grafo, arco, scelto);
			result.add(new Adiacenza(vicino,scelto,peso));
		}
		Collections.sort(result);
		return result;
	}
	
	
	
	public String getResult() {
		String result = "\n\nGeni studiati:\n";
		
		for(Genes g : this.geniStudiati.keySet())
			if(this.geniStudiati.get(g)>0)
				result += g+" studiato da "+this.geniStudiati.get(g)+" ingegneri\n";
		
		return result;
	}

}
