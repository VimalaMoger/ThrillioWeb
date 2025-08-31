package com.moger.demo.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class BookUserId implements Serializable {  
   
    private long bookId;
    private long userId;

}
