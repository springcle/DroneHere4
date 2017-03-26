package com.santamaria.dronehere.data;

import java.util.List;

/**
 * Created by NAKNAK on 2016-11-19.
 */
public class DroneResistance {
    String dr_resistance;
    List<MemDrone> mem_drone;
    public String getDroneResistance() {
        return dr_resistance;
    }
    public List<MemDrone> getMemResultDrone() {
        return mem_drone;
    }

    public void setDroneResistance(String dr_resistance) {
        this.dr_resistance = dr_resistance;
    }
}
