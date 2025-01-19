package agh.ics.oop.Models.Maps;

import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Sprite.Grass;
import agh.ics.oop.Models.Sprite.LiveObject;
import agh.ics.oop.Models.Sprite.MapObject;
import agh.ics.oop.Models.Utils.*;
import javafx.scene.control.RadioMenuItem;

import java.util.*;

public class MainMap extends WorldMap {
    //mapa pozwoli jednostkowo dostać się do elementów na polu, hashset do konkretnego elementu w nim i można też po nim iterować
    private final Map<Vector2D, Set<LiveObject>> animals = new HashMap<Vector2D, Set<LiveObject>>();
    private final Map<Vector2D, MapObject> grasses = new HashMap<Vector2D, MapObject>();
    private final List<MapObserver> observers = new ArrayList<MapObserver>();
    private final MapSettings settings;
    private final Boundary size;
    private final int equator;
    private final int equatorHeight;
    private boolean run = true;

    //MapSettings - wszystkie opcje jakie będą ustawiane
    public MainMap(Boundary size, MapSettings settings) {
        super();
        this.settings = settings;
        this.size = size;
        this.equator = size.upperRight().getY()/2;
        this.equatorHeight = size.upperRight().getY()/5;

        generateStartAnimal();
        setSurface(size);
        setStartEnergy(settings.startAnimalNumber(),settings.startEnergy());
        growGrass(settings.startGrassNumber());
    }
    //runDay
    //Kill
    //Move (wykonywany dla każdego zwierzaka z małmi przerwami)
    //Eat (wykonywany nie na liscie zwierząt tylko pozycji z move)
    //Reproduce (też na liscie z move)
    //Grow

    //wykonywane przez simulation co dnia, aktualnie cykl jedzenie i rozmnażania połączony
    @Override
    public void doDay(){
        killsAnimals();
        mapChanged("Kills");
        if(animals.isEmpty())
            run = false;

        moveTo(copyAnimalCollection());
        allMove();

        cycleEatMulti();
        mapChanged("EatAndMulti");

        growGrass(settings.numberOfGrowing());
        mapChanged("Grass");
        currentDay++;
    }

    private void moveTo(Set<LiveObject> AllAnimals) {
        try {
            for(LiveObject animal:AllAnimals){
                Vector2D toPos = animal.getPosition().add(animal.getDirection().toUnitVector());
                boolean status = false;
                if (!canMoveTo(toPos)) {
                    status = true;
                    if (toPos.getX() < 0) {
                        toPos.setX(size.upperRight().getX());
                    } else if (toPos.getX() > size.upperRight().getX()) {
                        toPos.setX(0);
                    }
                    if (toPos.getY() < 0) {
                        toPos.setY(0);
                        animal.fullRotate();
                    } else if (toPos.getY() > size.upperRight().getY()) {
                        toPos.setY(size.upperRight().getY());
                        animal.fullRotate();
                    }
                }
                removeAnimal(animal.getPosition(), animal);
                addAnimal(toPos, animal);
                animal.move(toPos);//wykonuje przypisanie lokalizacji i zmiany codzienne zwierzaka
                mapChanged("Move");
                Thread.sleep(50);
            }
        }catch (Exception e){
            System.out.println();
        }
    }

    //iteruje i usuwa  zwierzaki z energia == 0
    private void killsAnimals(){
        Iterator<Map.Entry<Vector2D, Set<LiveObject>>> iteratMap = animals.entrySet().iterator();

        while (iteratMap.hasNext()) {
            Map.Entry<Vector2D, Set<LiveObject>> set = iteratMap.next();
            Set<LiveObject> values = set.getValue();
            Iterator<LiveObject> iteratSet = values.iterator();

            while (iteratSet.hasNext()) {
                LiveObject anim = iteratSet.next();
                if (anim.getEnergy() <= 0) {
                    iteratSet.remove();
                    anim.isDead();
                    changeNumberOfAnimals(false,anim);
                }
            }

            if (values.isEmpty()) {
                iteratMap.remove();
                changeNumberOfUsePlaces(-1);
            }
        }
    }

    //wykonanie cyklu dla każdego pola, sprawdzenie czy można zjeść i się rozmnożyć
    private void cycleEatMulti(){
        for(Map.Entry<Vector2D, Set<LiveObject>> animal : animals.entrySet()) {
            if(animal.getValue().size() == 1 && !isGrassAt(animal.getKey())) {
                continue;
            }
            List<LiveObject> strongest = getTwoStrongest(animal.getKey());
            if(isGrassAt(animal.getKey())){
                strongest.getFirst().eat(settings.energyFromEat());
                grasses.remove(animal.getKey());
                oneEat(settings.energyFromEat());
            }

            if(strongest.size()> 1 && strongest.getLast().getEnergy() >= settings.energyToAdult()){
                multipl(strongest);
            }
        }
    }

    //pobranie 2 najsliniejszych z pola lub jednego i null na index 2
    private List<LiveObject> getTwoStrongest(Vector2D pos){
        List<LiveObject> list = new ArrayList<>();
        Set<LiveObject> values = animals.get(pos);
        TreeSet<LiveObject> treeSet = new TreeSet<LiveObject>(values);
        list.add(treeSet.removeFirst());
        if(!treeSet.isEmpty()){
            list.add(treeSet.removeFirst());
        }
        return list;
    }

    //stworzenie nowego potomka
    private void multipl(List<LiveObject> animals){
        List<Integer> dnaList;
        try {
            dnaList = animals.getFirst().multiplicationWith(animals.getLast(),settings.minMutation(),settings.maxMutation(),settings.whichMutation());
            Dna dna = new Dna(dnaList, settings.lenGen());
            animals.getFirst().multiplicationLostEnergy(settings.energyForMulti());
            animals.getLast().multiplicationLostEnergy(settings.energyForMulti());
            LiveObject child = new Animal(animals.getFirst().getPosition(), dna, 2 * settings.energyForMulti(),settings.energyToAdult());
            addAnimal(child.getPosition(), child);
            child.addParent(animals.getFirst(),animals.getLast());
            changeNumberOfAnimals(true,child);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }


    private void generateStartAnimal(){
        RandomPositionGenerator rand = new RandomPositionGenerator(size.upperRight().getX(),size.upperRight().getY(),settings.startAnimalNumber());
        for (Vector2D position : rand) {
            Dna dna = new Dna(settings.lenGen());
            addAnimal(position, new Animal(position, dna, settings.startEnergy(),settings.energyToAdult()));
            addDna(dna);
        }
    }

    private void mapChanged(String message) {
        for(MapObserver observer : observers){
            observer.mapChanged(this,message);
        }
    }

    private Vector2D randomEquatorPosition() {
        int width = size.upperRight().getX() + 1;
        int minY = Math.max(size.lowerLeft().getY(), equator - equatorHeight/2);
        int maxY = Math.min(size.upperRight().getY(), equator + equatorHeight/2);

        int randX = (int) (Math.random() * width); // od 0 do width - 1
        int randY = minY + (int) (Math.random() * (maxY - minY + 1)); // [minY...maxY]

        return new Vector2D(randX, randY);
    }

    private Vector2D randomOutsideEquatorPosition() {
        int width = size.upperRight().getX() + 1;
        int height = size.upperRight().getY() + 1;

        int minEquatorY = Math.max(size.lowerLeft().getY(), equator - equatorHeight/2);
        int maxEquatorY = Math.min(size.upperRight().getY(), equator + equatorHeight/2);

        while (true) {
            int randX = (int) (Math.random() * width);
            int randY = (int) (Math.random() * height);


            if (randY >= minEquatorY && randY <= maxEquatorY) {
                continue;
            }
            return new Vector2D(randX, randY);
        }
    }

    public void growGrass(int howMany){
        int toGood = howMany/5;
        //int toGood = howMany/5;
        //generowanie (pas 0-(sr-szer/2) i (sr+szer/2) - 20% a równik (sr+-szer) - 80%)
        int planted = 0;
        while (planted < howMany && getNumbersOfGrass() < numbersOfPlaces) {
            // czy trawa będzie na równiku
            double rand = Math.random(); // [0..1)
            Vector2D pos;
            if (rand < 0.8) {
                pos = randomEquatorPosition();
            } else {
                pos = randomOutsideEquatorPosition();
            }
            if (!isGrassAt(pos)) {
                grasses.put(pos, new Grass(pos, settings.energyFromEat()));
                changeGrass(1);
                planted++;
            }
        }
    }

    @Override
    public Set<LiveObject> copyAnimalCollection(){
        Set<LiveObject> AllObj = new HashSet<>();
        for(Set<LiveObject> set:animals.values()) AllObj.addAll(set);
        return AllObj;
    }


    @Override
    public boolean canMoveTo(Vector2D pos) {
        return (size.lowerLeft().precedes(pos) && size.upperRight().follows(pos));
    }

    //porusza zwierzakiem na podstawie jego obrotu

    @Override
    public boolean addAnimal(Vector2D pos, LiveObject animal) {
        if (animal.getEnergy()<0 || animal.getDna().getSize() != settings.lenGen())
            return false;
        // computeIfAbsent tworzy nowy HashSet, jeśli jeszcze nie istnieje
        if(!animals.containsKey(pos)){
            changeNumberOfUsePlaces(1);
        }
        animals.computeIfAbsent(pos, k -> new HashSet<>()).add(animal);
        return true;
    }

    //usuwanie zwierzaka i hashseta gdy trzeba
    @Override
    public boolean removeAnimal(Vector2D pos, LiveObject animal) {
        if (animals.containsKey(pos)) {
            boolean status = false;
            Set<LiveObject> set = animals.get(pos);
            status = set.remove(animal);
            if (set.isEmpty()) {
                animals.remove(pos);
                changeNumberOfUsePlaces(-1);
            }
            return status;
        }else
            return false;
    }

    @Override
    public void addObserver(MapObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MapObserver observer) {
        observers.remove(observer);
    }

    //AnimalsAt
    @Override
    public Set<LiveObject> getAnimalsAt(Vector2D pos) {
        return animals.get(pos);
    }

    @Override
    public MapObject getGrassAt(Vector2D pos) {
        return grasses.get(pos);
    }

    //isGrassAt
    @Override
    public boolean isGrassAt(Vector2D pos) {
        return grasses.containsKey(pos);
    }

    @Override
    public boolean checkRun(){
        return run;
    }

    @Override
    public Boundary getSize(){
        return size;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
