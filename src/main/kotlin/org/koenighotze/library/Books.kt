package org.koenighotze.library

import org.koenighotze.library.model.Book
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class Books {
    val books = mutableListOf(
        Book(UUID.randomUUID().toString(), "The Lord of The Rings", "J.R.R. Tolkien", "Todo")
    )

    @GetMapping
    fun allBooks() = books

    // TODO return 404 on Nil
    @GetMapping("/{id}")
    fun book(@PathVariable id: String) = books.find { it.id == id }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = books.removeIf { it.id == id }

    @PostMapping("/{id}")
    fun add(@PathVariable id: String, @RequestBody book: Book) = books.add(book.copy(id = id))
}