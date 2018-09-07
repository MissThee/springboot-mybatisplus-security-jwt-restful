package server.service;

import server.db.primary.model.sysoption.CLoginIplimit;

import java.util.List;

public interface FunCLoginIplimitService {
    List<CLoginIplimit> selectIplimitByUserId(Long userId);

    boolean createCLoginIplimit(CLoginIplimit cLoginIplimit);

    boolean deleteCLoginIplimitByIdList(List<Long> idList);
}
