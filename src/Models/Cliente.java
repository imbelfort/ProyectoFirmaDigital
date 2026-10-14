package Models;

import java.math.BigDecimal;

public class Cliente {
    private int idCliente;
    private int idUsuario;
    private String tipoCliente;
    private BigDecimal limiteCredito;
    private BigDecimal creditoDisponible;
    private BigDecimal creditoUtilizado;

    public Cliente() {
    }

    public Cliente(int idCliente, int idUsuario, String tipoCliente, BigDecimal limiteCredito, BigDecimal creditoDisponible, BigDecimal creditoUtilizado) {
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.tipoCliente = tipoCliente;
        this.limiteCredito = limiteCredito;
        this.creditoDisponible = creditoDisponible;
        this.creditoUtilizado = creditoUtilizado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public BigDecimal getCreditoDisponible() {
        return creditoDisponible;
    }

    public void setCreditoDisponible(BigDecimal creditoDisponible) {
        this.creditoDisponible = creditoDisponible;
    }

    public BigDecimal getCreditoUtilizado() {
        return creditoUtilizado;
    }

    public void setCreditoUtilizado(BigDecimal creditoUtilizado) {
        this.creditoUtilizado = creditoUtilizado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", idUsuario=" + idUsuario +
                ", tipoCliente='" + tipoCliente + '\'' +
                ", limiteCredito=" + limiteCredito +
                ", creditoDisponible=" + creditoDisponible +
                ", creditoUtilizado=" + creditoUtilizado +
                '}';
    }
}