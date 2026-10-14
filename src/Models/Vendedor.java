package Models;

import java.math.BigDecimal;

public class Vendedor {
    private int idVendedor;
    private int idUsuario;
    private BigDecimal comisionPorcentaje;
    private BigDecimal totalVentas;

    public Vendedor() {
        this.comisionPorcentaje = BigDecimal.ZERO;
        this.totalVentas = BigDecimal.ZERO;
    }

    public Vendedor(int idVendedor, int idUsuario, BigDecimal comisionPorcentaje, 
                    BigDecimal totalVentas, BigDecimal metaMensual) {
        this.idVendedor = idVendedor;
        this.idUsuario = idUsuario;
        this.comisionPorcentaje = comisionPorcentaje;
        this.totalVentas = totalVentas;
        // metaMensual removed to match DDL
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) {
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "idVendedor=" + idVendedor +
                ", idUsuario=" + idUsuario +
                ", comisionPorcentaje=" + comisionPorcentaje +
                ", totalVentas=" + totalVentas +
                '}';
    }
}
