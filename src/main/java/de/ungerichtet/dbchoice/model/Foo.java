package de.ungerichtet.dbchoice.model;

import lombok.Data;

import java.time.Instant;

/**
 * The model class used inside this application might look like
 * an easy one, but the {@link #created} property can cause problems.
 */
public @Data
class Foo {

    private long id;

    /**
     * Not every database has data types to store a {@link Instant}
     * with all of its values. Mysql 5.1 has a TIMESTAMP data type
     * but milliseconds for example are not stored. A bigint can be
     * used for this purpose, but must be handled in correct manner
     * (time zones etc.).
     */
    private Instant created;

    private String value;

    public Foo() {
        created = Instant.now();
    }

    public Foo(String value) {
        this();
        this.value = value;
    }
}

