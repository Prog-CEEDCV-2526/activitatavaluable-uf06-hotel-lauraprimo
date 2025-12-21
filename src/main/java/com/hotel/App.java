package com.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Codis generats aleatoriament, corresponen a cada reserva feta
    public static int codisReserva[] = new int[900];

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {

        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");

            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Obtindre una reserva");
        System.out.println("5. Llistar reserves per tipus");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
        switch (opcio) {
            case 1:
                reservarHabitacio();
                break;
            case 2:
                alliberarHabitacio();
                break;
            case 3:
                consultarDisponibilitat();
                break;
            case 4:
                obtindreReserva();
                break;
            case 5:
                obtindreReservaPerTipus();
                break;
            case 6:
                System.out.println("Eixint del sistema de reserves.");
                break;
            default:
                System.out.println("Opció no vàlida");
                break;
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        ArrayList<String> serveisTriats = new ArrayList<>();
        ArrayList<String> dadesReserva = new ArrayList<>();

        float preuTotalReserva = 0;
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        String tipusHabitacio = seleccionarTipusHabitacioDisponible();

        int numServeis = 0;
        float subtotalServeis = 0, subtotal = 0;
        System.out.println(tipusHabitacio);//

        if (tipusHabitacio != null) {
            serveisTriats = seleccionarServeis();
            float preuHabitacio = preusHabitacions.get(tipusHabitacio);
            System.out.println("Preu habitació: " + preuHabitacio + "€");

            System.out.println("Serveis triats:");
            for (String ser : serveisTriats) {
                numServeis++;
                System.out.println("\t" + numServeis + "." + ser + " (" + preusServeis.get(ser) + " €)");
                subtotalServeis += preusServeis.get(ser);
                dadesReserva.add(ser);

            }
            // suma entre serveis triats i preu de l'habitació escollida
            subtotal = subtotalServeis + preuHabitacio;

            System.out.println("Subtotal: " + subtotal + " €");

            System.out.printf("IVA (21%%): %.2f\n", subtotal * IVA);

            preuTotalReserva = calcularPreuTotal(tipusHabitacio, serveisTriats);

            System.out.printf("TOTAL: %.2f €\n", preuTotalReserva);

            System.out.println("Reserva creada amb èxit!");

            int codiRes = generarCodiReserva();
            System.out.println("Codi de reserva: " + codiRes);

            // l'arraylist dadesReserva conté
            // ("tipusHabitacio","preuTotalReserva","serve1","servei2")

            // afegim al ArrayList dadesReserves les dades tpus i preu total reserva
            dadesReserva.add(0, tipusHabitacio);
            dadesReserva.add(1, Float.toString(preuTotalReserva));// convertir a String el float total

            // fem una copia de dadesReserva abans d'afegir-la al hasmap reserves
            ArrayList<String> copiaDadesReserva = new ArrayList<>(dadesReserva);

            // afegim les dades al hashmap reserves
            reserves.put(codiRes, copiaDadesReserva);

            dadesReserva.clear();// netegem el arraylist per a la seguent reserva

            // Després de fer la reserva recorrem el hashmap disponibilitatHabitacions
            // amb un "foreach" per actualitzar la disponibilitat d'habitacions
            for (String tipo : disponibilitatHabitacions.keySet()) {
                if (tipo.equals(tipusHabitacio)) {
                    disponibilitatHabitacions.put(tipo, disponibilitatHabitacions.get(tipo) - 1);
                }
            }

        } else {
            System.out.println("No hi ha habitacions com les demanades pel client. Prova amb un altre tipus.");
            sc.next();
        }

    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        int resp = 0;
        String tipus = "";
        do {
            System.out.print("\nSeleccione tipus d’habitació: ");

            if (sc.hasNextInt()) {
                resp = sc.nextInt();

                switch (resp) {
                    case 1:
                        tipus = "Estàndard";
                        break;
                    case 2:
                        tipus = "Suite";
                        break;
                    case 3:
                        tipus = "Deluxe";
                        break;
                    default:

                }
            } else {
                System.out.println("Entrada no vàlida. S'ha d'indicar un nombre entre 1 i 3, ambdos inclosos.");
                sc.next();
            }
        } while (resp < 1 || resp > 3);
        return tipus;
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {

        System.out.println("\n Tipus d'habitacions disponibles: ");
        System.out.println(
                "\n 1. Estàndard (" + disponibilitatHabitacions.get("Estàndard") + " disponibles) - "
                        + preusHabitacions.get(TIPUS_ESTANDARD) + " € per nit."
                        + "\n 2. Suite (" + disponibilitatHabitacions.get("Suite") + " disponibles) - "
                        + preusHabitacions.get(TIPUS_SUITE) + " € per nit."
                        + "\n 3. Deluxe (" + disponibilitatHabitacions.get("Deluxe") + " disponibles) - "
                        + preusHabitacions.get(TIPUS_DELUXE) + " € per nit.");

        String tipusHabitTriada = seleccionarTipusHabitacio(); // tipus
        // Comprovem que hi haja habitacions lliures del tipus triat,
        // si hi ha retorna el tipus d'habitació escollit
        // si no hi ha retorna null
        if (disponibilitatHabitacions.get(tipusHabitTriada) > 0) {
            return tipusHabitTriada;
        } else {
            return null;
        }

    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        ArrayList<String> serveis = new ArrayList<>();
        String servei = "";
        String textTriarServei = "\n Serveis addicionals (0-4): " +
                "0. Finalitzar \n" +
                "1. " + SERVEI_ESMORZAR + " (" + preusServeis.get(SERVEI_ESMORZAR) + " €),\n" +
                "2. " + SERVEI_GIMNAS + " (" + preusServeis.get(SERVEI_GIMNAS) + " €),\n" +
                "3. " + SERVEI_SPA + " (" + preusServeis.get(SERVEI_SPA) + "€),\n" +
                "4. " + SERVEI_PISCINA + " (" + preusServeis.get(SERVEI_PISCINA) + "€)\n";
        int resp = -1;
        System.out.print(textTriarServei);
        do {

            System.out.println("\nVol afegir un servei? (s/n):");
            String resposta = sc.next();
            sc.nextLine(); // neteja el buffer

            if (resposta.equalsIgnoreCase("s")) {

                resp = llegirEnter(" Seleccione servei: ");

                if (resp == 0) {

                    System.out.println("S'ha finalitzat la introducció de serveis addicionals.");
                    break;

                } else if (resp > 0 && resp < 5) {

                    switch (resp) {
                        case 1:
                            servei = "Esmorzar";
                            break;
                        case 2:
                            servei = "Gimnàs";
                            break;
                        case 3:
                            servei = "Spa";
                            break;
                        case 4:
                            servei = "Piscina";
                            break;
                        default:

                    }
                    if (!serveis.contains(servei)) {
                        System.out.println("Servei afegit:" + servei);
                        serveis.add(servei);
                    } else {
                        System.out.println("Servei ja escollit, tria'n un altre.");
                    }
                } else {
                    System.out.println("Opció incorrecta. Has d'escollir un número entre 0 i 4.");
                }

            } else if (resposta.equalsIgnoreCase("n")) {
                System.out.println("Calculem el total...");
                resp = 0;
                break;
            } else {
                System.out.println("Resposta incorrecta. Escriu 's' per sí o 'n' per no.");
            }

        } while (resp != 0 && serveis.size() < 4);

        return serveis;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        float total = 0, tot = 0;

        total += preusHabitacions.get(tipusHabitacio);

        if (serveisSeleccionats.size() > 0) {
            for (int i = 0; i < serveisSeleccionats.size(); i++) {

                total += preusServeis.get(serveisSeleccionats.get(i));

            }
        }

        tot = total + total * IVA;// float IVA = 0.21f
        return tot;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        int codiAleatori = 0;
        boolean esTroba;
        do {
            codiAleatori = (int) (Math.random() * 900 + 100);// Genera un codi aleatori entre 100 y 999
            esTroba = false; // reiniciem a false en cada iteració del do-while

            // busca si el codi creat en el array(int) de codis de reserva
            for (int codi : codisReserva) {
                // si es troba un codi duplicat, actualiza esTroba a true per continuar generant
                // codis
                if (codi == codiAleatori) {
                    esTroba = true;
                    break;
                }
            }
        } while (esTroba); // repetix sols si està duplicat

        // bucle for que recorre l'array per trobar la primera posició buida i guardar
        // el nou codi
        for (int i = 0; i < codisReserva.length; i++) {

            if (codisReserva[i] == 0) {
                codisReserva[i] = codiAleatori;
                break;
            }
        }
        return codiAleatori;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        // Demanar codi, tornar habitació i eliminar reserva

        int resp = llegirEnter(" Introdueix el codi de reserva: ");

        for (Integer codi : reserves.keySet()) {
            if (codi.equals(resp)) {
                System.out.println("Reserva trobada!");
                // obtindre tipusHabitacio de la reserva a borrar
                String tipusH = reserves.get(codi).get(0);

                reserves.remove(codi); // elimina l'entrada associada a este codi de reserva del hashmap reserves

                // Recorrem el hashmap disponibilitatHabitacions amb un "foreach"
                // per actualitzar la disponibilitat d'habitacions
                for (String tipo : disponibilitatHabitacions.keySet()) {
                    if (tipo == tipusH) {
                        disponibilitatHabitacions.put(tipo, disponibilitatHabitacions.get(tipo) + 1);
                    }
                }

                System.out.println("Reserva alliberada correctament.");

                System.out.println("Disponibilitat actualitzada.");
                break; // eixir del bucle for

            } else {
                System.out.println("La reserva no existeix.");
            }
        } // final del for each

    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        System.out.println("\n===== DISPONIBILITAT D'HABITACIONS =====");
        System.out.printf("%3s %15s %15s \n", "Tipus", "Lliures", "Ocupades");

        for (String tipo : preusHabitacions.keySet()) {
            mostrarDisponibilitatTipus(tipo);
        }

    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {

        // cas inicial : array buit o null
        if (codis.length == 0 || codis == null) {
            return;
        }

        // comprovar que codisReserva[0] != 0
        if (codis[0] != 0) {
            // existeix el codi passat entre les reserves guardades?
            if (reserves.containsKey(codis[0])) {
                // reserves associades a la clau continguda en codis[0]
                ArrayList<String> dades = reserves.get(codis[0]);

                // comprovem el primer element de l'array siga igual que tipus Hab
                // i fem una crida a mostrarDadesReserva() amb el primer codi de reserva no 0
                if (dades.get(0).equals(tipus)) {
                    mostrarDadesReserva(codis[0]);
                }

            }
        }
        // bloc de codi que s'executarà fins que el array codis tinga elements
        if (codis.length > 1) {
            // recortar el vector de codis
            int newCodis[] = new int[codis.length - 1];

            // copia del vector de codis anterior
            System.arraycopy(codis, 1, newCodis, 0, newCodis.length);

            // cridem al mètode de nou fins que array codis es buide
            llistarReservesPerTipus(newCodis, tipus);
        }

    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        boolean continua = true;
        System.out.println("\n===== CONSULTAR RESERVA =====");
        while (continua) {
            int codi = llegirEnter("\nIntrodueix el codi de reserva: ");

            if (codi == 0) {
                System.out.println("Tornant al menú principal...");
                continua = false;

            } else {

                mostrarDadesReserva(codi);
                // comprovar que hi ha entrada d'usuari
                if (sc.hasNext()) {
                    System.out.print("\nVols consultar una altra reserva? (s/n): ");

                    String resposta = sc.next();
                    sc.nextLine(); // neteja buffer

                    if (resposta.equalsIgnoreCase("n")) {
                        continua = false;
                    }
                } else {
                    continua = false;
                }
            }

        }

    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");

        String tipusHabitacio = seleccionarTipusHabitacio();
        if (tipusHabitacio == null) {
            return;
        }
        int contador = 0;
        // foreach per a filtrar els codis que están en reserves
        for (int codi : codisReserva) {
            if (reserves.containsKey(codi)) {
                contador++;// conta les reserves q hi ha
            }
        }
        // array d'enters amb codis vàlids
        int codisValids[] = new int[contador]; // tindrà tants elements com reserves hi ha

        int i = 0;
        for (int codi : reserves.keySet()) {
            codisValids[i++] = codi;
        }
        
        // crida al mètode recursiu només si hi ha codis/reserves
        if (codisValids.length == 0) {
            System.out.println("No hi ha reserves d'aquest tipus.");
        } else {
            llistarReservesPerTipus(codisValids, tipusHabitacio);
        }

    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {

        System.out.println("\nDades de la reserva: " + codi);

        // Comprovem que hi aparega en reserves el codi de reserva donat per l'usuari
        if (reserves.containsKey(codi)) {
            System.out.println("- Tipus d'habitació: " + reserves.get(codi).get(0));
            System.out.println("- Preu total: " + reserves.get(codi).get(1) + " €");
            System.out.println("- Serveis addicionals: ");

            if (reserves.get(codi).size() > 2) {
                for (int i = 2; i < reserves.get(codi).size(); i++) {
                    System.out.println("    -> " + reserves.get(codi).get(i));
                }
            } else {
                System.out.println("No s'han contractat serveis addicionals.");
            }

        } else {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");

        }

    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    /*
     * s'ha modificat lleugerament per a que comprove realment que lo que afegeix
     * l'usuari siga un enter
     * en cas contrari, torne un missatge d'error.
     */
    static int llegirEnter(String missatge) {

        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
            System.out.print(missatge);

            if (sc.hasNextInt()) {
                valor = sc.nextInt();
                correcte = true;

            } else {
                System.out.println("Opció incorrecta, has d'escriure un nombre vàlid.");
                sc.next();
            }
            sc.nextLine();// neteja el buffer
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t\t" + ocupades);
    }
}
