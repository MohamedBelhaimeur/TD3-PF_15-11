package td1.commandes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import td1.paires.Paire;

public class Commande{
    private List<Paire<Produit, Integer>> lignes;

    public Commande() {
        this.lignes = new ArrayList<>();
    }

    public Commande ajouter(Produit p, int q) {
        lignes.add(new Paire<>(p, q));
        return this;
    }
 
    private static final Function<Paire<Produit, Integer>, String> formateurLigne = k -> String.format("Commande : %s: %d", k.fst(), k.snd());
    public List<Paire<Produit, Integer>> lignes() {
        return lignes;
    }

    @Override
    public String toString() {
       
       return  lignes.stream()
               .map(formateurLigne)
               .collect(Collectors.joining("\n"));
       
       
    }
    
    public static <A,B> Map<A, List<B>> regrouper(List<Paire<A,B>> liste){
        /*
            Méthode générique typée <A, B>.
            C'est une Liste et non un ensemble, donc on garde d'éventuels doublons.
            Avec ((a1, b1), (a2, b2), (a1, b4)), par exemple, on retourne a1 -> [b1, b4].
        */
        Map<A, List<B>> map = new HashMap<>();
        for (Paire<A,B> n:liste) {
            if (!map.containsKey(n.fst())) {
                map.put(n.fst(), new ArrayList<>());
            }
            map.get(n.fst()).add(n.snd());
        }
        return map;


        /*return liste.stream()
                .collect(groupingBy(Paire::fst),Collectors.mapping(Paire::snd, Collectors.toSet()));*/
    }
   
    /**
     * cumule les lignes en fonction des produits
     */
    public Commande normaliser() {
        Map<Produit, Integer> lignesCumulees = new HashMap<>();
        for (Paire<Produit, Integer> ligne : lignes) {
            Produit p = ligne.fst();
            int qte = ligne.snd();
            if (lignesCumulees.containsKey(ligne.fst())) {
                lignesCumulees.put(p, lignesCumulees.get(p) + qte);
            } else {
                lignesCumulees.put(p, qte);
            }
        }
        Commande commandeNormalisee = new Commande();
        for (Produit p : lignesCumulees.keySet()) {
            commandeNormalisee.ajouter(p, lignesCumulees.get(p));
        }
        return commandeNormalisee;
    }

    public Double cout(Function<Paire<Produit, Integer>, Double> calculLigne) {
    	 return lignes.stream()
                 .map(calculLigne)
                 .reduce(0.0, Double::sum);
    }

    public String affiche(Function<Paire<Produit, Integer>, Double> calculLigne) {
        Commande c = this.normaliser();
        final String HLINE = "+------------+------------+-----+------------+--------+------------+\n";
        StringBuilder str = new StringBuilder();
        str.append("\n\nCommande\n");
        str.append(HLINE);
        str.append("+ nom        + prix       + qtÃ© + prix ht    + tva    + prix ttc   +\n");
        str.append(HLINE);
        for (Paire<Produit, Integer> ligne : c.lignes) {
            str.append(String.format("+ %10s + %10.2f + %3d + %10.2f + %5.2f%% + %10.2f +\n", ligne.fst(), // nom
                    ligne.fst().prix(), // prix unitaire
                    ligne.snd(), // qtÃ©
                    ligne.fst().prix() * ligne.snd(), // prix ht
                    ligne.fst().cat().tva() * 100, // tva
                    calculLigne.apply(ligne)));
        }
        str.append(HLINE);
        str.append(String.format("Total : %10.2f", c.cout(calculLigne)));
        return str.toString();
    }

}
