package dev.redy1908.greenway.util.services;

import org.springframework.data.domain.Page;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;

import java.util.List;
import java.util.function.Supplier;

public abstract class PagingService<T, D> {

    protected abstract D mapToDto(T entity);

    protected PageResponseDTO<D> createPageResponse(Supplier<Page<T>> pageSupplier) {
        Page<T> elements = pageSupplier.get();
        List<T> listElements = elements.getContent();
        List<D> content = listElements.stream().map(this::mapToDto).toList();

        PageResponseDTO<D> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setContent(content);
        pageResponseDTO.setPageNo(elements.getNumber());
        pageResponseDTO.setPageSize(elements.getSize());
        pageResponseDTO.setTotalElements(elements.getTotalElements());
        pageResponseDTO.setTotalPages(elements.getTotalPages());
        pageResponseDTO.setLast(elements.isLast());

        return pageResponseDTO;
    }

}
