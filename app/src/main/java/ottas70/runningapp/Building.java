package ottas70.runningapp;

import java.io.Serializable;

/**
 * Created by ottovodvarka on 21.01.17.
 */

public class Building implements Serializable {

    private String ownersName;
    private String address;
    private BuildingType buildingType;
    private int price;

    public Building(String ownersName, String address, int buildingType, int price) {
        this.ownersName = ownersName;
        this.address = address;
        this.price = price;
        setBuildingType(buildingType);
    }

    private void setBuildingType(int buildingType) {
        switch (buildingType) {
            case 1:
                this.buildingType = BuildingType.FIRST_TYPE;
                break;
            case 2:
                this.buildingType = BuildingType.SECOND_TYPE;
                break;
            case 3:
                this.buildingType = BuildingType.THIRD_TYPE;
                break;
        }
    }

    public int getBuildingTypeInInteger() {
        switch (buildingType) {
            case FIRST_TYPE:
                return 1;
            case SECOND_TYPE:
                return 2;
            case THIRD_TYPE:
                return 3;
        }
        return 1;
    }

    public String getAddress() {
        return address;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }
}
