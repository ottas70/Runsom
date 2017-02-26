package ottas70.runningapp;

import java.util.ArrayList;

import ottas70.runningapp.Models.SortInfo;

public class SortInfoBuilder {
    private ArrayList<Integer> types;
    private boolean every;
    private String runner;
    private boolean asc;
    private boolean desc;
    private int minPrice;
    private int maxPrice;
    private String address;

    public SortInfoBuilder setTypes(ArrayList<Integer> types) {
        this.types = types;
        return this;
    }

    public SortInfoBuilder setEvery(boolean every) {
        this.every = every;
        return this;
    }

    public SortInfoBuilder setRunner(String runner) {
        this.runner = runner;
        return this;
    }

    public SortInfoBuilder setAsc(boolean asc) {
        this.asc = asc;
        return this;
    }

    public SortInfoBuilder setDesc(boolean desc) {
        this.desc = desc;
        return this;
    }

    public SortInfoBuilder setMinPrice(int minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public SortInfoBuilder setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public SortInfoBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public SortInfo createSortInfo() {
        return new SortInfo(types, every, runner, asc, desc, minPrice, maxPrice, address);
    }
}