package com.github.missthee.db.entity.primary.compute;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
@Entity
@Data
@Accessors(chain = true)
public class Compute implements Serializable {
    @Id
    private Integer id;

    private Integer column1;

    private Integer column2;

    private static final long serialVersionUID = 1L;

   public static final String ID = "id";

   public static final String DB_ID = "id";

   public static final String COLUMN1 = "column1";

   public static final String DB_COLUMN1 = "column1";

   public static final String COLUMN2 = "column2";

   public static final String DB_COLUMN2 = "column2";
}