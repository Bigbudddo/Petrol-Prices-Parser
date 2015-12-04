package com.stuart_harrison.petrolpricesparser;

public class FuelType {
    private String typeName;
    private float highest;
    private float average;
    private float lowest;

    public float Highest() { return highest; }
    public float Average() { return average; }
    public float Lowest() { return lowest; }

    public String Name() { return typeName; }
    public String HighestString() { return Float.toString(highest); }
    public String AverageString() { return Float.toString(average); }
    public String LowestString() { return Float.toString(lowest); }

    public FuelType(String name, String high, String low, String average) {
        this.typeName = name;
        this.highest = Float.parseFloat(high);
        this.average = Float.parseFloat(average);
        this.lowest = Float.parseFloat(low);
    }
}
