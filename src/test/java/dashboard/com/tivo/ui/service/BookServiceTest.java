/**
 * 
 */
package dashboard.com.tivo.ui.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.tivo.ui.domain.Book;
import com.tivo.ui.service.BookService;

/**
 * @author anthonymorris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
	
	private BookService bookService;

	@Test
	public void shouldReturnBooksForAuthor() throws Exception {
		List<Book> books = Arrays.asList(new Book("First", "A"));
		
		//given
		
		//when
		
		//then
	}

}
