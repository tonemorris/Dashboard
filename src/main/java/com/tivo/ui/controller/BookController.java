/**
 * 
 */
package com.tivo.ui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tivo.ui.domain.Book;
import com.tivo.ui.service.BookService;

/**
 * @author anthonymorris
 *
 */
@RestController
public class BookController {

	private BookService bookService;

	/**
	 * @param bookService
	 */
	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * @param string
	 * @return
	 */
	@RequestMapping("/book/{name}")
	public List<Book> findByName(@PathVariable String name) {
		return bookService.findByName(name);
	}

}
