package dev.redy1908.greenway.util.services;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Supplier;

public abstract class PagingService<T> {

    protected PageResponseDTO<T> createPageResponse(Supplier<Page<T>> pageSupplier) {
        Page<T> elements = pageSupplier.get();
        List<T> listElements = elements.getContent();

        PageResponseDTO<T> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setContent(listElements);
        pageResponseDTO.setPageNo(elements.getNumber());
        pageResponseDTO.setPageSize(elements.getSize());
        pageResponseDTO.setTotalElements(elements.getTotalElements());
        pageResponseDTO.setTotalPages(elements.getTotalPages());
        pageResponseDTO.setLast(elements.isLast());

        return pageResponseDTO;
    }

}
