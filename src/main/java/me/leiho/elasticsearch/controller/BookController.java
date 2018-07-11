package me.leiho.elasticsearch.controller;

import me.leiho.elasticsearch.dao.BookDao;
import me.leiho.elasticsearch.model.Book;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "ElasticSearch Demo接口")
@RestController
@RequestMapping("/books")
public class BookController {

    private BookDao bookDao;

    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @ApiOperation("添加书籍")
    @PostMapping
    public Book insertBook(@RequestBody Book book){
        return bookDao.insertBook(book);
    }

    @ApiOperation("通过编号查询书籍")
    @GetMapping("/{id}")
    public Map<String, Object> getBookById(@PathVariable String id){
        return bookDao.getBookById(id);
    }

    @ApiOperation("通过编号更新书籍")
    @PutMapping("/{id}")
    public Map<String, Object> updateBookById(@RequestBody Book book, @PathVariable String id){
        return bookDao.updateBookById(id, book);
    }

    @ApiOperation("通过编号删除书籍")
    @DeleteMapping("/{id}")
    public Boolean deleteBookById(@PathVariable String id){
        return bookDao.deleteBookById(id);
    }
}
