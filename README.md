# TD/TP introduction à la programmation thread Java.

# Correction

## Exercice 1 - Entrelacement des Threads : PingPong

Classes Java :

 - [`PingPongThread.java`](PingPongThread.java) : en
   héritant de la classe `Thread`;
 
 - [`PingPongRunnable.java`](PingPongRunnable.java) :
   en implémentant l'interface `Runnable`.

## Exercice 2 - SynchronizedCounter

Classes Java :

 -
   [`SynchronizedCounter.java`](SynchronizedCounter.java)
   : les méthodes pour incrémenter ou décrémenter le compteur sont
   protégées grâce au mot clé `synchronized`. Un seul thread à la fois
   peut exécuter une méthode de ce type.
 
 -
   [`SynchronizedCounterThread.java`](SynchronizedCounterThread.java). Création
   de deux threads qui incrémentent et décrémentent un compteur
   partagé. Le résultat est toujours correct.

 - [`Counter.java`](Counter.java). Ici on a enlevé le mot clé `synchronized`.
 
 - [`CounterThread.java`](CounterThread.java). Le
   résultat est que les valeurs du compteur partagé montrent des
   problèmes de cohérence de données (voir diapositive 22 du cours 1).

 - [`LockCounter.java`](LockCounter.java). Dans cette classe on protège uniquement le bloc d'instructions et non toute la méthode. Tous les blocs d'instructions `synchronized (Objet)` qui partagent `Objet` sont en exclusion mutuelle.
 
 - [`LockCounterThread.java`](LockCounterThread.java)

 - [`AtomicCounter.java`](AtomicCounter.java). Ici on
   utilise un type atomique pour la variable partagée compteur. Les
   opérations d'incrément et de décrément sont effectuées par des
   méthodes spéciales qui garantissent la cohérence. Elles sont
   effectuées en une seule étape du point de vue des threads.
 
 - [`AtomicCounterThread.java`](AtomicCounterThread.java)

Dans tous les cas, il faut penser à attendre la terminaison des
threads avant d'afficher la valeur finale du compteur dans le thread
`main` pour garantir un résultat correct.

## Exercice 3 - Somme multithread

Classes Java :

 -
   [`SommeIntervalle.java`](SommeIntervalle.java). Cette
   classe implémente `Runnable` pour calculer la somme des éléments du
   tableau passé en argument sur l'intervalle `[debut, fin[`. Rappel :
   la méthode `run()` n'accepte pas de paramètres, on passe donc la
   référence sur le tableau et les indices de début et fin dans un
   constructeur.

 - [`SommeTab.java`](SommeTab.java). `SommeTab` propose
   la méthode `somme()` qui instancie `p` objets de type
   `SommeIntervalle` qui vont chacun calculer une partie de la somme
   des éléments d'un tableau dans un thread. À la fin des `p` threads,
   après la boucle `join()`, on récupére les résultats partiels et on
   calcule la somme totale. Rappel : la méthode `run()` est de type
   `void`, on récupère donc les résultats avec un accesseur.

Dans le `main()` de cette classe on lance plusieurs exécutions de `somme()` et on compare les temps de calcul à ceux obtenus en séquentiel et avec l'API `stream` introduite dans Java 8.


## Exercice 4 - Tri Fusion

Classes Java :

 - [`TriFusion.java`](TriFusion.java). Comme dans
   `SommeTab`, `TriFusion` découpe un tableau en `p` morceaux. Chaque
   morceau est trié en parallèle dans un thread `TriIntervalle` (qui
   est une classe interne dans cet exercice). Ensuite on fusionne les
   morceaux triés, sans utiliser le multithreading dans cette
   correction.
   

 - [`TestTriFusion.java`](TestTriFusion.java). Test unitaires.
   

Dans le `main()` de `TriFusion` on peut choisir d'être verbeux ou pas
pour suivre le déroulement de l'algorithme. Il suffit de remplacer la
ligne `Debugger.enabled = true;` par `Debugger.enabled = false;` pour
stopper l'affichage avant les tests.


## Exercice 5 - Recherche multithread

Exercice non travaillé en séance.


