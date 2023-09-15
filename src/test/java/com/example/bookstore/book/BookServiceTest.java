package com.example.bookstore.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.service.book.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

//    private CreateBookRequestDto requestDto;
//    private CreateBookRequestDto updateRequestDto;
//    private Book book;
//    private BookDto bookDto;

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("title");
        requestDto.setAuthor("author");
        requestDto.setIsbn("0-596-42238-9");
        requestDto.setDescription("description");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setCoverImage("coverimage");
        return requestDto;
    }

    private CreateBookRequestDto getUpdateRequestDto() {
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto();
        updateRequestDto.setTitle("another title");
        updateRequestDto.setAuthor("another author");
        updateRequestDto.setIsbn("0-196-32222-9");
        updateRequestDto.setDescription("another description");
        updateRequestDto.setPrice(BigDecimal.valueOf(45));
        updateRequestDto.setCoverImage("another coverimage");
        return updateRequestDto;
    }

    private Book getBook() {
        Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setIsbn("0-596-42238-9");
        book.setDescription("description");
        book.setPrice(BigDecimal.valueOf(20));
        book.setCoverImage("coverimage");
        return book;
    }

    private BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("title");
        bookDto.setAuthor("author");
        bookDto.setDescription("description");
        bookDto.setPrice(BigDecimal.valueOf(20));
        bookDto.setCoverImage("coverimage");
        return bookDto;
    }

    private BookDto getUpdatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("another title");
        bookDto.setAuthor("another author");
        bookDto.setDescription("another description");
        bookDto.setPrice(BigDecimal.valueOf(45));
        bookDto.setCoverImage("another coverimage");
        return bookDto;
    }


//    @BeforeEach
//    void setUp() {
//        requestDto = new CreateBookRequestDto();
//        requestDto.setTitle("title");
//        requestDto.setAuthor("author");
//        requestDto.setIsbn("0-596-42238-9");
//        requestDto.setDescription("description");
//        requestDto.setPrice(BigDecimal.valueOf(20));
//        requestDto.setCoverImage("coverimage");
//
//        updateRequestDto = new CreateBookRequestDto();
//        updateRequestDto.setTitle("another title");
//        updateRequestDto.setAuthor("another author");
//        updateRequestDto.setIsbn("0-196-32222-9");
//        updateRequestDto.setDescription("another description");
//        updateRequestDto.setPrice(BigDecimal.valueOf(45));
//        updateRequestDto.setCoverImage("another coverimage");
//
//        book = new Book();
//        book.setTitle(requestDto.getTitle());
//        book.setAuthor(requestDto.getAuthor());
//        book.setIsbn(requestDto.getIsbn());
//        book.setDescription(requestDto.getDescription());
//        book.setPrice(requestDto.getPrice());
//        book.setCoverImage(requestDto.getCoverImage());
//
//        bookDto = new BookDto();
//        bookDto.setId(1L);
//        bookDto.setTitle(book.getTitle());
//        bookDto.setAuthor(book.getAuthor());
//        bookDto.setDescription(book.getDescription());
//        bookDto.setPrice(book.getPrice());
//        bookDto.setCoverImage(book.getCoverImage());
//    }

    @Test
    void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        Book book = getBook();
        BookDto bookDto = getBookDto();

        Mockito.when(bookMapper.toEntity(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected  = bookDto;
        BookDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toEntity(requestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void update_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto updateRequestDto = getUpdateRequestDto();
        Book book = getBook();
        BookDto bookDto = getUpdatedBookDto();

        BookDto expected = bookDto;
        expected.setTitle("another title");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        book.setId(1L);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toEntity(updateRequestDto)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.updateById(1L, updateRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).save(book);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toEntity(updateRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void update_NonExistingBookId_ThrowsException() {
        CreateBookRequestDto updateRequestDto = getUpdateRequestDto();

        Long bookId = 100L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(bookId, updateRequestDto));

        String expected = "Can not found Book by id = " + bookId;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_ExistingBookId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = getBook();
        BookDto bookDto = getBookDto();
        book.setId(bookId);

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual =  getBookDto();

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1));
    }
}
