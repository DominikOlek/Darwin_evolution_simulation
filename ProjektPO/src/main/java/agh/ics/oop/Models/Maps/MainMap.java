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
    private final Set<Vector2D> equatorFreeFields = new HashSet<>();
    private final Set<Vector2D> outsideFreeFields = new HashSet<>();
    private final Set<Vector2D> giantAreaFreeFields = new HashSet<>();
    private int minEquatorY;
    private int maxEquatorY;
    private boolean isBreak = false;
    private final static int increaseEnergy = 2;

    //MapSettings - wszystkie opcje jakie będą ustawiane
    public MainMap(Boundary size, MapSettings settings) {
        super();
        this.settings = settings;
        this.size = size;
        this.equator = size.upperRight().getY()/2;
        this.equatorHeight = size.upperRight().getY()/5;

        initFreeFields();
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
                if(!isBreak)
                    Thread.sleep(50);
                else
                    Thread.sleep(5);
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

    private void removeFromAllSets(Vector2D... positions) {
        for (Vector2D p : positions) {
            equatorFreeFields.remove(p);
            outsideFreeFields.remove(p);
            giantAreaFreeFields.remove(p);
        }
    }

    private void addToSets(Vector2D pos) {
        if (isEquatorPos(pos)) equatorFreeFields.add(pos);
        else outsideFreeFields.add(pos);

        if (isInGiantArea(pos)) giantAreaFreeFields.add(pos);
    }


    private void removeGrass(Grass grass, Vector2D pos) {
        grasses.remove(pos);
        changeGrass(-1);

        if (grass.isBig()) {
            // Pętla, żeby usunąć 4 pola
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Vector2D check = new Vector2D(pos.getX()+dx, pos.getY()+dy);
                    if (grasses.get(check) == grass) {
                        grasses.remove(check);
                        addToSets(check);
                    }
                }
            }
        } else {
            addToSets(pos);
        }
    }



    private void cycleEatMulti() {
        // Dla każdego pola
        for(Map.Entry<Vector2D, Set<LiveObject>> entry : animals.entrySet()) {
            Vector2D pos = entry.getKey();
            Set<LiveObject> animalSet = entry.getValue();

            if (animalSet.size() == 1 && !isGrassAt(pos)) {
                // jeśli jest tylko 1 zwierzak i brak rośliny, nie ma co jeść/rozmnażać
                continue;
            }

            // Bierzemy 2 najsilniejsze z getTwoStrongest(...)
            List<LiveObject> strongest = getTwoStrongest(pos);

            if (isGrassAt(pos)) {
                // Pobieramy roślinę
                MapObject roslina = grasses.get(pos);
                if (roslina instanceof Grass grassObj) {
                    strongest.getFirst().eat(roslina.getEnergy());

                    removeGrass(grassObj, pos);

                    oneEat(roslina.getEnergy());

                    // Dodanie pola do wolnych
                    if (isEquatorPos(pos)) equatorFreeFields.add(pos);
                    else outsideFreeFields.add(pos);
                }
            }

            // Rozmnażanie
            if (strongest.size() > 1 && strongest.getLast().getEnergy() >= settings.energyToAdult()) {
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

    private void initFreeFields() {
        int width = size.upperRight().getX() + 1;
        int height = size.upperRight().getY() + 1;

        this.minEquatorY = Math.max(size.lowerLeft().getY(), equator - equatorHeight/2);
        this.maxEquatorY = Math.min(size.upperRight().getY(), equator + equatorHeight/2);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2D pos = new Vector2D(x,y);

                if (y >= minEquatorY && y <= maxEquatorY) equatorFreeFields.add(pos);
                else outsideFreeFields.add(pos);
            }
        }

        int giantWidth = (int) (width * 0.2);
        int giantHeight = (int) (height * 0.2);

        // Losujemy lewy-górny róg kwadratu (żeby dało się zmieścić giantWidth × giantHeight)
        int startX = (int) (Math.random() * (width - giantWidth + 1));
        int startY = (int) (Math.random() * (height - giantHeight + 1));

        for (int gx = startX; gx < startX + giantWidth; gx++) {
            for (int gy = startY; gy < startY + giantHeight; gy++) {
                Vector2D pos = new Vector2D(gx, gy);
                giantAreaFreeFields.add(pos);
            }
        }
    }

    private boolean isEquatorPos(Vector2D pos) {
        return pos.getY() >= minEquatorY && pos.getY() <= maxEquatorY;
    }

    private boolean isInGiantArea(Vector2D pos) {
        return giantAreaFreeFields != null && giantAreaFreeFields.contains(pos);
    }

private boolean placeGrass(boolean isBig, Set<Vector2D> freeSet) {
    if (freeSet.isEmpty()) return false; // brak pól = nie sadzimy

    // Losujemy pole z freeSet
    List<Vector2D> temp = new ArrayList<>(freeSet);
    Vector2D chosen = temp.get((int) (Math.random() * temp.size()));

    if (!isBig) {
        Grass small = new Grass(chosen, settings.energyFromEat(), false);
        grasses.put(chosen, small);
        changeGrass(1);

        removeFromAllSets(chosen);
        return true;

    } else {
        return placeBig(chosen, freeSet);
    }
}

private boolean placeBig(Vector2D chosen, Set<Vector2D> freeSet) {
    int x = chosen.getX();
    int y = chosen.getY();

    Vector2D p1 = new Vector2D(x+1,y);
    Vector2D p2 = new Vector2D(x,y+1);
    Vector2D p3 = new Vector2D(x+1,y+1);

    // sprawdzamy, czy p1, p2, p3 też są w freeSet
    if (!freeSet.contains(p1) || !freeSet.contains(p2) || !freeSet.contains(p3)) {
        // Nie ma miejsca na 2x2
        return false;
    }

    Grass big = new Grass(chosen, settings.energyFromEat() * increaseEnergy, true);

    // 4 klucze w mapie 'grasses' wskazują TEN SAM obiekt
    grasses.put(chosen, big);
    grasses.put(p1, big);
    grasses.put(p2, big);
    grasses.put(p3, big);

    changeGrass(1);

    removeFromAllSets(chosen, p1, p2, p3);
    return true;
}

    private void growGrass(int dailyGrassCount) {
        // rosliny zasadzone danego dnia
        int planted = 0;

        while (planted < dailyGrassCount && (!equatorFreeFields.isEmpty() || !outsideFreeFields.isEmpty())) {

            double rand = Math.random();
            boolean wantEquator = (rand < 0.8);

            Set<Vector2D> chosenSet = (wantEquator) ? equatorFreeFields : outsideFreeFields;

            if (chosenSet.isEmpty()) {
                if (wantEquator && !outsideFreeFields.isEmpty()) chosenSet = outsideFreeFields;
                else if (!wantEquator && !equatorFreeFields.isEmpty()) chosenSet = equatorFreeFields;
                else break; // brak wolnych pól
            }

            boolean isBigPlant = false;
            Set<Vector2D> bigSet = giantAreaFreeFields;
            if (settings.whichGrass()) {
                if ((planted+1) % 5 == 0 && !bigSet.isEmpty()) {
                    isBigPlant = true;
                }
            }

            // Sadzimy roślinę (dużą albo małą)
            if (!placeGrass(isBigPlant, isBigPlant ? bigSet : chosenSet)) {
                // nie posadziliśmy rośliny
                if (isBigPlant) {
                    placeGrass(false, chosenSet);
                }
            }
            planted++;
        }
    }

    @Override
    public Set<LiveObject> copyAnimalCollection(){
        Set<LiveObject> AllObj = new HashSet<>();
        for(Set<LiveObject> set:animals.values()) AllObj.addAll(set);
        return AllObj;
    }

    public Vector2D getEquator(){
        return new Vector2D(minEquatorY,maxEquatorY);
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

    @Override
    public void setBreak(boolean isBreak) {
        this.isBreak = isBreak;
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
