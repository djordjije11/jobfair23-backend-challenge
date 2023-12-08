package com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TokensConverter implements AttributeConverter<Tokens, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Tokens attribute) {
        return attribute == null ? null : attribute.amount();
    }
    @Override
    public Tokens convertToEntityAttribute(Integer dbData) {
        return new Tokens(dbData);
    }
}
