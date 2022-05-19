package com.israel.livraisonexpresspos.utils;
//todo : change this name for "orderStatesWithBadges"
public class CourseStatus {
    public static final int CODE_UNASSIGNED = 0;
    public static final int CODE_ASSIGNED = 1;
    public static final int CODE_RUNNING = 23;
    public static final int CODE_STARTED = 2;
    public static final int CODE_INPROGRESS = 3; //correspond a charger
    public static final int CODE_DELIVERED = 4;
    public static final int CODE_CANCELED = 5;
    public static final int CODE_RELAUNCH = 6;
    public static final int CODE_TO_VALIDATE = 7;
    public static final int CODE_EXPIRED = 8;

    public static final String BADGE_UNASSIGNED = "unassigned";
    public static final String BADGE_ASSIGNED = "pending";
    public static final String BADGE_STARTED = "started";
    public static final String BADGE_INPROGRESS = "inprogress";
    public static final String BADGE_RUNNING = "running";
    public static final String BADGE_DELIVERED = "delivered";
    public static final String BADGE_CANCELLED = "canceled";
    public static final String BADGE_MY_RACES = "racer_race";
    public static final String BADGE_OF_TODAY = "of-today";
    public static final String BADGE_CONFIRMED = "confirmed";
    public static final String BADGE_TO_BE_TREAT = "a_traiter";
    public static final String BADGE_TO_VALIDATE = "to_validate";

    public static String getStatusHuman(int code){
        switch (code){
            case 1: return "Assignée";
            case 2: return "Démarrée";
            case 3: return "En cours de livraison";
            case 4: return "Terminée";
            case 5: return "Annulée";
            case 6: return "A Relancer";
            case 7: return "A Valider";
            default: return "Non assignée";
        }
    }
}
