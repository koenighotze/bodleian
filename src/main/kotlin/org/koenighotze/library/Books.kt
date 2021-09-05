package org.koenighotze.library

import org.koenighotze.library.model.Book
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class Books(val repo: BooksRepository) {
    @GetMapping
    fun allBooks(): List<Book> = repo.findAll()

    // TODO return 404 on Nil
    @GetMapping("/{id}")
    fun book(@PathVariable id: String) = repo.findById(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = repo.deleteById(id)

    @PostMapping("/{id}")
    fun add(@PathVariable id: String, @RequestBody book: Book) = repo.save(book.copy(id = id))
}