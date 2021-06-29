package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertici(Map<String,Genes> idMap){
		String sql = "SELECT * "
				+ "FROM genes g "
				+ "WHERE g.Essential='Essential' "
				+ "GROUP BY g.GeneID ";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("GeneID"))) {
					Genes genes = new Genes(res.getString("GeneID"), 
							res.getString("Essential"), 
							res.getInt("Chromosome"));
					result.add(genes);
					idMap.put(res.getString("GeneID"), genes);
				}
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}
	}
	
	public List<Adiacenza> getArchi(Map<String,Genes> idMap){
		String sql = "SELECT i.GeneID1 AS id1, i.GeneID2 AS id2, ABS(i.Expression_Corr) AS peso, g1.Chromosome AS c1, g2.Chromosome AS c2 "
				+ "FROM interactions i, genes g1, genes g2 "
				+ "WHERE i.GeneID1 <> i.GeneID2 AND g1.GeneID=i.GeneID1 AND g2.GeneID=i.GeneID2 AND g1.Essential='Essential' AND g1.Essential=g2.Essential "
				+ "GROUP BY i.GeneID1, i.GeneID2";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Genes g1 = idMap.get(res.getString("id1"));
				Genes g2 = idMap.get(res.getString("id2"));
				
				if(g1!=null && g2!=null) {
					double peso = res.getDouble("peso");
					Adiacenza a = null;
					if(res.getInt("c1")==res.getInt("c2"))
						a = new Adiacenza(g1,g2,2*peso);
					else 
						a = new Adiacenza(g1,g2,peso);
					result.add(a);
				}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	


	
}
