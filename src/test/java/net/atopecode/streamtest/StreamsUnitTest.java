package net.atopecode.streamtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import net.atopecode.streamtest.data.Book;
import net.atopecode.streamtest.data.BookRepository;

public class StreamsUnitTest {

	
	private BookRepository bookRepository;
	
	public StreamsUnitTest() {
		this.bookRepository  = new BookRepository();
	}

	@Test
	public void peek() {
		//A diferencia de 'map()' hace algo con el objeto pero devuelve el mismo objeto, no otro tipo de dato.
		//Con 'peek()' se realizan operaciones sobre el objeto y se devuelve. Con 'map()' se recibe un objeto y se devuelve uno de otro tipo.
		List<Book> books = bookRepository.getBooks().stream()
			.peek(book -> book.setAuthor("ATopeCode"))
			.peek(book -> System.out.println(book))
			.collect(Collectors.toList());
	}
	
	@Test
	public void map() {
		List<String> titles = bookRepository.getBooks().stream()
				.map(book -> book.getTitle())
				.peek(title -> System.out.println(title))
				.collect(Collectors.toList());
		
		assertEquals(titles.size(), bookRepository.getBooks().size());
	}
	
	@Test
	public void forEach() {
		bookRepository.getBooks().stream()
			.forEach(book -> {
				double newPrice = book.getPrice();
				newPrice *= 2;
				book.setPrice(newPrice);
				System.out.println(book);
			});
	}
	
	@Test
	public void mapAndForEach() {
		List<String> titles = bookRepository.getBooks().stream()
				.map(book -> book.getTitle())
				.peek(title -> System.out.println(title))
				.collect(Collectors.toList());
		
		assertEquals(titles.size(), bookRepository.getBooks().size());
		bookRepository.getBooks().forEach(
				(book) -> {
					assertTrue(titles.contains(book.getTitle()));
				});
	}
	
	@Test
	public void filter() {
		List<Book> books = bookRepository.getBooks().stream()
				.filter(book -> book.getPrice() >= 15)
				.peek(book -> System.out.println(book))
				.collect(Collectors.toList());
		
		assertTrue(books.size() == 2);
	}
	
	@Test
	public void findFirst() {
		Book bookFirst = bookRepository.getBooks().stream()
					.filter(book -> book.getPrice() >= 15)
					.peek(System.out::println)
					.findFirst()
					.orElse(null);
		
		assertTrue(bookFirst.getPrice() >= 15);
		
		//Nota.- Hay 2 libros que cumplen la condición del 'filter()' pero como se utiliza evaluación 'lazyEvaluation' al existir el 'findFirst()'
		//ya no se realiza la siguiente iteración sobre el stream. Se observa que pasa esto porque en el 'peek()' solo se visualiza el primer
		//elemento que cumple la condición y que justamente es el resultado de la consulta.
	}
	
	@Test
	public void toArray() {
		//Como se hacía antes de los Streams (2 formas distintas):
		Book[] booksArray = bookRepository.getBooks().toArray(Book[]::new);
		booksArray = bookRepository.getBooks().toArray(new Book[bookRepository.getBooks().size()]);
				
		//Como se hace con Streams (es lo mismo pero son Streams):
		booksArray = bookRepository.getBooks().stream().toArray(Book[]::new);
		
		//Convertimos el array en un stream para visualizar su contenido más fácilmente sin tener que utilizar el 'for(Book book: booksArray)' de toda la vida:
		Stream.of(booksArray)
			.peek(System.out::println)
			.collect(Collectors.toList());
	}
	
	@Test
	public void sorted() {
		List<Book> books = bookRepository.getBooks().stream()
				.sorted((book1, book2) -> book1.getAuthor().compareToIgnoreCase(book2.getAuthor()))
				.peek(System.out::println)
				.collect(Collectors.toList());
		
		assertTrue(books.get(0).getAuthor().contains("Bartolomé"));
		assertTrue(books.get(1).getAuthor().contains("José"));
		assertTrue(books.get(2).getAuthor().contains("Luís"));
	}
}
