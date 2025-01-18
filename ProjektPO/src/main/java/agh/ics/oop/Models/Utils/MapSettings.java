package agh.ics.oop.Models.Utils;

public record MapSettings(
        boolean whichMutation ,
        int lenGen,
        int energyFromEat,
        int energyToAdult,
        int energyForMulti,
        int startGrassNumber,
        int startAnimalNumber,
        int numberOfGrowing,
        int startEnergy,
        int minMutation,
        int maxMutation,
        boolean whichGrass,
        boolean saveToFile
){};
