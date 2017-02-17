package ottas70.runningapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ottovodvarka on 14.02.17.
 */

public class SortInfo implements Serializable{

    private ArrayList<Integer> types;
    private boolean every;
    private String runner;
    private boolean asc;
    private boolean desc;
    private int minPrice;
    private int maxPrice;
    private String address;

    public SortInfo(ArrayList<Integer> types, boolean every, String runner, boolean asc, boolean desc, int minPrice, int maxPrice, String address) {
        this.types = types;
        this.every = every;
        this.runner = runner;
        this.asc = asc;
        this.desc = desc;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.address = address;
    }

    public ArrayList<Integer> getTypes() {
        return types;
    }

    public boolean isEvery() {
        return every;
    }

    public String getRunner() {
        return runner;
    }

    public boolean isAsc() {
        return asc;
    }

    public boolean isDesc() {
        return desc;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public String getAddress() {
        return address;
    }
}
