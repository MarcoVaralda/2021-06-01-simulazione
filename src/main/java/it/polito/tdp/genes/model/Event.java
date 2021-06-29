package it.polito.tdp.genes.model;

public class Event implements Comparable<Event>{
	
	private int data;
	private int ingegnere;
	private Genes gene;
	
	public Event(int data, int ingegnere, Genes gene) {
		super();
		this.data = data;
		this.ingegnere = ingegnere;
		this.gene = gene;
	}

	public int getData() {
		return data;
	}

	public int getIngegnere() {
		return ingegnere;
	}

	public Genes getGene() {
		return gene;
	}

	@Override
	public int compareTo(Event altro) {
		return this.data-altro.getData();
	}

}
