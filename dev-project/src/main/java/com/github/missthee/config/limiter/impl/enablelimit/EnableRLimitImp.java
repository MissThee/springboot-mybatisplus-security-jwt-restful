package com.github.missthee.config.limiter.impl.enablelimit;

import com.github.missthee.config.limiter.impl.limit.LimitPointCut;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class EnableRLimitImp implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{LimitPointCut.class.getName()};
    }
}
