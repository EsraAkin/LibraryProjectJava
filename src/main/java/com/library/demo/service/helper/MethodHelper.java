package com.library.demo.service.helper;

import com.library.demo.repository.businnes.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {
    private final BookRepository bookRepository;


}
