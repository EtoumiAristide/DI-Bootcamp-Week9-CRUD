package com.postgresqltutorial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.postgresqltutorial.models.Actor;

public class App {
	private static Connection dbConnexion;
    public static void main(String[] args) {
        ConnexionBD connect = new ConnexionBD();
        dbConnexion = connect.connect();
        
        Actor actor=new Actor();
        actor.setFirstName("Akissi");
        actor.setLastName("Delta");
        
        //insertActor(actor);
        
        //======= UPDATE
        actor.setActorId(203);
        actor.setLastName("Bohiri");
        actor.setFirstName("Michel");
        //updateActor(actor);
        
        
        //======= SELECT
        List<Actor> listeActorDB=getAllActor();
        listeActorDB.forEach(actorDB->System.out.println(actorDB));
        
        //===== DELETE
        deleteActor(actor);
    }
    
    private static void insertActor(Actor actor) {
    	final String SQL_INSERT_ACTOR="INSERT INTO actor(first_name, last_name) "
    			+ "VALUES(? ,?)";
    	
    	try {
    		PreparedStatement statement = dbConnexion.prepareStatement(SQL_INSERT_ACTOR, Statement.RETURN_GENERATED_KEYS);
    		
    		statement.setString(1, actor.getFirstName());
    		statement.setString(2, actor.getLastName());
    		
    		int nbLigneAffecte = statement.executeUpdate();
    		if(nbLigneAffecte!=0) {
    			try {
    				ResultSet rs = statement.getGeneratedKeys();
    						
                    if (rs.next()) {
                        Long id = rs.getLong(1);
                    	System.out.println("Données insérées avec succès");
            			System.out.println("ID GENERE = "+id);
                    }
                } catch (SQLException ex) {
                	ex.printStackTrace();
                    System.out.println("Aucun ID généré");
                }
    		}
    	
    	}catch(SQLException ex) {
    		System.out.println("Echec survenue lors de l'insertion");
    		ex.printStackTrace();
    	}
    }
    
    private static void updateActor(Actor actor) {
    	final String SQL_UPDATE="UPDATE actor "
    			+ "SET last_name= ?, "
    			+ "first_name = ? "
    			+ "WHERE actor_id = ?";
    	
    	try {
    		PreparedStatement statement=dbConnexion.prepareStatement(SQL_UPDATE);
    		statement.setString(1, actor.getLastName());
    		statement.setString(2, actor.getFirstName());
    		statement.setInt(3, actor.getActorId());
    		
    		int nbLignAffecte=statement.executeUpdate();
    		
    		if(nbLignAffecte!=0) {
    			System.out.println("Acteur modifié avec succès");
    		}
    	}catch(SQLException ex) {
    		System.out.println("Mise à jour echoué");
    		ex.printStackTrace();
    	}
    }
    
    private static List<Actor> getAllActor(){
    	List<Actor> listeDB=new ArrayList<>();
    	
    	final String SQL_SELECT="SELECT * "
    			+ "FROM actor";
    	
    	try {
    		Statement statement=dbConnexion.createStatement();
    		statement.executeQuery(SQL_SELECT);
    		ResultSet resultatSQL=statement.getResultSet();
    		
    		while(resultatSQL.next()) {
    			Actor actor=new Actor();
    			actor.setActorId(resultatSQL.getInt("actor_id"));
    			actor.setFirstName(resultatSQL.getString("first_name"));
    			actor.setLastName(resultatSQL.getString("last_name"));
    			actor.setLastUpdate(resultatSQL.getDate("last_update"));
    			
    			listeDB.add(actor);
    		}
    		
    	}catch (SQLException e) {
			System.out.println("Impossible de recuperer la liste des acteurs");
			e.printStackTrace();
		}
    	
    	return listeDB;
    }
    
    private static void deleteActor(Actor actor) {
    	final String SQL_DELETE="DELETE FROM actor "
    			+ "WHERE actor_id = ?";
    	
    	try {
    		PreparedStatement statement=dbConnexion.prepareStatement(SQL_DELETE);
    		statement.setInt(1, actor.getActorId());
    		
    		int nbLigneAffecte=statement.executeUpdate();
    		
    		if(nbLigneAffecte!=0) {
    			System.out.println(actor.getFirstName()+" supprimé avec succès");
    		}
    	}catch(SQLException ex) {
    		System.out.println("Impossible de supprimer l'acteur");
    		ex.printStackTrace();
    	}
    }
}

