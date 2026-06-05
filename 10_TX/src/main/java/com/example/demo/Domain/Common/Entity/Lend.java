package com.example.demo.Domain.Common.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //누가?
    @ManyToOne(fetch = FetchType.EAGER)
//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="username",
            foreignKey = @ForeignKey(
                    name="FK_LEND_USER",
                    foreignKeyDefinition = "FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private User user;

    //어떤책?
    @ManyToOne(fetch = FetchType.EAGER)
//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="bookCode",
            foreignKey = @ForeignKey(
                    name="FK_LEND_BOOK",
                    foreignKeyDefinition = "FOREIGN KEY (book_code) REFERENCES book(book_code) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private Book book;
}
