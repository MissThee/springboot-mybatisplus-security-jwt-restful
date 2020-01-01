package com.github.common.config.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.DefaultConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
//对象拷贝工具配置
@Configuration
public class OrikaConfig {
    private DefaultMapperFactory.Builder builder=new DefaultMapperFactory.Builder();
    @Bean
//    @Scope("prototype")
    public MapperFacade mapperFacade() {
        ConverterFactory converterFactory = new DefaultConverterFactory() ;
        converterFactory.registerConverter(new LocalDateTimeDateConverter());//添加LocalDateTime与Date转换器
        builder.converterFactory(converterFactory);
        return builder.build().getMapperFacade();
    }

    private static class LocalDateTimeDateConverter extends BidirectionalConverter<LocalDateTime, Date> {
        @Override
        public Date convertTo(LocalDateTime source, Type<Date> destinationType, MappingContext mappingContext) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = source.atZone(zoneId);
            return Date.from(zdt.toInstant());
        }

        @Override
        public LocalDateTime convertFrom(Date source, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
            Instant instant = source.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }
    }
}