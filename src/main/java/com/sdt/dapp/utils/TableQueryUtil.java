package com.sdt.dapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdt.dapp.properties.QueryConditionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TableQueryUtil {

private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PageRequestUtil pageRequestUtil;

    @Autowired
    ReflectionUtil reflectionUtil;


    Map<String, String> conditionMap;


    public <T, U extends JpaSpecificationExecutor<T> & PagingAndSortingRepository<T, ?>> PagingResult<T> findAll(Map advQuery, String keyword, Pageable pageable, Class<T> clazz, U repository) {
        if (advQuery != null && advQuery.size() > 0 || !StringUtils.isEmpty(keyword)) {
            Specification specification = (root, criteriaQuery, criteriaBuilder) ->
                    generateCommonPredicate(advQuery, keyword, -1, clazz, root, criteriaQuery, criteriaBuilder);
            return new PagingResult(repository.findAll(specification, pageable));
        } else {
            return new PagingResult(repository.findAll(pageable));
        }
    }

    public <T, U extends JpaSpecificationExecutor<T> & PagingAndSortingRepository<T, ?>> Iterable<T> findAll(Map advQuery, String keyword, int maxSize, Class<T> clazz, U repository) {
        PageRequest pageRequest = PageRequest.of(0, maxSize);

        if (advQuery != null && advQuery.size() > 0 || !StringUtils.isEmpty(keyword)) {
            Specification specification = (root, criteriaQuery, criteriaBuilder) ->
                    generateCommonPredicate(advQuery, keyword, maxSize, clazz, root, criteriaQuery, criteriaBuilder);
            if (maxSize > 0) {
                return repository.findAll(specification, pageRequest).getContent();
            }
            return repository.findAll(specification);

        } else {
            if (maxSize > 0) {
                return repository.findAll(pageRequest).getContent();
            }
            return repository.findAll();
        }
    }

    public <T, U extends JpaSpecificationExecutor<T> & PagingAndSortingRepository<T, ?>> Iterable<T> findAll(Map advQuery, String keyword, Class<T> clazz, U repository) {
        if (advQuery != null && advQuery.size() > 0 || !StringUtils.isEmpty(keyword)) {
            Specification specification = (root, criteriaQuery, criteriaBuilder) ->
                    generateCommonPredicate(advQuery, keyword, clazz, root, criteriaQuery, criteriaBuilder);
            return repository.findAll(specification);
        } else {
            return repository.findAll();
        }
    }

    /**
     * 根据字段排序
     * 排序 格式：xxx_descend 或XXX ；xxx代表字段名，只有字段名默认升序，代表字段名后加"_descend"代表降序
     *
     * @param advQuery
     * @param keyword
     * @param sorter
     * @param clazz
     * @param repository
     * @param <T>
     * @param <U>
     * @return
     */
    public <T, U extends JpaSpecificationExecutor<T> & PagingAndSortingRepository<T, ?>> Iterable<T> findAll(Map advQuery, String keyword, String sorter, Class<T> clazz, U repository) {
        if (advQuery != null && advQuery.size() > 0 || !StringUtils.isEmpty(keyword) || !StringUtils.isEmpty(sorter)) {
            Specification specification = (root, criteriaQuery, criteriaBuilder) ->
                    generateCommonPredicate(advQuery, keyword, clazz, root, criteriaQuery, criteriaBuilder);
            Sort sort;
            if (!StringUtils.isEmpty(sorter)) {
                String sortName[] = sorter.split("_");

                Sort.Direction direction = Sort.Direction.ASC;
                if (sortName.length > 1 && sortName[1].equalsIgnoreCase("descend")) {
                    direction = Sort.Direction.DESC;
                }
                Sort.Order order = new Sort.Order(direction, sortName[0]);
                sort = Sort.by(order);
                return repository.findAll(specification, sort);

            }
            return repository.findAll(specification);

        } else {
            return repository.findAll();
        }
    }


    public Predicate generateCommonPredicate(Map<String, Object> matchMap, String keyword, int maxSize, Class clazz, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate resultPredicate = null;
        List<Predicate> andPredicates = new ArrayList<>();
        if (matchMap != null) {
            for (String key : matchMap.keySet()) {
                Object value = matchMap.get(key);
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                Predicate predicate = generatePredicateByClassAndKey(clazz, root, criteriaBuilder, key, value);
                if (predicate == null) {
                    continue;
                }
                andPredicates.add(predicate);
            }
        }
        if (!StringUtils.isEmpty(keyword)) {
            List<Predicate> orPredicates = generateOrPredicateListByKeyword(clazz, root, criteriaBuilder, keyword);
            if (orPredicates.size() > 0) {
                andPredicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
        }
        if (andPredicates.size() > 0) {
            resultPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        }
        return resultPredicate;
    }

    public Predicate generateCommonPredicate(Map<String, Object> matchMap, String keyword, Class clazz, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate resultPredicate = null;
        List<Predicate> andPredicates = new ArrayList<>();
        if (matchMap != null) {
            for (String key : matchMap.keySet()) {
                Object value = matchMap.get(key);
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                Predicate predicate = generatePredicateByClassAndKey(clazz, root, criteriaBuilder, key, value);
                if (predicate == null) {
                    continue;
                }
                andPredicates.add(predicate);
            }
        }
        if (!StringUtils.isEmpty(keyword)) {
            List<Predicate> orPredicates = generateOrPredicateListByKeyword(clazz, root, criteriaBuilder, keyword);
            if (orPredicates.size() > 0) {
                andPredicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
        }
        if (andPredicates.size() > 0) {
            resultPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        }
        return resultPredicate;
    }

    private List<Predicate> generateOrPredicateListByKeyword(Class<? extends Entity> clazz, Root root, CriteriaBuilder criteriaBuilder, String keyword) {
        List<Predicate> orPredicate = new ArrayList<>();
        keyword = "%" + keyword + "%";
        Field[] fieldList = clazz.getDeclaredFields();
        for (Field field : fieldList) {
            if (reflectionUtil.isBaseType(field) && field.getAnnotation(Transient.class) == null) {
                Predicate predicate = criteriaBuilder.like(root.get(field.getName()).as(String.class), keyword);
                orPredicate.add(predicate);
            }
        }
        return orPredicate;
    }


    /**
     * @param clazz
     * @param root
     * @param criteriaBuilder
     * @param key
     * @param value
     * @return
     */


    private Predicate generatePredicateByClassAndKey(Class<? extends Entity> clazz, Root root, CriteriaBuilder criteriaBuilder, String key, Object value) {
        Predicate predicate = null;
        Object objectValue;
        QueryConditionProperties queryConditionProperties = new QueryConditionProperties();
        conditionMap = queryConditionProperties.getConditionMap();
        String split = conditionMap.get("split");
        try {
            if (key.contains(split) && key.split(split).length > 0) {
                String condition = key.split(split)[1];
                String fieldName = key.split(split)[0];
                Field field = clazz.getDeclaredField(fieldName);
                ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                Transient aTransient = field.getAnnotation(Transient.class);
                //多对多，一对多，不做考虑
                if (manyToMany != null || oneToMany != null || aTransient != null) {
                    return null;
                }
                try {
                    objectValue = reflectionUtil.priseObject(field, value);
                } catch (Exception e) {
                    objectValue = value;
                }
                String conditionName = conditionMap.get(condition);

                switch (conditionName) {
                    case "range":
                        String str = objectValue.toString();
                        String[] strArr = str.split(" - ");
                        if(strArr.length == 2){
                            predicate = criteriaBuilder.between(root.get(fieldName).as(String.class), (Comparable) strArr[0], (Comparable) strArr[1]);
                        }
                        break;
                    case "lessThan": //less than
                        predicate = criteriaBuilder.lessThan(root.get(fieldName).as(Comparable.class), (Comparable) objectValue);
                        break;
                    case "lessEqual": //less equal
                        predicate = criteriaBuilder.lessThanOrEqualTo(root.get(fieldName).as(Comparable.class), (Comparable) objectValue);
                        break;
                    case "moreThan": //more than
                        predicate = criteriaBuilder.greaterThan(root.get(fieldName).as(Comparable.class), (Comparable) objectValue);
                        break;
                    case "moreEqual": //more equal
                        predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName).as(Comparable.class), (Comparable) objectValue);
                        break;
                    case "like":
                        predicate = criteriaBuilder.like(root.get(fieldName).as(String.class), "%" + objectValue + "%");
                        break;
                    case "in":
                        if (objectValue instanceof Collection) {
                            predicate = root.get(fieldName).in((Collection) objectValue);
                        }
                        break;
                    case "notEqual":
                        predicate = criteriaBuilder.notEqual(root.get(fieldName).as(String.class), objectValue);
                        break;
                    case "null":
                        boolean isNull = (boolean) objectValue;
                        if (isNull) {
                            predicate = root.get(fieldName).isNull();
                        } else {
                            predicate = root.get(fieldName).isNotNull();
                        }
                        break;
                }
            } else {
                Field field = clazz.getDeclaredField(key);
                objectValue = reflectionUtil.priseObject(field, value);
                predicate = criteriaBuilder.equal(root.get(key), objectValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        return predicate;
    }
}
