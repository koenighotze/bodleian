package org.koenighotze.library

import org.koenighotze.library.model.Book
import java.util.UUID.randomUUID

class WellKnownBooks {
    companion object {
        val books = mutableListOf(
            Book(randomUUID().toString(), "The Lord of The Rings", "J.R.R. Tolkien", "Todo")
        )
    }
}