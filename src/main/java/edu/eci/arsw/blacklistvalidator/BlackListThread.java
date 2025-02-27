package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class BlackListThread extends Thread{

    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int nList;
    private int ocurrencesCount;
    private int startT;

    private  String ipaddress;
    private int checkedList = 0;
    LinkedList<Integer> blackListOcurrences;

    private int checkedListsCount;



    public BlackListThread(int nList, int ocurrencesCount, int startT, String ipaddress,  LinkedList<Integer> blackListOcurrences, int checkedListsCount) {
        this.nList = nList;
        this.ocurrencesCount = ocurrencesCount;
        this.startT = startT;
        this.ipaddress = ipaddress;
        this.blackListOcurrences = blackListOcurrences;
        this.checkedListsCount = checkedListsCount;
    }

    public int getOcurrencesCount(){
        return ocurrencesCount;
    }
    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();


        System.out.println("IP" + ipaddress + " ocurrencesCount: " + ocurrencesCount + " start: " + startT + "nList: " + nList);
        for (int i = startT; i < nList && ocurrencesCount < BLACK_LIST_ALARM_COUNT; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){

                blackListOcurrences.add(i);

                ocurrencesCount++;
            }


        }


    }



}
