package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;

public class BlackListThread extends Thread{

    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int nList;
    private int ocurrenceCount;

    LinkedList<Integer> blackListOcurrences;

    public BlackListThread(int nList, int ocurrenceCount, LinkedList<Integer> blackListOcurrences){
        nList = this.nList;
        ocurrenceCount = this.ocurrenceCount;
        blackListOcurrences = this.blackListOcurrences;
    }

    public void run(){

    }
}
