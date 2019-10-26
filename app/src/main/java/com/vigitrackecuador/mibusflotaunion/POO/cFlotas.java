package com.vigitrackecuador.mibusflotaunion.POO;

public class cFlotas
{
    private String id_bus;
    private int id_ruta;
    private String letra_ruta;
    private String date_salida;
    private String date_llegada;
    private int num_vuelta;
    private int atraso;
    private int adelanto;
    public cFlotas() {

    }

    public String getId_bus() {
        return id_bus;
    }

    public void setId_bus(String id_bus) {
        this.id_bus = id_bus;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getLetra_ruta() {
        return letra_ruta;
    }

    public void setLetra_ruta(String letra_ruta) {
        this.letra_ruta = letra_ruta;
    }

    public String getDate_salida() {
        return date_salida;
    }

    public void setDate_salida(String date_salida) {
        this.date_salida = date_salida;
    }

    public String getDate_llegada() {
        return date_llegada;
    }

    public void setDate_llegada(String date_llegada) {
        this.date_llegada = date_llegada;
    }

    public int getNum_vuelta() {
        return num_vuelta;
    }

    public void setNum_vuelta(int num_vuelta) {
        this.num_vuelta = num_vuelta;
    }

    public int getAtraso() {
        return atraso;
    }

    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    public int getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(int adelanto) {
        this.adelanto = adelanto;
    }
}

