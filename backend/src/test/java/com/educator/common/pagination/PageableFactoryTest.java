package com.educator.common.pagination;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class PageableFactoryTest {

    @Test
    void of_defaultsPageToZeroWhenNull() {
        Pageable pageable = PageableFactory.of(null, 10, null);

        assertThat(pageable.getPageNumber()).isEqualTo(0);
    }

    @Test
    void of_defaultsPageToZeroWhenNegative() {
        Pageable pageable = PageableFactory.of(-5, 10, null);

        assertThat(pageable.getPageNumber()).isEqualTo(0);
    }

    @Test
    void of_keepsProvidedPageWhenValid() {
        Pageable pageable = PageableFactory.of(3, 10, null);

        assertThat(pageable.getPageNumber()).isEqualTo(3);
    }

    @Test
    void of_defaultsSizeToTwentyWhenNull() {
        Pageable pageable = PageableFactory.of(0, null, null);

        assertThat(pageable.getPageSize()).isEqualTo(20);
    }

    @Test
    void of_defaultsSizeToTwentyWhenZero() {
        Pageable pageable = PageableFactory.of(0, 0, null);

        assertThat(pageable.getPageSize()).isEqualTo(20);
    }

    @Test
    void of_defaultsSizeToTwentyWhenNegative() {
        Pageable pageable = PageableFactory.of(0, -1, null);

        assertThat(pageable.getPageSize()).isEqualTo(20);
    }

    @Test
    void of_capsSizeAtHundred() {
        Pageable pageable = PageableFactory.of(0, 1000, null);

        assertThat(pageable.getPageSize()).isEqualTo(100);
    }

    @Test
    void of_keepsSizeWhenWithinLimit() {
        Pageable pageable = PageableFactory.of(0, 100, null);

        assertThat(pageable.getPageSize()).isEqualTo(100);
    }

    @Test
    void of_usesUnsortedWhenSortIsNull() {
        Pageable pageable = PageableFactory.of(0, 10, null);

        assertThat(pageable.getSort().isUnsorted()).isTrue();
    }

    @Test
    void of_appliesProvidedSort() {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageableFactory.of(1, 25, sort);

        assertThat(pageable.getSort().getOrderFor("createdAt")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("createdAt").isDescending()).isTrue();
    }
}

