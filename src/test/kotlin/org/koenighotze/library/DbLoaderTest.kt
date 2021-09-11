package org.koenighotze.library

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotNull

@DisplayName("The DbLoader")
@SpringBootTest
@Tag("integration")
internal class DbLoaderTest(@Autowired val repository: BooksRepository, @Autowired val dbLoader: DbLoader) {
    @Test
    fun `should load books into the database`() {
        val books = repository.findAll()

        // Smoke test
        expectThat(books.size).isGreaterThan(2)
        expectThat(books.find { it.title == ("Release IT!") }).isNotNull()
    }
}
