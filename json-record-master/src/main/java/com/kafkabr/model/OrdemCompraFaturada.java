package com.kafkabr.model;

import java.math.BigDecimal;

/**
 * Evento sobre faturamento das ordens de compra.
 * 
 * @author fabiojose
 */
public class OrdemCompraFaturada {
   
    private String id;
    private String clienteId;
    private BigDecimal valor;

    public String toString() {
        return "id=" + id + ", clienteId=" + clienteId + ", valor=" + valor; 
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setClientId(String clienteId){
        this.clienteId = clienteId;
    }

    public String getClienteId(){
        return clienteId;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor(){
        return valor;
    }
}