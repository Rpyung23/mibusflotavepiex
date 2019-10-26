package com.vigitrackecuador.mibusflotaunion.POO;

public class cDespacho
{
    private int contadorIndice;
    private String control;
    private String progamada;
    private String marcpro;
    private int califi;
    private int totalSubPasa;
    private int totalBajaPasa;

    public int getTotalSubPasa() {
        return totalSubPasa;
    }

    public void setTotalSubPasa(int totalSubPasa) {
        this.totalSubPasa = totalSubPasa;
    }

    public int getTotalBajaPasa() {
        return totalBajaPasa;
    }

    public void setTotalBajaPasa(int totalBajaPasa) {
        this.totalBajaPasa = totalBajaPasa;
    }

    public cDespacho() { }

    public int getContadorIndice() {
        return contadorIndice;
    }

    public void setContadorIndice(int contadorIndice) {
        this.contadorIndice = contadorIndice;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getProgamada() {
        return progamada;
    }

    public void setProgamada(String progamada) {
        this.progamada = progamada;
    }

    public String getMarcpro() {
        return marcpro;
    }

    public void setMarcpro(String marcpro) {
        this.marcpro = marcpro;
    }

    public int getCalifi() {
        return califi;
    }

    public void setCalifi(int califi) {
        this.califi = califi;
    }

}
