package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Enums.ChangeType;
import agh.ics.oop.Models.Sprite.StatisticalObject;
import agh.ics.oop.UI.SimulationPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalStatistics implements Observer, StatisticReport {
    List<Integer> dna;
    int activeGen;
    int energy;
    int eat;
    int childNr;
    int allChild;
    int age;
    int dead=0;
    Vector2D position= new Vector2D(0,0);
    SimulationPresenter presenter;

    public AnimalStatistics(SimulationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void update(StatisticalObject animal, ChangeType changeType) {
        switch (changeType) {
            case Gen -> dna = animal.getDna().getPartToMultiplication(1,true);
            case ActiveGen -> {activeGen = animal.getDna().getActual(); position=animal.getPosition();energy=animal.getEnergy();age = animal.getCurrentDay();}
            case Eat -> {eat = animal.getHowManyEaten(); energy=animal.getEnergy();}
            case Dead -> {dead=animal.getCurrentDay(); age=0;}
            case Child -> {childNr=animal.getNumberOfChild();energy=animal.getEnergy();}
            case AllChild -> {allChild=animal.getAllChild();}
        }
        presenter.getAnimalStat();
    }

    public Map<String,String> getStatistics(){
        Map<String,String> data=new HashMap<>();
        data.put("ActiveGen: ",Integer.toString(activeGen));
        if (dead== 0) data.put("Age: ",Integer.toString(age));
        else data.put("Age of dead: ",Integer.toString(dead));
        data.put("Energy: ",Integer.toString(energy));
        data.put("Number of child: ",Integer.toString(childNr));
        data.put("Number of descendant: ",Integer.toString(allChild));
        data.put("Number of eat: ",Integer.toString(eat));
        data.put("Position: ",position.toString());
        data.put("Genotype: ",dna.toString());
        return data;
    }

    @Override
    public String toString() {
        return "AnimalStatistics{" +
                "dna=" + dna +
                ", activeGen=" + activeGen +
                ", energy=" + energy +
                ", eat=" + eat +
                ", childNr=" + childNr +
                ", allChild=" + allChild +
                ", age=" + age +
                ", dead=" + dead +
                ", position=" + position +
                '}';
    }
}
