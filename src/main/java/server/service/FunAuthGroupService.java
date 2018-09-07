package server.service;

import server.db.primary.model.sysoption.AuthGroup;

import java.util.List;

public interface FunAuthGroupService {
    List<AuthGroup> selectGroup();

    AuthGroup selectGroupById(Long id);

    List<AuthGroup> selectGroupForList();

    boolean isGroupNameExist(String name);

    boolean createGroup(AuthGroup authGroup);

    boolean isGroupNameExistExceptSelf(Long id, String name);

    boolean updateGroup(AuthGroup authGroup);

    boolean deleteGroupByIdList(List<Long> idList);

    List<AuthGroup> selectGroupTable();

    AuthGroup selectGroupTableOneById(Long id);

    boolean isGroupNoExistExceptSelf(Long id, String groupNo);

    boolean isGroupNoExist(String groupNo);
}
