package server.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//树形数据构建工具
@Component
public class BuildTree {
    /**
     * 遍历id含parentId树形数据结构的数据
     *
     * @param li             包含id和parentId的数据
     * @param parentId       固定为null
     * @param rootId         根节点的id
     * @param includeSelf    包含根节点本身true：使用节点id==rootId为根节点；false：使用节点父id==rootId为根节点
     * @param idColumn       自定义实体类id列名
     * @param parentIdColumn 自定义实体类parentId列名
     * @param attrMap        每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public <T> JSONArray buildTree(List<T> li, Integer parentId, Integer rootId, Boolean includeSelf, String idColumn, String parentIdColumn, Map<String, String> attrMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONArray nodeJA = new JSONArray();
        for (T currentItem : li) {
            Integer currentId = (Integer) currentItem.getClass().getMethod("get" + idColumn.substring(0, 1).toUpperCase() + idColumn.substring(1)).invoke(currentItem);
            Integer currentPId = (Integer) currentItem.getClass().getMethod("get" + parentIdColumn.substring(0, 1).toUpperCase() + parentIdColumn.substring(1)).invoke(currentItem);
            if (parentId == null ? (rootId == null ? ((includeSelf ? currentId : currentPId) == null) : (rootId.equals(includeSelf ? currentId : currentPId))) : (currentPId != null && currentPId.equals(parentId))) {
                JSONObject childrenJO = new JSONObject() {{
                    put("id", currentId);
                    for (Entry<String, String> entry : attrMap.entrySet()) {
                        if (!StringUtils.isEmpty(entry.getValue())) {
                            String propertyName = entry.getValue().substring(0, 1).toUpperCase() + entry.getValue().substring(1);
                            put(entry.getKey(), currentItem.getClass().getMethod("get" + propertyName).invoke(currentItem));
                        }
                    }
                    put("children", buildTree(li, currentId, rootId, includeSelf, idColumn, parentIdColumn, attrMap));
                }};

                if (childrenJO.getJSONArray("children") == null || childrenJO.getJSONArray("children").isEmpty()) {
                    childrenJO.remove("children");
                }
                nodeJA.add(childrenJO);
            }
        }
        return nodeJA;
    }

    /**
     * 遍历id含parentId树形数据结构的数据。自定义自是否包含身节点，自定义根节点。
     *
     * @param li          包含id和parentId的数据
     * @param rootId      根节点的id
     * @param includeSelf 包含根节点本身true：使用节点id==rootId为根节点；false：使用节点父id==rootId为根节点
     * @param attrMap     每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public <T> JSONArray buildTree(List<T> li, Integer rootId, Boolean includeSelf, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return buildTree(li, null, rootId, includeSelf, "id", "parentId", attrMap);
    }

    /**
     * 遍历id含parentId树形数据结构的数据。包含自身节点，自定义根节点。
     *
     * @param li      包含id和parentId的数据
     * @param rootId  根节点的id
     * @param attrMap 每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public <T> JSONArray buildTree(List<T> li, Integer rootId, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return buildTree(li, null, rootId, true, "id", "parentId", attrMap);
    }


    /**
     * 遍历id含parentId树形数据结构的数据。已parentId==null值为根节点。
     *
     * @param li      包含id和parentId的数据
     * @param attrMap 每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public <T> JSONArray buildTree(List<T> li, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return buildTree(li, null, null, false, "id", "parentId", attrMap);
    }


    /**
     * 遍历id含parentId树形数据结构的数据,返回指定节点的子id集合
     *
     * @param li             包含id和parentId的数据
     * @param parentId       固定为null
     * @param rootId         根节点的id
     * @param includeSelf    包含根节点本身true：使用节点id==rootId为根节点；false：使用节点父id==rootId为根节点
     * @param idColumn       自定义实体类id列名
     * @param parentIdColumn 自定义实体类parentId列名
     */
    public <T> List<Integer> getChildIdList(List<T> li, Integer parentId, Integer rootId, Boolean includeSelf, String idColumn, String parentIdColumn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> idList = new ArrayList<>();
        for (T currentItem : li) {
            Integer currentId = (Integer) currentItem.getClass().getMethod("get" + idColumn.substring(0, 1).toUpperCase() + idColumn.substring(1)).invoke(currentItem);
            Integer currentPId = (Integer) currentItem.getClass().getMethod("get" + parentIdColumn.substring(0, 1).toUpperCase() + parentIdColumn.substring(1)).invoke(currentItem);
            if (parentId == null ? (rootId == null ? ((includeSelf ? currentId : currentPId) == null) : (rootId.equals(includeSelf ? currentId : currentPId))) : (currentPId != null && currentPId.equals(parentId))) {
                idList.addAll(getChildIdList(li, currentId, rootId, includeSelf, idColumn, parentIdColumn));
                idList.add(currentId);
            }
        }
        return idList;
    }

    public <T> List<Integer> getChildIdList(List<T> li, Integer rootId, Boolean includeSelf, String idColumn, String parentIdColumn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, includeSelf, idColumn, parentIdColumn);
    }

    public <T> List<Integer> getChildIdList(List<T> li, Integer rootId, Boolean includeSelf) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, includeSelf, "id", "parentId");
    }

    public <T> List<Integer> getChildIdList(List<T> li, Integer rootId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, true, "id", "parentId");
    }
}
