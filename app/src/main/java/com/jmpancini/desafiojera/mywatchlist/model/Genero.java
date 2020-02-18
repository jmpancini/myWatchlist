package com.jmpancini.desafiojera.mywatchlist.model;

import java.util.HashMap;

public class Genero {

    public static HashMap<String, String> genereMap = new HashMap<String, String>();
    public static HashMap<String, Integer> contadorGenero = new HashMap<String, Integer>();
    private static String idMaior = "";
    private static int maior = 0;


    private String id;
    private String name;

    public Genero() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if((this.id==null || this.id.isEmpty()) && id!=null && !id.isEmpty()){

            if(contadorGenero.get(id)==null) {
                contadorGenero.put(id, 1);
            }else{
                contadorGenero.put(id,  contadorGenero.get(id) + 1);
            }

           if(contadorGenero.get(id) > maior){
               idMaior = id;
               maior = contadorGenero.get(id);
           }
        }
        this.id = id;
    }

    public static String getIdMaior(){
        return idMaior;
    }

    public String getName() {
        if((name==null || name.isEmpty()) && id!=null && !id.isEmpty()){
            name = genereMap.get(id);
        }
        return name;
    }

    public void setName(String name) {
        if(name!=null && !name.isEmpty() && id!=null && !id.isEmpty()){
            genereMap.put(id,name);
        }
        this.name = name;
    }

    public static void resetContador(){
        idMaior = "";
        maior = 0;
        contadorGenero.clear();
    }

}
