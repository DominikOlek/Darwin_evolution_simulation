package agh.ics.oop.Models.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.sqrt;

public class Dna {
    private List<Integer> value = new ArrayList<Integer>();
    private int actual = 0;
    private int size ;

    public Dna(int size) {
        this.size = size;
        generateDna();
    }

    public Dna(List<Integer> value,int size) {
        this.size = size;
        this.value = value;
    }

    //generowanie dna na początku
    private void generateDna(){
        for(int i =0;i<size;i++){
            value.add(getRandom(0,8));
        }
    }

    public void nextDay(){
        actual = (actual+1)%value.size();
    }
    public int getActual() {
        return value.get(actual);
    }

    //stworzenie Dna dziecka
    public List<Integer> multiplicationWith(Dna other,float percentageFromMe){
        List<Integer> list = new ArrayList<>();
        //podział lewy czy prawy
        if (Math.random() < 0.5f ){
            list = getPartToMultiplication(percentageFromMe,true);
            list.addAll(other.getPartToMultiplication(1-percentageFromMe,false));
        }else{
            list = other.getPartToMultiplication(1-percentageFromMe,true);
            list.addAll(getPartToMultiplication(percentageFromMe,false));
        }

        //MUTACJE
        int many = getRandom(0,list.size());  //TUTAJ TEŻ LEPSZE LOSOWANIE BEZ POWTÓRZEŃ ?
        for(int i=0;i<many;i++){
            int j = getRandom(0,list.size());
            int k = getRandom(0,8);
            list.set(j, k);
        }

        return list;
    }

    //pobranie odpowiedniej części dna
    public List<Integer> getPartToMultiplication(float percentageFromMe,boolean isLeft){
        if(isLeft){
            return new ArrayList<>(value.subList(0, (int) Math.ceil(value.size()*percentageFromMe)));
        }else{
            return new ArrayList<>(value.subList((int) (Math.ceil(value.size()*(1-percentageFromMe))),  value.size()));
        }
    }

    //from - od jakiej, to-ile (czyli realnie to-1)
    private int getRandom(int from,int to){
        return from + (int) Math.floor(Math.random()* to);
    }

    @Override
    public String toString() {
        return "Dna{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dna dna = (Dna) o;
        return Objects.equals(value, dna.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
