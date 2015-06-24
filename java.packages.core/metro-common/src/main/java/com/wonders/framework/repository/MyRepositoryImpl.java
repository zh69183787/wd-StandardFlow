package com.wonders.framework.repository;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.commons.lang3.reflect.FieldUtils.getField;
import static org.apache.commons.lang3.reflect.MethodUtils.invokeMethod;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.xpath.axes.HasPositionalPredChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.entity.SearchType;

@SuppressWarnings("rawtypes")
public class MyRepositoryImpl<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements MyRepository<T, ID> {

	private final Class<T> domainClass;
	private final EntityManager entityManager;
	
	static {
		ConvertUtils.register(new AbstractConverter() {
			@Override
			protected Object convertToType(Class type, Object value)
					throws Throwable {
				
				if (value instanceof String) {
					return DateUtils.parseDate((String) value, "yyyy-MM-dd");
				}
				return value;
			}
			
			@Override
			protected Class getDefaultType() {
				return Date.class;
			}
		}, Date.class);
		
		ConvertUtils.register(new AbstractConverter() {
			@Override
			protected Object convertToType(Class type, Object value)
					throws Throwable {
				
				if (value instanceof String) {
					return NumberUtils.toLong((String) value);
				}
				return value;
			}
			
			@Override
			protected Class getDefaultType() {
				return String.class;
			}
		}, Serializable.class);
	}

	public MyRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);

		this.domainClass = domainClass;
		this.entityManager = entityManager;
	}

	@Override
	public Page<T> findAll(Map<?, ?> params, Pageable pageable) {
		
			TypedQuery<T> query = getQuery(params, pageable);
		return pageable == null ? new PageImpl<T>(query.getResultList()) : readPage(query, pageable, params);
	}

	private TypedQuery<T> getQuery(Map<?, ?> params, Pageable pageable) {
		
		Sort sort = pageable == null ? null : pageable.getSort();
		return getQuery(params, sort);
	}

	private TypedQuery<T> getQuery(Map<?, ?> params, Sort sort) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(domainClass);

		Root<T> root = query.from(domainClass);
		query.select(root);

		applySearchParamsCriteria(params, query,".");

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return entityManager.createQuery(query);
	}

	private TypedQuery<Long> getCountQuery(Map<?, ?> params) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = query.from(domainClass);
		query.select(builder.count(root));

		applySearchParamsCriteria(params, query,".");

		return entityManager.createQuery(query);
	}

	private <S> void applySearchParamsCriteria(Map<?, ?> params, CriteriaQuery<S> query,String splitStr) {
		if (MapUtils.isEmpty(params)) {
			return;
		}
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		List<Predicate> predicates = this.getPredicates(params,query,splitStr);
		List<Predicate> predicates = this.getPredicateALL(params, query, splitStr);
		if (CollectionUtils.isNotEmpty(predicates)) {
			query.where(predicates.toArray(new Predicate[predicates.size()]));
		}
		
	}
	private <S> List<Predicate>  getPredicateALL(Map<?, ?> params, CriteriaQuery<S> query,String splitStr){
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Map<String, Map<Object, Object>> psMap = new HashMap<String, Map<Object,Object>>();
		Map<Object, Object> pMapTemp;
		
		Map<Object, Object> pMap = new HashMap<Object, Object>();
		for (Object key : params.keySet()) {
			String[] objkeys = key.toString().split("_");
			if(objkeys.length == 3){
				
				String childKey = objkeys[0]+"_"+objkeys[1];
				String operator = objkeys[2];
				Object valueObj =  params.get(key);
				
				pMapTemp = null;
				pMapTemp = psMap.get(operator);
				if(pMapTemp == null){
					pMapTemp = new HashMap<Object, Object>();
					psMap.put(operator, pMapTemp);
				}
				pMapTemp.put(childKey, valueObj);
			}else{
				pMap.put(key, params.get(key));
			}
		}
		
		if(pMap.size() > 0){
			List<Predicate> predicateTemp = getPredicates(pMap, query, splitStr);
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			if(predicateTemp != null && predicateTemp.size() > 0){
				predicates.add(builder.and(predicateTemp.toArray(new Predicate[predicateTemp.size()])));
			}
		}
		for(String key : psMap.keySet()){
			List<Predicate> predicateTemp = getPredicates(psMap.get(key), query, splitStr);
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			if(predicateTemp != null && predicateTemp.size() > 0){
				String s = key.substring(0, 1);
				if("r".equals(s)){
					predicates.add(builder.or(predicateTemp.toArray(new Predicate[predicateTemp.size()])));
				}else{
					predicates.addAll(predicateTemp);
				}
			}
		}
		
		return predicates;
		
	}
	@SuppressWarnings("unchecked")
	private <S> List<Predicate>  getPredicates(Map<?, ?> params, CriteriaQuery<S> query,String splitStr){
		
		
		Root<T> root =  (Root<T>) query.getRoots()
			.iterator().next();
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Object key : params.keySet()) {
			
			int ln =((String) key).lastIndexOf("_");
			String name = ((String)key).substring(0,ln);//substringBefore((String) key, "_");
			String operator = ((String)key).substring(ln+1);//substringAfter((String) key, "_");
			Object valueObj =  params.get(key);
			String value =  (String)valueObj;
			if (isBlank(value) 
					&& !startsWith(operator, "is")
					&& !StringUtils.equals(operator, "fetch")) {
				continue;
			}
			
			if (StringUtils.equals(operator, "fetch")) {
				if (query.getSelection() == root) {
					JoinType jt = JoinType.INNER;
					if (isNotBlank(value)) {
						jt = EnumUtils.getEnum(JoinType.class, upperCase(value));
					}
					root.fetch(name, jt);
					//root.
				}
				continue;
			}
			
			try {
				Predicate pc = buildPredicate( root, name, operator, valueObj, query,splitStr);
				if(pc!=null){
					predicates.add(pc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return predicates;
	}

	@SuppressWarnings("unchecked")
	private <S> Predicate buildPredicate( Root<T> root,
			String name, String operator, Object value,CriteriaQuery<S> query,String splitStr) throws Exception {

		String[] names = split(name, splitStr);
		Path path = null ;
		Object val = null;
		if(name.indexOf("+")<0&&name.indexOf("-")<0){
				path = root.get(names[0]);
			for (int i = 1; i < names.length; i++) {
				path = path.get(names[i]);
			}
			
			val= convert(names, (String)value);
		}
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		
		SearchType st = SearchType.parseValue(operator);
		if(st != null){
			switch (st) {
				case EQ :
					return builder.equal(path, val);
				case LIKE:
					return builder.like(path, "%" + val + "%");
				case GT:
					return builder.greaterThan(path, (Comparable) val);
				case LT:
					return builder.lessThan(path, (Comparable) val);
				case GE:
					return builder.greaterThanOrEqualTo(path, (Comparable) val);
				case LE:
					return builder.lessThanOrEqualTo(path, (Comparable) val);
				case NE :
					
					return builder.notEqual(path, val);					
				case OR :
//					search_ projectBase^code_eq+projectId_eq-projectTitle_eq_or
					String[] na_ors = split(name, "-");// "-" 或
					Predicate[] p_or =new Predicate[na_ors.length];
					
					if("".equals(value)){return null;}
					String[] values = split((String)value, ",");
					int k=0;
					List<Predicate> predicateAll = new ArrayList<Predicate>();//   bug
					for(int i=0;i<na_ors.length;i++){
						String[]  na_ands = split(na_ors[i], "+");
						Map<String, Object> paramor = new HashMap<String, Object>();
						for(int j=0;j<na_ands.length;j++){
							if(k+1>values.length){continue;}
							paramor.put(na_ands[j], values[k]);
							k++;
						}
						
						List<Predicate> predicates = getPredicates(paramor,query,"^");
						predicateAll.add(builder.and(predicates.toArray(new Predicate[predicates.size()])));
					//	p_or[i] =  builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
					/*for(List<Predicate> ps :predicateAll){
						
					}*/
					
					
					return builder.or(predicateAll.toArray(new Predicate[predicateAll.size()]));	
				case NNULL :
					return builder.isNotNull(path);
				case NB :
					return builder.notEqual(path,"");
				case ISNULL :
					return builder.isNull(path);
				case ISEMPTY :
					return builder.equal(path, "");
			}
		}
		
		if(name.indexOf("+")>=0||name.indexOf("-")>=0){return null;}
		
		if (StringUtils.startsWith(operator, "is")) {
			return (Predicate) invokeMethod(builder, operator, path);
		}
		
		return (Predicate) invokeMethod(builder, operator, path, val);
	}

	private Page<T> readPage(TypedQuery<T> query, Pageable pageable,
			Map<?, ?> params) {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		long total = getCountQuery(params).getSingleResult();
		List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}
	
	private Object convert(String[] names, String value) {
		
		if (isBlank(value)) {
			return value;
		}
		
		Class<?> cls = getField(domainClass, names[0], true).getType();
		for (int i = 1; i < names.length; i++) {
			cls =  getField(cls, names[i], true).getType();
		}
		if(cls.isEnum())
			return EnumUtils.getEnum((Class<? extends Enum>)cls, upperCase(value));
		return ConvertUtils.convert(value, cls);
 	}

	@Override
	@Transactional
	public <S extends T> List<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();
		int i = 0;
		int round = 30;
		if (entities == null) {
			return result;
		}

		for (S entity : entities) {
			result.add(save(entity));
			if(i%round == 0){
				entityManager.flush();
				entityManager.clear();
			}
			i++;
		}
		
		return result;
	}
	
	@Transactional
	public int doHql(String hql) {
		int o = entityManager.createQuery(hql).executeUpdate();
		entityManager.flush();
		entityManager.clear();
		return o;
	}
	
	@Transactional(readOnly = true)
	public Object loadObjectByHql(String hql) {
		Object o = entityManager.createQuery(hql).getSingleResult();
		return o;
	}
	
}
