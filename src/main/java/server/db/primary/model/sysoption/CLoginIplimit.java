package server.db.primary.model.sysoption;

import lombok.Data;

@Data
public class CLoginIplimit {
    private Long id;

    private String ipAddr;

    private Long cLoginId;
}