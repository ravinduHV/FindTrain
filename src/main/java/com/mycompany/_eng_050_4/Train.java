
package com.mycompany._eng_050_4;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Train {
    public char StartingStation;
    public char DestinationStation;
    public float TravelDistance;
    private Duration TravelTime; //Seconds
    private final Duration StopTime = Duration.ofSeconds(10*60);
    private final float speed = 30;
    public LocalTime operatesFrom = LocalTime.of(6, 0);
    public LocalTime operatesTo = LocalTime.of(20, 0);
    
    Train(char startingSt, char DestinationSt, float travelDistance){
        this.StartingStation = startingSt;
        this.DestinationStation = DestinationSt;
        this.TravelDistance = travelDistance;
        calculateTravelTime();
    }
    
    private void calculateTravelTime(){
        this.TravelTime = Duration.ofSeconds((long)((this.TravelDistance/this.speed)*3600));
    }
    
    public String CheckAvailability(LocalTime from, LocalTime to){
        LocalTime temp = operatesFrom;
        char tempStation = this.StartingStation;
        
        while(temp.isBefore(operatesTo)){
            temp = temp.plusSeconds(StopTime.toSeconds());
            if((temp.equals(from) || temp.isAfter(from)) && (temp.equals(to) || temp.isBefore(to))){
                System.out.println(temp);
            }
            temp = temp.plusSeconds(TravelTime.toSeconds());
            tempStation = (tempStation == this.StartingStation)? this.DestinationStation:this.StartingStation; 
        }
        return null;
    }
    
    public List<ResultTrain> CheckAvailableTrain(LocalTime t, char st){
        LocalTime temp = operatesFrom;
        char tempStation = this.StartingStation;
        
        List<ResultTrain> result = new ArrayList<>();
        
        while(temp.isBefore(operatesTo)){
            temp = temp.plusSeconds(StopTime.toSeconds());
            if((temp.equals(t) || temp.isAfter(t)) && tempStation == st){
                //System.out.println("From Station "+tempStation + " at "+temp);
                result.add(new ResultTrain(tempStation,(tempStation == this.StartingStation)? this.DestinationStation:this.StartingStation,temp,this.TravelTime));
            }
            temp = temp.plusSeconds(TravelTime.toSeconds());
            tempStation = (tempStation == this.StartingStation)? this.DestinationStation:this.StartingStation; 
        }
        return result;
    }
    
    public void printSchedule(){
        LocalTime temp = operatesFrom;
        char tempStation = this.StartingStation;
        
        while(temp.isBefore(operatesTo)){
            temp = temp.plusSeconds(StopTime.toSeconds());
            if(temp.isBefore(operatesTo)){
                System.out.println("At Station "+tempStation + " From "+temp.toString());
            }
            temp = temp.plusSeconds(TravelTime.toSeconds());
            tempStation = (tempStation == this.StartingStation)? this.DestinationStation:this.StartingStation; 
        }
    }
    
    @Override
    public String toString(){
        return "Train From "+this.StartingStation  + " to " + this.DestinationStation + 
                " Travel Distance of "+this.TravelDistance + "Travel Time of "+this.TravelTime.toMinutes() +" Minutes";
    }

    public boolean equals(Train other){
        return (this.StartingStation == other.StartingStation) &&
                this.DestinationStation == other.DestinationStation;
    }
    public boolean thisTrain(char S, char D){
        return (this.StartingStation == S) &&
                this.DestinationStation == D;
    }
}
