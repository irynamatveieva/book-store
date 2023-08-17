package com.example.bookstore.repository.book;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationBuilder;
import com.example.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.getTitles() != null && searchParameters.getTitles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.getTitles()));
        }
        if (searchParameters.getAuthors() != null && searchParameters.getAuthors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.getAuthors()));
        }
        if (searchParameters.getFromPrice() != null) {
            String[] arrOfParameter = {String.valueOf(searchParameters.getFromPrice())};
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("priceGreaterThan")
                    .getSpecification(arrOfParameter));
        }
        if (searchParameters.getToPrice() != null) {
            String[] arrOfParameter = {String.valueOf(searchParameters.getToPrice())};
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("priceLessThan")
                    .getSpecification(arrOfParameter));
        }
        if (searchParameters.getDescriptions() != null
                && searchParameters.getDescriptions().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("description")
                    .getSpecification(searchParameters.getDescriptions()));
        }
        if (searchParameters.getCoverImages() != null
                && searchParameters.getCoverImages().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("cover_image")
                    .getSpecification(searchParameters.getCoverImages()));
        }
        return spec;
    }
}
