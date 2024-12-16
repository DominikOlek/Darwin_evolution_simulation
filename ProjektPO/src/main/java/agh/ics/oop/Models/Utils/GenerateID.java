package agh.ics.oop.Models.Utils;

public class GenerateID {
    private static int id=0;
    public static int GetID() {
        id+=1;
        return id;
    }
}


