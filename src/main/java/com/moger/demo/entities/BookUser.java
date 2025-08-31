package com.moger.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class BookUser { 
	
    @EmbeddedId
    private BookUserId id;

    public BookUser(Book book, User user){
        id.setBookId(book.getId());
        id.setUserId(user.getId());
    }

	@ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable =false)
    private Book book;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

}
