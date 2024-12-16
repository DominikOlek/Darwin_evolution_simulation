package agh.ics.oop.Models.Maps;

import agh.ics.oop.Models.Enums.MoveDirection;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Sprite.Grass;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.Models.Utils.Vector2D;

import java.util.*;

import static java.lang.Math.sqrt;

public class MainMap {
    //mapa pozwoli jednostkowo dostać się do elementów na polu, hashset do konkretnego elementu w nim i można też po nim iterować
    private static final Map<Vector2D, Set<Animal>> animals = new HashMap<Vector2D, Set<Animal>>();
    private final Map<Vector2D, Grass> grasses = new HashMap<Vector2D, Grass>();

    private final int startGrassNumber;
    private final int numberOfGrowing;
    private final int lenGen;
    private final int energyFromEat;
    private final int growForDay;
    private final int energyToAdult;
    private final int energyForMulti;

    private Boundary size;
    private int equator;
    private int equatorHeight;


    public MainMap(Boundary size, MapSettings settings) {
        this.startGrassNumber = settings.startGrassNumber();
        this.size = size;
        this.numberOfGrowing = settings.numberOfGrowing();
        this.equator = size.upperRight().getY()/2;
        this.equatorHeight = size.upperRight().getY()/5;

        this.lenGen = settings.lenGen();
        this.energyFromEat = settings.energyFromEat();
        this.growForDay = settings.growForDay();
        this.energyToAdult = settings.energyToAdult();
        this.energyForMulti = settings.energyForMulti();
    }

    //runDay
        //Kill
        //Move (na tej podstawie stworzyć tabele z pozycjami w danym kroku)
        //Eat (wykonywany nie na liscie zwierząt tylko pozycji z move)
        //Reproduce (też na liscie z move)
        //Grow (tak by nie rosło na tych na których jest)

    //hashmap jest aktualizowana tak żeby iterować tylko po pozycjach gdzie coś jeszcze jest, hasset przechowuje jakie zwierzęta są na danym polu
    //czy TreeSet się opłaca ? Jedzenie Reprodukcja a Ruchy usuwanie dodawanie
    public boolean CanMoveTo(Vector2D pos) {
        return (size.lowerLeft().precedes(pos) && size.upperRight().follows(pos));
    }

    //move
    public boolean moveTo(Animal animal) {
        Vector2D toPos = animal.getPosition().add(animal.getDirection().toUnitVector());
        boolean status = false;
        if(!CanMoveTo(toPos)) {
            status = true;
            if(toPos.getX()<0){
                toPos.setX(size.upperRight().getX());
            }else if(toPos.getX()>size.upperRight().getX()){
                toPos.setX(0);
            }else if(toPos.getY()<0){
                toPos.setY(0);
                animal.fullRotate();
            }else if(toPos.getY()>size.upperRight().getY()){
                toPos.setY(size.upperRight().getY());
                animal.fullRotate();
            }
        }
        removeAnimal(animal.getPosition(),animal);
        addAnimal(toPos,animal);
        animal.move(toPos);
        return status;
    }

    public static void addAnimal(Vector2D pos, Animal animal) {
        // computeIfAbsent tworzy nowy HashSet, jeśli jeszcze nie istnieje
        animals.computeIfAbsent(pos, _ -> new HashSet<>()).add(animal);
    }

    public static void removeAnimal(Vector2D pos, Animal animal) {
        if (animals.containsKey(pos)) {
            Set<Animal> set = animals.get(pos);
            set.remove(animal);
            if (set.isEmpty()) {
                animals.remove(pos);
            }
        }
    }


    private void killsAnimals(){
        Iterator<Map.Entry<Vector2D, Set<Animal>>> iterat = animals.entrySet().iterator();

        while (iterat.hasNext()) {
            Map.Entry<Vector2D, Set<Animal>> set = iterat.next();
            Set<Animal> values = set.getValue();
            Iterator<Animal> iterator = values.iterator();

            while (iterator.hasNext()) {
                Animal anim = iterator.next();
                if (anim.getEnergy() <= 0) {
                    iterator.remove();
                }
            }

            if (values.isEmpty()) {
                animals.remove(set.getKey());
            }
        }
    }

    public void eating(){
        for(Map.Entry<Vector2D, Grass> grass : grasses.entrySet()) {
            eatingAnimals(grass.getKey());
        }
    }

    public void eatingAnimals(Vector2D pos) { // OD RAZU MAMY INFORMACJE O 2 NAJSILNIEJSZYCH LEPIEJ PRZENIEŚĆ DO OSOBNEJ
        Set<Animal> values = animals.get(pos);
        TreeSet<Animal> treeSet = new TreeSet<Animal>();
        treeSet.addAll(values);
        Animal oneAnimal = treeSet.getFirst();
        treeSet.getFirst().eat(energyFromEat);
        if(values.size()>1){
            treeSet.removeAll(values);
            Animal secondAnimal = treeSet.getFirst();
        }
    }



    //AnimalsAt
    public Set<Animal> getAnimalsAt(Vector2D pos) {
        return animals.get(pos);
    }

    //isGrassAt
    public boolean isGrassAt(Vector2D pos) {
        return grasses.containsKey(pos);
    }

    public void growGrass(int howMany){
        int toGood = howMany/5;
        //generowanie (pas 0-(sr-szer/2) i (sr+szer/2) | równik sr+-szer)
    }

}
