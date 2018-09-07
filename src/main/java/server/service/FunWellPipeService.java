package server.service;

import server.db.primary.model.sysoption.WellPipeInfo_Group;

import java.util.List;

public interface FunWellPipeService {
    List<WellPipeInfo_Group> selectPipeGroup();
}
