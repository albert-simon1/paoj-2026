package com.pao.laboratory03.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Exercițiul 1 — Colecții: HashMap și TreeMap
 *
 * Creează în acest main:
 *
 * PARTEA A — HashMap (frecvența cuvintelor)
 * 1. Declară un array de String-uri:
 *    String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
 * 2. Creează un HashMap<String, Integer> care contorizează de câte ori apare fiecare cuvânt.
 *    - Parcurge array-ul și folosește put() + getOrDefault() pentru a incrementa contorul.
 * 3. Afișează map-ul.
 * 4. Verifică dacă există cheia "rust" cu containsKey().
 * 5. Afișează DOAR cheile (keySet()), apoi DOAR valorile (values()).
 * 6. Parcurge map-ul cu entrySet() și afișează "cheia -> valoarea" pentru fiecare intrare.
 *
 * PARTEA B — TreeMap (sortare automată)
 * 7. Creează un TreeMap<String, Integer> din același HashMap (constructor cu argument).
 * 8. Afișează TreeMap-ul — observă ordinea alfabetică a cheilor.
 * 9. Folosește firstKey() și lastKey() pentru a afișa prima și ultima cheie.
 *
 * PARTEA C — Map cu obiecte
 * 10. Creează un HashMap<String, List<String>> care asociază materii cu liste de studenți.
 *     Exemplu: "PAOJ" -> ["Ana", "Mihai", "Ion"], "BD" -> ["Ana", "Elena"]
 * 11. Afișează toți studenții de la materia "PAOJ".
 * 12. Adaugă un student nou la "BD" și afișează lista actualizată.
 *
 * Output așteptat (orientativ — ordinea HashMap poate varia):
 *
 * === PARTEA A: HashMap — frecvența cuvintelor ===
 * Frecvență: {python=2, c++=2, java=3, rust=1, go=1}
 * Conține 'rust'? true
 * Chei: [python, c++, java, rust, go]
 * Valori: [2, 2, 3, 1, 1]
 * python -> 2
 * c++ -> 2
 * java -> 3
 * rust -> 1
 * go -> 1
 *
 * === PARTEA B: TreeMap — sortare automată ===
 * Sortat: {c++=2, go=1, java=3, python=2, rust=1}
 * Prima cheie: c++
 * Ultima cheie: rust
 *
 * === PARTEA C: Map cu obiecte ===
 * Studenți la PAOJ: [Ana, Mihai, Ion]
 * Studenți la BD (actualizat): [Ana, Elena, George]
 */
public class Main {
    public static void main(String[] args) {
        // TODO: implementează cele 3 părți de mai sus
        String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
        HashMap<String, Integer> h=new HashMap<>();

        for(String i: words){
            h.put(i,h.getOrDefault(i, 0) + 1);
        }
        System.out.println("Frecvența cuvintelor:");
        System.out.println(h);
        if(h.containsKey("rust")){
            System.out.println("da apare");
        }
        else{
            System.out.println("nu apare");
        }
        System.out.println(h.keySet());
        System.out.println(h.values());
for(Map.Entry<String,Integer>p:h.entrySet()){
    System.out.println(p.getKey()+"->"+p.getValue());
}
        TreeMap<String, Integer> sortedWords = new TreeMap<>(h);

        System.out.println(sortedWords);

        System.out.println(sortedWords.firstKey());
        System.out.println( sortedWords.lastKey());

        Map<String, List<String>> cursuri = new HashMap<>();

        List<String> studentiPAOJ = new ArrayList<>(Arrays.asList("Ana", "Mihai", "Ion"));
        List<String> studentiBD = new ArrayList<>(Arrays.asList("Ana", "Elena"));

        cursuri.put("PAOJ", studentiPAOJ);
        cursuri.put("BD", studentiBD);

        System.out.println("\n11. Studenți la PAOJ: " + cursuri.get("PAOJ"));

        if (cursuri.containsKey("BD")) {
            cursuri.get("BD").add("George");
        }
        System.out.println("12. Listă actualizată BD: " + cursuri.get("BD"));

}

}


