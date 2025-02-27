/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int nThread) throws InterruptedException {

        ArrayList<BlackListThread> threads = new ArrayList<>();
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;

        int nListPerThread = skds.getRegisteredServersCount() / nThread;
        int remain = skds.getRegisteredServersCount() % nThread;

        System.out.println("Listas en total :" + nListPerThread);

        System.out.println("Sobrantes para repartir entre los hilos: " + remain);

        int start = 0;
        for(int i = 0; i < nThread ; i++){
            int numberListThread = nListPerThread + (i < remain? 1 : 0);
            System.out.println("Listas totales por hilo: " + numberListThread);
            BlackListThread thread = new BlackListThread(start + numberListThread,  ocurrencesCount, start, ipaddress, blackListOcurrences, checkedListsCount);
            System.out.println("------- " + numberListThread + " start: "+  start + " nList: " + numberListThread + " ocurrencesCount: " + ocurrencesCount + " blackListOcurrene: " + blackListOcurrences + " ipaddress: " + ipaddress);
            threads.add(thread);
            thread.start();
            start += numberListThread;
            System.out.println("siguiente tope: " + numberListThread);

        }


        for(BlackListThread thread : threads){
            thread.join();
        }

        ocurrencesCount = getTotalOcurrences(threads);

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }




        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});

        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    private int getTotalOcurrences(ArrayList<BlackListThread> threads){
        int total = 0;
        for(BlackListThread thread: threads){
            total += thread.getOcurrencesCount();
        }
        return total;
    }

    private int getTotalListCount(ArrayList<BlackListThread> threads){
        
    }
    
    
}
