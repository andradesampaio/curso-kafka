package com.kafkabr.model;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

/**
 * @author fabiojose
 */
public class OrdemCompraFaturadaDeserializer 
    extends JsonbDeserializer<OrdemCompraFaturada> {

    public OrdemCompraFaturadaDeserializer() {
        super(OrdemCompraFaturada.class);
    }
}