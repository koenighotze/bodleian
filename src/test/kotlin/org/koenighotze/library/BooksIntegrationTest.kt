package org.koenighotze.library

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.koenighotze.library.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

@SpringBootTest(
    classes = [ LibraryApplication::class ],
    webEnvironment = RANDOM_PORT
)
@DisplayName("The Books REST endpoint")
@IntegrationTestTag
class BooksIntegrationTest(@Autowired val template: TestRestTemplate, @Autowired val repo: BooksRepository) {
    @BeforeEach
    fun populateDb() {
        repo.saveAll(WellKnownBooks.books)
    }

    @AfterEach
    fun cleanUpDb() {
        repo.deleteAll()
    }

    @Nested
    @DisplayName("when GET-ing a book")
    inner class Get {
        @Nested
        @DisplayName("and the book is found")
        inner class Ok {
            @Test
            fun `it should return the book's data`() {
                val wellKnownId = WellKnownBooks.books[0].id
                val response = template.getForEntity("/books/$wellKnownId", Book::class.java)

                assertAll("the HTTP Response",
                    { assertEquals(OK, response.statusCode) },
                    { assertEquals(WellKnownBooks.books[0], response.body) }
                )
            }
        }

        @Nested
        @DisplayName("and the book is not found")
        inner class Nok {
            @Test
            fun `it should return 404`() {
                val response = template.getForEntity("/books/not_there", Book::class.java)

                assertEquals(NOT_FOUND, response.statusCode)
            }
        }
    }

    @Nested
    @DisplayName("when deleting a book")
    inner class Delete {
        @Nested
        @DisplayName("and the book is found")
        inner class Found {
            @Test
            fun `it should return 200`() {
                val wellKnownId = WellKnownBooks.books[0].id
                val response = template.exchange("/books/$wellKnownId", DELETE, null, ResponseEntity::class.java)

                assertEquals(OK, response.statusCode)
            }
        }

        @Nested
        @DisplayName("and the book is not found")
        inner class NotFound {
            @Test
            fun `it should return 404`() {
                val response = template.exchange("/books/not_there", DELETE, null, ResponseEntity::class.java)

                assertEquals(NOT_FOUND, response.statusCode)
            }
        }
    }
}
