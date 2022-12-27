package com.fastshop.service.mapper;

import com.fastshop.domain.Color;
import com.fastshop.service.dto.ColorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Color} and its DTO {@link ColorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColorMapper extends EntityMapper<ColorDTO, Color> {}
