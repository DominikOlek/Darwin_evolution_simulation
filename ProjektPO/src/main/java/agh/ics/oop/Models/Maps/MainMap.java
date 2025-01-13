package agh.ics.oop.Models.Maps;

import agh.ics.oop.Models.Enums.MoveDirection;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Sprite.Grass;
import agh.ics.oop.Models.Utils.*;

import java.util.*;

import static java.lang.Math.sqrt;

public class MainMap extends DayStatistics {
    //mapa pozwoli jednostkowo dostać się do elementów na polu, hashset do konkretnego elementu w nim i można też po nim iterować
    private final Map<Vector2D, Set<Animal>> animals = new HashMap<Vector2D, Set<Animal>>();
    private final Map<Vector2D, Grass> grasses = new HashMap<Vector2D, Grass>();
    private int currentDay =0;

    private final int startGrassNumber;
    private final int numberOfGrowing;
    private final int lenGen;
    private final int energyFromEat;
    private final int growForDay;
    private final int energyToAdult;
    private final int energyForMulti;
    private final int startEnergy;
    private final int howMany;

    private final int ID;

    private Boundary size;
    private int equator;
    private int equatorHeight;
    private boolean run = true;

    //MapSettings - wszystkie opcje jakie będą ustawiane
    public MainMap(Boundary size, MapSettings settings,int howManyAnimals) {
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
        this.startEnergy = settings.startEnergy();
        this.howMany = howManyAnimals;
        generateStartAnimal();
        setSurface(size);
        setStartEnergy(howManyAnimals,startEnergy);

        ID = GenerateID.GetID();
    }
    //wykonywane przez simulation co dnia, aktualnie cykl jedzenie i rozmnażania połączony
    public void doDay(){

        killsAnimals();
        if(animals.isEmpty())
            run = false;

        moveTo(copyCollection());
        allMove();


        cycleEatMulti();

        System.out.println("Mapa: "+ID);
        for (Map.Entry<Vector2D, Set<Animal>> entry : animals.entrySet()) {
            Vector2D position = entry.getKey();
            Set<Animal> animalSet = entry.getValue();
            System.out.println("Pozycja: " + position);
            System.out.println("Zwierzęta: ");
            for (Animal animal : animalSet) {
                System.out.println("  " + animal);
            }
        }
    }

    private void generateStartAnimal(){
        RandomPositionGenerator rand = new RandomPositionGenerator(size.upperRight().getX(),size.upperRight().getY(),howMany);
        for (Vector2D position : rand) {
            addAnimal(position, new Animal(position, new Dna(lenGen), startEnergy));
        }
    }

    public Set<Animal> copyCollection(){
        Set<Animal> AllObj = new HashSet<>();
        for(Set<Animal> set:animals.values()) AllObj.addAll(set);
        return AllObj;
    }


    //runDay
        //Kill
        //Move (na tej podstawie stworzyć tabele z pozycjami w danym kroku)
        //Eat (wykonywany nie na liscie zwierząt tylko pozycji z move)
        //Reproduce (też na liscie z move)
        //Grow (tak by nie rosło na tych na których jest)

    //hashmap jest aktualizowana tak żeby iterować tylko po pozycjach gdzie coś jeszcze jest, hasset przechowuje jakie zwierzęta są na danym polu
    //czy TreeSet się opłaca ? Jedzenie Reprodukcja a Ruchy usuwanie dodawanie

    //sprawdza czy mieści się w mapie
    public boolean canMoveTo(Vector2D pos) {
        return (size.lowerLeft().precedes(pos) && size.upperRight().follows(pos));
    }

    //porusza zwierzakiem na podstawie jego obrotu
    public void moveTo(Set<Animal> AllAnimals) {
        try {
            for(Animal animal:AllAnimals){
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
            }
        }catch (Exception e){
            System.out.println();
        }
    }

    public boolean addAnimal(Vector2D pos, Animal animal) {
        if (animal.getEnergy()<0 || animal.getDna().getSize() != lenGen)
            return false;
        // computeIfAbsent tworzy nowy HashSet, jeśli jeszcze nie istnieje
        if(!animals.containsKey(pos)){
            changeNumberOfUsePlaces(1);
        }
        animals.computeIfAbsent(pos, k -> new HashSet<>()).add(animal);
        return true;
    }

    //usuwanie zwierzaka i hashseta gdy trzeba
    public boolean removeAnimal(Vector2D pos, Animal animal) {
        if (animals.containsKey(pos)) {
            boolean status = false;
            Set<Animal> set = animals.get(pos);
            status = set.remove(animal);
            if (set.isEmpty()) {
                animals.remove(pos);
                changeNumberOfUsePlaces(-1);
            }
            return status;
        }else
            return false;
    }

    //iteruje i usuwa  zwierzaki z energia == 0
    private void killsAnimals(){
        Iterator<Map.Entry<Vector2D, Set<Animal>>> iteratMap = animals.entrySet().iterator();

        while (iteratMap.hasNext()) {
            Map.Entry<Vector2D, Set<Animal>> set = iteratMap.next();
            Set<Animal> values = set.getValue();
            Iterator<Animal> iteratSet = values.iterator();

            while (iteratSet.hasNext()) {
                Animal anim = iteratSet.next();
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

    //wykonanie cyklu dla każdego pola sprawdzenie czy można zjeść i się rozmnożyć
    private void cycleEatMulti(){
        for(Map.Entry<Vector2D, Set<Animal>> animal : animals.entrySet()) {
            if(animal.getValue().size() == 1 && !isGrassAt(animal.getKey())) {
                continue;
            }
            List<Animal> strongest = getTwoStrongest(animal.getKey());
            if(isGrassAt(animal.getKey())){
                strongest.getFirst().eat(energyFromEat);
                grasses.remove(animal.getKey());
                oneEat(energyFromEat);
            }

            if(strongest.getLast() != null && strongest.getLast().getEnergy() >= energyToAdult){
                multipl(strongest);
            }
        }
    }

    //pobranie 2 najsliniejszych z pola lub jednego i null na index 2
    public List<Animal> getTwoStrongest(Vector2D pos){
        List<Animal> list = new ArrayList<>(2);
        Set<Animal> values = animals.get(pos);
        TreeSet<Animal> treeSet = new TreeSet<Animal>(values);
        list.add(treeSet.removeFirst());
        if(!treeSet.isEmpty()){
            list.add(treeSet.removeFirst());
        }
        return list;
    }

    //stworzenie nowego potomka
    private void multipl(List<Animal> animals){
        List<Integer> dnaList = new ArrayList<>(lenGen);
        try {
            dnaList = animals.getFirst().multiplicationWith(animals.getLast());
            Dna dna = new Dna(dnaList, lenGen);
            animals.getFirst().multiplicationLostEnergy(energyForMulti);
            animals.getLast().multiplicationLostEnergy(energyForMulti);
            Animal child = new Animal(animals.getFirst().getPosition(), dna, 2 * energyForMulti);
            addAnimal(child.getPosition(), child);
            child.addParent(animals.getFirst(),animals.getLast());
            changeNumberOfAnimals(true,child);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
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

    public boolean checkRun(){
        return run;
    }

    public void growGrass(int howMany){
        int toGood = howMany/5;
        //generowanie (pas 0-(sr-szer/2) i (sr+szer/2) - 20% a równik (sr+-szer) - 80%)
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
