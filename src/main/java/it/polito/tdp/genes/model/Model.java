package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	GenesDao dao ;
	Map<String,Genes> idMap;
	Graph<Genes,DefaultWeightedEdge> grafo;
	
	// PUNTO 2
	Simulatore sim;
	
	public Model() {
		this.dao = new GenesDao();
		this.idMap = new TreeMap<>();
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.getVertici(idMap);
		
		// Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		// Aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi(idMap)) 
			if(!this.grafo.containsEdge(a.getG1(), a.getG2()))
				Graphs.addEdge(this.grafo, a.getG1(), a.getG2(), a.getPeso());
		
		return "Grafo creato!\n\nNumero vertici: "+this.grafo.vertexSet().size()+"\nNumero archi: "+this.grafo.edgeSet().size();
	}
	
	public Collection<Genes> getVertici() {
		return this.grafo.vertexSet();
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
	
	
	// PUNTO 2
	public String simula(int n, Genes g) {
		this.sim = new Simulatore();
		
		this.sim.init(n, g, grafo);
		
		this.sim.run();
		
		return this.sim.getResult();
	}
	
	
}
