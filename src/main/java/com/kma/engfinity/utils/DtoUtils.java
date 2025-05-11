package com.kma.engfinity.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import com.kma.engfinity.constants.Constant.*;

@Component
public class DtoUtils {
    public static class PageableBuilder {
        public static Pageable build (Integer page, Integer size, String orderBy, String order) {
            Sort sort = OrderBy.ASC.equalsIgnoreCase(order)
                    ? Sort.by(orderBy).ascending()
                    : Sort.by(orderBy).descending();
            return PageRequest.of(page, size, sort);
        }

    }
}
