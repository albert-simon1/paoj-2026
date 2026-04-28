# Aplicație Bancară - Etapa I

Sistem pentru gestionarea operațiunilor bancare de bază: clienți, conturi, carduri și tranzacții.

# 1.1 Acțiuni / Interogări posibile în sistem 
1. Înregistrează un client nou în sistem.
2. Deschide un cont curent pentru un client.
3. Deschide un cont de economii.
4. Emite un card nou și atașează-l unui cont curent.
5. Depune o sumă de bani într-un cont (prin IBAN).
6. Retrage o sumă de bani dintr-un cont.
7. Transferă bani între două conturi diferite.
8. Blochează un card bancar.
9. Afișează toate conturile deținute de un client.
10. Generează și afișează extrasul de cont (istoricul tranzacțiilor sortat cronologic).

# 1.2 Tipuri de obiecte din domeniu
1. `Tranzactionabil` (Interfață)
2. `ContBancar` (Clasă abstractă)
3. `ContCurent` (Clasă derivată)
4. `ContEconomii` (Clasă derivată)
5. `Client`
6. `Adresa`
7. `Card`
8. `Tranzactie` (Clasă imutabilă)
