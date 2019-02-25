package server.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

//树形数据构建工具
public class TreeData {
    /**
     * 遍历id含parentId树形数据结构的数据
     *
     * @param li             包含id和parentId的数据集合的迭代器
     * @param parentId       固定为null
     * @param rootId         根节点的id
     * @param rootIdIsNodeId 使用节点id或parentId与rootId比较。true：节点id==rootId为根节点；false：节点parentId==rootId为根节点
     * @param idColumn       自定义实体类id列名
     * @param parentIdColumn 自定义实体类parentId列名
     * @param attrMap        每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public static <T> JSONArray tree(List<T> li, Integer parentId, Integer rootId, Boolean rootIdIsNodeId, String idColumn, String parentIdColumn, Map<String, String> attrMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONArray nodeJA = new JSONArray();
        for (T currentItem : li) {
            Integer nodeId = (Integer) getPropertyValue(currentItem, idColumn);
            Integer nodePId = (Integer) getPropertyValue(currentItem, parentIdColumn);
            if (parentId == null ? (Objects.equals(rootId, rootIdIsNodeId ? nodeId : nodePId)) : (Objects.equals(nodePId, parentId))) {
                JSONObject childrenJO = new JSONObject() {{
                    put("id", nodeId);
                    //填充自定义属性
                    for (Entry<String, String> entry : attrMap.entrySet()) {
                        if (!StringUtils.isEmpty(entry.getValue())) {
                            put(entry.getKey(), getPropertyValue(currentItem, entry.getValue()));
                        }
                    }
                    put("children", tree(li, nodeId, rootId, rootIdIsNodeId, idColumn, parentIdColumn, attrMap));
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
     * @param li             包含id和parentId的数据
     * @param rootId         根节点的id
     * @param rootIdIsNodeId 使用节点id或parentId与rootId比较。true：节点id==rootId为根节点；false：节点parentId==rootId为根节点
     * @param attrMap        每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public static <T> JSONArray tree(List<T> li, Integer rootId, Boolean rootIdIsNodeId, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return tree(li, null, rootId, rootIdIsNodeId, "id", "parentId", attrMap);
    }

    /**
     * 遍历id含parentId树形数据结构的数据。包含自身节点，自定义根节点。
     *
     * @param li      包含id和parentId的数据
     * @param rootId  根节点的id
     * @param attrMap 每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public static <T> JSONArray tree(List<T> li, Integer rootId, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return tree(li, null, rootId, true, "id", "parentId", attrMap);
    }


    /**
     * 遍历id含parentId树形数据结构的数据。已parentId==null值为根节点。
     *
     * @param li      包含id和parentId的数据
     * @param attrMap 每个节点额外的属性Map(标签名, 实体类属性名>
     */
    public static <T> JSONArray tree(List<T> li, Map<String, String> attrMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return tree(li, null, null, false, "id", "parentId", attrMap);
    }


    /**
     * 遍历id含parentId树形数据结构的数据,返回指定节点的子id集合
     *
     * @param li             包含id和parentId的数据
     * @param parentId       固定为null
     * @param rootId         根节点的id
     * @param rootIdIsNodeId 使用节点id或parentId与rootId比较。true：节点id==rootId为根节点；false：节点parentId==rootId为根节点
     * @param idColumn       自定义实体类id列名
     * @param parentIdColumn 自定义实体类parentId列名
     */
    public static <T> List<Integer> getChildIdList(List<T> li, Integer parentId, Integer rootId, Boolean rootIdIsNodeId, String idColumn, String parentIdColumn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> idList = new ArrayList<>();
        for (T currentItem : li) {
            Integer nodeId = (Integer) currentItem.getClass().getMethod("get" + idColumn.substring(0, 1).toUpperCase() + idColumn.substring(1)).invoke(currentItem);
            Integer nodePId = (Integer) currentItem.getClass().getMethod("get" + parentIdColumn.substring(0, 1).toUpperCase() + parentIdColumn.substring(1)).invoke(currentItem);
            if (parentId == null ? (Objects.equals(rootId, rootIdIsNodeId ? nodeId : nodePId)) : (Objects.equals(nodePId, parentId))) {
                idList.addAll(getChildIdList(li, nodeId, rootId, rootIdIsNodeId, idColumn, parentIdColumn));
                idList.add(nodeId);
            }
        }
        return idList;
    }

    public static <T> List<Integer> getChildIdList(List<T> li, Integer rootId, Boolean rootIdIsNodeId, String idColumn, String parentIdColumn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, rootIdIsNodeId, idColumn, parentIdColumn);
    }

    public static <T> List<Integer> getChildIdList(List<T> li, Integer rootId, Boolean rootIdIsNodeId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, rootIdIsNodeId, "id", "parentId");
    }

    public static <T> List<Integer> getChildIdList(List<T> li, Integer rootId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getChildIdList(li, null, rootId, true, "id", "parentId");
    }

    //    获取相应属性的值
    private static Object getPropertyValue(Object currentItem, String propertyName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return currentItem.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1)).invoke(currentItem);
    }
}
