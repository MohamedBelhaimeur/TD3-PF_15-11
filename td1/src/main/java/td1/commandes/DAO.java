package td1.commandes;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;

import static td1.commandes.Categorie.*;
import td1.paires.Paire;


public class DAO {
    private List<Commande> commandes;

    private DAO(Commande c1, Commande ...cs) {
        commandes = new ArrayList<>();
        commandes.add(c1);
        commandes.addAll(List.of(cs));
    }

    public static DAO instance = null;

    public static final DAO instance() {
        if (instance == null) {
            Produit camembert = new Produit("Camembert", 4.0, NORMAL);
            Produit yaourts = new Produit("Yaourts", 2.5, INTERMEDIAIRE);
            Produit masques = new Produit("Masques", 25.0, REDUIT);
            Produit gel = new Produit("Gel", 5.0, REDUIT);
            Produit tournevis = new Produit("Tournevis", 4.5, NORMAL);
            //
            Commande c1 = new Commande()
                .ajouter(camembert, 1)
                .ajouter(yaourts, 6);
            Commande c2 = new Commande()
                .ajouter(masques, 2)
                .ajouter(gel, 10)
                .ajouter(camembert, 2)
                .ajouter(masques, 3);
            //
            instance = new DAO(c1,c2);
        }
        return instance;
    }

    /**
     * liste de toutes les commandes
     */
    public List<Commande> commandes() { return commandes; }

    /**
     * ensemble des différents produits commandés
     */
    public Set<Produit> produits() {
    	Set<Produit>liste_produit=new HashSet<>();
    	for(Commande index:commandes){
        	for (Paire<Produit,Integer> index2:index.lignes()) {
        		 
        			
        	}
        	
        }
      
    	 return liste_produit;
    }

    /**
     * liste des commandes vérifiant un prédicat
     */
    public List<Commande> selectionCommande(Predicate<Commande> p) {
        List<Commande> selectioncommande=new ArrayList<>();

        for(Commande index:commandes){
          if(p.test(index)){
              selectioncommande.add(index);
          }
        }
        return selectioncommande;
    }

    /**
     * liste des commandes dont au moins une ligne vérifie un prédicat
     */
    public List<Commande> selectionCommandeSurExistanceLigne(Predicate<Paire<Produit,Integer>> p) {
    	List<Commande> selectioncommande=new ArrayList<>();

        for(Commande index:commandes){
        	for (Paire<Produit,Integer> index2:index.lignes()) {
        		 if(p.test(index2)){
                     selectioncommande.add(index); 
                 }
        	}
         
        }
        return selectioncommande;
    
    }

    /**
     * ensemble des différents produits commandés vérifiant un prédicat
     */
    public Set<Produit> selectionProduits(Predicate<Produit> p) {
    	Set<Produit>liste_produit=new HashSet<>();
    	for(Commande index:commandes){
        	for (Paire<Produit,Integer> index2:index.lignes()) {
        		 if(p.test(index2.fst())){
        			 liste_produit.add(index2.fst()); 
                 }
        	}
        	
        }
      
    	 return liste_produit;
}
}