
package com.mycompany._eng_050_4;

import java.time.Duration;
import java.time.LocalTime;


public class ResultTrain {
    public char startSt;
    public char destinationSt;
    public LocalTime depatureTime;
    public Duration TravelDuration;
    
    ResultTrain(char s, char d, LocalTime t, Duration du){
        this.startSt = s;
        this.destinationSt = d;
        this.depatureTime = t;
        this.TravelDuration = du;
    }
    
    public String toString(){
        String s = "Train Starts at "+this.depatureTime.toString()+" from Station: "+this.startSt;
        return s;
    }
}
