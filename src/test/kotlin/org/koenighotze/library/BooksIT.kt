package org.koenighotze.library

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.koenighotze.library.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

@SpringBootTest(
    classes = [ LibraryApplication::class ],
    webEnvironment = RANDOM_PORT
)
@DisplayName("The Books REST endpoint")
class BooksIT(@Autowired val template: TestRestTemplate) {
    @Nested
    @DisplayName("when GET-ing a book")
    inner class Get {
        @Nested
        @DisplayName("and the book is found")
        inner class Ok {
            @Test
            fun `it should return the book's data`() {
                val response = template.getForEntity("/books/123", Book::class.java)

                assertAll("the HTTP Response",
                    { assertEquals(OK, response.statusCode) },
                    { assertEquals(Book("", "", "", ""), response.body) }
                )
            }
        }


        @Test
        fun `it should return the book's data`() {
        }
    }
}