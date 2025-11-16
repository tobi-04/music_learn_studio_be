package com.tobi.MusicLearn_Studio_Backend.common.utils;

import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {

    /**
     * Convert Spring Data Page to custom PageResponse
     */
    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> converter) {
        List<R> content = page.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());

        return PageResponse.<R>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .nextPage(page.hasNext() ? page.getNumber() + 1 : null)
                .previousPage(page.hasPrevious() ? page.getNumber() - 1 : null)
                .build();
    }
}
