package com.example.demo.api;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookRestController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/book")
    public Object xemDanhSach(){
        return bookRepository.findAll();
    }
    @GetMapping("/book/{id}")
    public Object xemChiTiet(@PathVariable Long id){
        return bookRepository.findById(id).orElse(null);
    }

    @PostMapping("/book")
    public Object themMoi(@RequestBody Book book){
//        System.out.println(book.toString());
        return bookRepository.save(book);
    }

    @PutMapping("/book/{id}")
    public Object suaTheoId(@PathVariable Long id,@RequestBody Book book){
        book.setId(id);
        return bookRepository.save(book);
    }

    @DeleteMapping("/book/{id}")
    public void xoaTheoId(@PathVariable Long id){
        bookRepository.deleteById(id);
    }
}
