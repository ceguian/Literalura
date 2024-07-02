package com.aluracursos.Literalura.models;

import java.util.ArrayList;
import java.util.List;

public enum Idioma {
    Ingles("en"),
    Espanol("es"),
    Frances("fr"),
    Portugues("pt"),
    Aleman("de"),
    Arabe("ar"),
    Tagalo("tl"),
    Chino("zh"),
    Ruso("ru"),
    Italiano("it"),
    Hungaro("hu"),
    Finlandes("fi"),
    Desconocido("enm")
    ;

    private String lenguaje;

    Idioma(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public static Idioma fromString(List<String> text){
        var lista = new ArrayList<>(text.stream().toList());
        var lengua = lista.getFirst();

        for(Idioma idioma : Idioma.values()){
            if(idioma.lenguaje.equalsIgnoreCase(lengua)){
                return idioma;
            }else{
                try {
                    idioma = null;
                    return idioma;
                }catch (NullPointerException e){
                    return idioma;
                }
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado");
    }
}
