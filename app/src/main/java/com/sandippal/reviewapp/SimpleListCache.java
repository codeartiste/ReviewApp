package com.sandippal.reviewapp;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SimpleListCache  {


    private List<ReviewStringObj> resl ;
    private int cacheSize;
    public SimpleListCache(int sz){
        cacheSize = sz;
        resl = new LinkedList<ReviewStringObj>();
    }
    int checkAndInsertFreshList(List <ReviewStringObj> tList){

        // Algorithm to check if the elements are in the cache or not
        // If yes , don't insert
        // To detect: Assumption is if the list starts with same element , then cache is fresh
        // if not check the size of the cache, delete if the current size > sz
        // Also check if the entry is fresh or not, if stale then it either exists or needs to be inserted
        // use other method to append to cache using pages

        if(resl.size() == 0){
            insertAtEnd(tList) ;
        }else{

            ListIterator<ReviewStringObj>
                    freshIt = tList.listIterator();

            while (freshIt.hasNext()) {

                ReviewStringObj obj1  = freshIt.next() ;
                int index = resl.indexOf(obj1);
                System.out.println("Index of match = "+ index);
                obj1.print();
                if(index == 0){
                    return 1;
                }else if(index > 0) {

                    resl.addAll(0, tList.subList(0 , index -1  ));
                    return 1;
                } else{
                    //do nothing . check next element
                }
            }
            // All elements are fresh thus insert the entire list
            resl.addAll(0, tList);
            return 0;

        }
        return 1;
    }

    public List<ReviewStringObj> getResl(){
        return resl;

    }

    boolean insertAtEnd(List <ReviewStringObj> tList) {

        if( resl.size() < cacheSize - 20){

            resl.addAll(tList);
            return true;

        }
        return false;
    }
    void debugPrintCache(){
        ListIterator<ReviewStringObj>
                iterator = resl.listIterator();

        // Printing the iterated values

        while (iterator.hasNext()) {
            ReviewStringObj obj1  = iterator.next() ;
            obj1.print();
        }

    }

    int getSize(){

        return resl.size();

    }


    void invalidate(){

        resl.clear();
    }





}
