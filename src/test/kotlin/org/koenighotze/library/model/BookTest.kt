package org.koenighotze.library.model

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("A Book")
class BookTest {
    @Nested
    @DisplayName("when constructing")
    inner class WhenConstructing {
        @Test
        fun `should allow the id to be optional`() {
            val book = Book(id = null, title = "foo", author = "bar", isbn = "qux")

            assertNull(book.id)
        }
    }
}