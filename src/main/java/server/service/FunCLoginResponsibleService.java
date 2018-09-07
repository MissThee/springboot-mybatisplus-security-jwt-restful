package server.service;

import server.db.primary.model.sysoption.CLoginResponsible;

import java.util.List;

public interface FunCLoginResponsibleService {
    List<CLoginResponsible> selectResponsibleByUserId(Long userId);

    boolean createCLoginResponsible(CLoginResponsible cLoginResponsible);

    boolean deleteCLoginResponsibleByIdList(List<Long> idList);
}
